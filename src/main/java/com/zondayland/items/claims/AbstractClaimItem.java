package com.zondayland.items.claims;

import com.google.common.collect.Sets;
import com.zondayland.ZondayLand;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
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
    public AbstractClaimItem(Item.Properties settings) {
        super(settings);
    }

    protected static int OpacClaimProcess(
            ServerPlayer player, boolean shouldClaim, boolean shouldServerClaim, boolean shouldReplace
    ) {
        ServerLevel world = (ServerLevel) player.level();
        ServerPlayerData playerData = (ServerPlayerData) ServerPlayerDataAPI.from(player);

        MinecraftServer server = world.getServer();
        IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>
                serverData = ServerData.from(server);

        int chunkX = player.chunkPosition().x;
        int chunkZ = player.chunkPosition().z;

        UUID claimId = shouldServerClaim ? PlayerConfig.SERVER_CLAIM_UUID : player.getUUID();

        // Timing shinanigans -------------
        if (serverData.getServerTickHandler().getTickCounter() == playerData.getClaimActionRequestHandler()
                                                                            .getLastRequestTickCounter()) {
            player.sendSystemMessage(Component.literal("Could not (un)claim, timing error"));
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
                IPlayerConfig playerConfig = serverData.getPlayerConfigs().getLoadedConfig(player.getUUID());
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
                        world.dimension().location(),
                        claimId,
                        subConfigIndex,
                        player.chunkPosition().x,
                        player.chunkPosition().z,
                        chunkX,
                        chunkZ,
                        shouldReplace
                );

                if (result.getResultType().success) {
                    player.sendSystemMessage(Component.literal("Successfully claimed chunk (%d, %d)".formatted(chunkX, chunkZ)));
                    return 1;
                } else {
                    player.sendSystemMessage(Component.literal("Could not claim chunk: " + result.getResultType()
                                                                                 .toString()
                                                                                 .toLowerCase()
                                                                                 .replace('_', ' ')));
                    return 0;
                }

            } else {
                // Unclaim action
                result = claimsManager.tryToUnclaim(
                        world.dimension().location(),
                        claimId,
                        player.chunkPosition().x,
                        player.chunkPosition().z,
                        chunkX,
                        chunkZ,
                        shouldReplace
                );
                if (result.getResultType().success) {
                    player.sendSystemMessage(Component.literal("Successfully unclaimed chunk (%d, %d)".formatted(chunkX, chunkZ)));
                    return 1;
                } else {
                    player.sendSystemMessage(Component.literal("Could not unclaim chunk: " + result.getResultType()
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
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player user, InteractionHand hand) {
        if (world.isClientSide) return super.use(world, user, hand);

        ItemStack itemStack = user.getItemInHand(hand);

        if (!this.claimOperation(world, user)) {
            ZondayLand.LOGGER.info("Server: Failed to (server)(un)claim chunk");
            return InteractionResultHolder.fail(itemStack);
        }

        this.onClaimSuccess(world, user, user.chunkPosition());

        if (!user.getAbilities().invulnerable) {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.success(itemStack);
    }

    protected abstract boolean claimOperation(Level world, Player user);

    protected abstract void onClaimSuccess(Level world, Player user, ChunkPos pos);
}
