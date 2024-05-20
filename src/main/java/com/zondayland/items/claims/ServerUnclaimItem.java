package com.zondayland.items.claims;

import com.google.common.collect.Sets;
import com.zondayland.ZondayLand;
import com.zondayland.network.PacketIdentifiers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
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
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

public class ServerUnclaimItem extends AbstractClaimItem {
//    private static final String UnclaimCommand = "openpac-claims server unclaim anyway";

    public ServerUnclaimItem(Settings settings) {
        super(settings);
    }

    private static void ServerUnclaimChunk(MinecraftServer server, World world, ServerPlayerEntity serverPlayer) {
        ChunkPos chunkPos = serverPlayer.getChunkPos();

        // OPAC ServerPlayerData
        ServerPlayerData playerData = (ServerPlayerData) ServerPlayerDataAPI.from(serverPlayer);

        // OPAC ServerData
        IServerData<IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>, IServerParty<IPartyMember, IPartyPlayerInfo, IPartyAlly>>
                serverData = ServerData.from(server);

        // Timing shinanigans
        if (serverData.getServerTickHandler().getTickCounter() == playerData.getClaimActionRequestHandler()
                                                                            .getLastRequestTickCounter()) {
            ZondayLand.LOGGER.warn("Too fast !");
            return;//going too fast
        }
        playerData.getClaimActionRequestHandler()
                  .setLastRequestTickCounter(serverData.getServerTickHandler().getTickCounter());

        // OPAC ServerClaimsManager
        IServerClaimsManager<IPlayerChunkClaim, IServerPlayerClaimInfo<IPlayerDimensionClaims<IPlayerClaimPosList>>, IServerDimensionClaimsManager<IServerRegionClaims>>
                claimsManager = serverData.getServerClaimsManager();

        // 	public ClaimResult<PlayerChunkClaim> tryToUnclaim(@Nonnull ResourceLocation dimension, @Nonnull UUID id, int fromX, int fromZ, int x, int z, boolean replace) {
        ClaimResult<IPlayerChunkClaim> result = claimsManager.tryToUnclaim(
                world.getDimensionKey().getValue(),
                PlayerConfig.SERVER_CLAIM_UUID,
                chunkPos.x,
                chunkPos.z,
                chunkPos.x,
                chunkPos.z,
                true
        );

        if (result.getResultType().fail) {
            ZondayLand.LOGGER.error("Unable to unclaim chunk: " + result.getResultType().toString());
        }

        if (result != null)
            ((ClaimsManagerSynchronizer) claimsManager.getClaimsManagerSynchronizer()).syncToPlayerClaimActionResult(
                    new AreaClaimResult(
                            Sets.newHashSet(result.getResultType()),
                            chunkPos.x,
                            chunkPos.z,
                            chunkPos.x,
                            chunkPos.z
                    ),
                    serverPlayer
            );
    }


    @Override
    protected boolean claimOperation(World world, PlayerEntity user) {
        ChunkPos chunkPos = user.getChunkPos();
        ZondayLand.LOGGER.info("Attempting server unclaim of chunk " + chunkPos.toString() + " as " + user.getEntityName());

//        try {
//            return 1 == user.getServer()
//                .getCommandManager()
//                .getDispatcher()
//                .execute(UnclaimCommand, user.getCommandSource());
//        } catch (CommandSyntaxException e) {
//            ZondayLand.LOGGER.error(String.format("Error %s executing %s command: %s", e.getType(), UnclaimCommand, e.getMessage()));
//            user.sendMessage(Text.of("Could not claim chunk: " + e.getMessage()));
//            return false;
//        }
        return 1 == OpacClaimProcess((ServerPlayerEntity) user, false, true, true);
    }

    @Override
    protected void onClaimSuccess(World world, PlayerEntity user, ChunkPos pos) {
        {
            ServerPlayNetworking.send(
                    (ServerPlayerEntity) user,
                    PacketIdentifiers.UNCLAIM_SUCCESS,
                    PacketByteBufs.empty()
            );

            world.playSound(
                    (PlayerEntity) null,
                    user.getBlockPos(),
                    SoundEvents.BLOCK_ANVIL_BREAK,
                    SoundCategory.PLAYERS
            );
        }
    }
}
