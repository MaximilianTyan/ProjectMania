package com.zondayland.items.claims;

import com.google.common.collect.Sets;
import com.zondayland.ZondayLand;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.IPlayerClaimPosList;
import xaero.pac.common.claims.player.IPlayerDimensionClaims;
import xaero.pac.common.claims.result.api.AreaClaimResult;
import xaero.pac.common.claims.result.api.ClaimResult;
import xaero.pac.common.parties.party.IPartyPlayerInfo;
import xaero.pac.common.parties.party.ally.IPartyAlly;
import xaero.pac.common.parties.party.member.IPartyMember;
import xaero.pac.common.server.IServerData;
import xaero.pac.common.server.ServerData;
import xaero.pac.common.server.claims.IServerClaimsManager;
import xaero.pac.common.server.claims.IServerDimensionClaimsManager;
import xaero.pac.common.server.claims.IServerRegionClaims;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.claims.sync.ClaimsManagerSynchronizer;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

import java.util.UUID;

public abstract class AbstractClaimItem extends Item {
    public AbstractClaimItem(Settings settings) {
        super(settings);
    }

    protected static int OpacClaimProcess(
            ServerPlayerEntity player, boolean shouldClaim, boolean shouldServerClaim, boolean shouldReplace
    ) {
        ServerWorld world = player.getServerWorld();
        ServerPlayerData playerData = (ServerPlayerData) ServerPlayerDataAPI.from(player);

        MinecraftServer server = world.getServer();
        IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>
                serverData = ServerData.from(server);

        int chunkX = player.getChunkPos().x;
        int chunkZ = player.getChunkPos().z;

        UUID claimId = shouldServerClaim ? PlayerConfig.SERVER_CLAIM_UUID : player.getUuid();

        // Timing shinanigans -------------
        if (serverData.getServerTickHandler().getTickCounter() == playerData.getClaimActionRequestHandler()
                                                                            .getLastRequestTickCounter()) {
            player.sendMessage(Text.of("Could not (un)claim, timing error"));
            return 0;//going too fast
        }
        playerData.getClaimActionRequestHandler()
                  .setLastRequestTickCounter(serverData.getServerTickHandler().getTickCounter());
        // Timing shinanigans -------------

        // ServerClaimsManager // OPAC
        IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>
                claimsManager = serverData.getServerClaimsManager();

        claimsManager.getPermissionHandler().ensureAdminModeStatusPermission(player, playerData);
        ClaimResult<?> result = null;
        try {
            if (shouldClaim) {
                // Claim action
                IPlayerConfig playerConfig = serverData.getPlayerConfigs().getLoadedConfig(player.getUuid());
                IPlayerConfig usedSubConfig =
                        shouldServerClaim ? playerConfig.getUsedServerSubConfig() : playerConfig.getUsedSubConfig();
                int subConfigIndex = usedSubConfig.getSubIndex();

                // public ClaimResult<PlayerChunkClaim>
                // tryToClaim(@Nonnull ResourceLocation dimension,
                // @Nonnull UUID claimId,
                // int subConfigIndex,
                // int fromX,
                // int fromZ,
                // int x,
                // int z,
                // boolean replace
                result = claimsManager.tryToClaim(
                        world.getDimensionKey().getValue(),
                        claimId,
                        subConfigIndex,
                        player.getChunkPos().x,
                        player.getChunkPos().z,
                        chunkX,
                        chunkZ,
                        shouldReplace
                );

                if (result.getResultType().success) {
                    player.sendMessage(Text.of("Successfully claimed chunk (%d, %d)".formatted(chunkX, chunkZ)));
                    return 1;
                } else {
                    player.sendMessage(Text.of("Could not claim chunk: " + result.getResultType()
                                                                                 .toString()
                                                                                 .toLowerCase()
                                                                                 .replace('_', ' ')));
                    return 0;
                }

            } else {
                // Unclaim action
                result = claimsManager.tryToUnclaim(
                        world.getDimensionKey().getValue(),
                        claimId,
                        player.getChunkPos().x,
                        player.getChunkPos().z,
                        chunkX,
                        chunkZ,
                        shouldReplace
                );
                if (result.getResultType().success) {
                    player.sendMessage(Text.of("Successfully unclaimed chunk (%d, %d)".formatted(chunkX, chunkZ)));
                    return 1;
                } else {
                    player.sendMessage(Text.of("Could not unclaim chunk: " + result.getResultType()
                                                                                   .toString()
                                                                                   .toLowerCase()
                                                                                   .replace('_', ' ')));
                    return 0;
                }
            }
        } finally {
            if (result != null)
                ((ClaimsManagerSynchronizer) claimsManager.getClaimsManagerSynchronizer()).syncToPlayerClaimActionResult(
                        new AreaClaimResult(Sets.newHashSet(result.getResultType()), chunkX, chunkZ, chunkX, chunkZ),
                        player
                );
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient()) return super.use(world, user, hand);

        ItemStack itemStack = user.getStackInHand(hand);

        if (!this.claimOperation(world, user)) {
            ZondayLand.LOGGER.info("Server: Failed to (server)(un)claim chunk");
            return TypedActionResult.fail(itemStack);
        }

        this.onClaimSuccess(world, user, user.getChunkPos());

        if (!user.getAbilities().creativeMode) {
            itemStack.decrement(1);
        }

        return TypedActionResult.success(itemStack, true);
    }

    protected abstract boolean claimOperation(World world, PlayerEntity user);

    protected abstract void onClaimSuccess(World world, PlayerEntity user, ChunkPos pos);
}
