package com.zondayland.items.claims;

import com.google.common.collect.Sets;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.zondayland.ZondayLand;
import com.zondayland.network.PacketIdentifiers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
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
import xaero.pac.common.server.claims.ServerClaimsManager;
import xaero.pac.common.server.claims.player.IServerPlayerClaimInfo;
import xaero.pac.common.server.claims.sync.ClaimsManagerSynchronizer;
import xaero.pac.common.server.parties.party.IServerParty;
import xaero.pac.common.server.player.config.IPlayerConfig;
import xaero.pac.common.server.player.config.PlayerConfig;
import xaero.pac.common.server.player.data.ServerPlayerData;
import xaero.pac.common.server.player.data.api.ServerPlayerDataAPI;

import java.util.UUID;

public class ServerClaimItem extends AbstractClaimItem {
    private static final String ClaimCommand = "openpac-claims server claim anyway";

    public ServerClaimItem(Settings settings) {
        super(settings);
    }


    @Override
    protected boolean claimOperation(World world, PlayerEntity user) {
        ChunkPos chunkPos = user.getChunkPos();
        ZondayLand.LOGGER.info("Attempting server claim of chunk " + chunkPos.toString() + " as " + user.getEntityName());

//        try {
//            return 1 == user.getServer()
//                            .getCommandManager()
//                            .getDispatcher()
//                            .execute(ClaimCommand, user.getCommandSource());
//        } catch (CommandSyntaxException e) {
//            ZondayLand.LOGGER.error(String.format("Error %s executing %s command: %s", e.getType(), ClaimCommand, e.getMessage()));
//            user.sendMessage(Text.of("Could not claim chunk: " + e.getMessage()));
//            return false;
//        }

        return 1 == OpacClaimProcess((ServerPlayerEntity) user, true, true, true);
    }

    @Override
    protected void successAction(World world, PlayerEntity user, ChunkPos pos) {
        {
            ServerPlayNetworking.send(
                    (ServerPlayerEntity) user,
                    PacketIdentifiers.CLAIM_SUCCESS,
                    PacketByteBufs.empty()
            );

            world.playSound(
                    (PlayerEntity) null,
                    user.getBlockPos(),
                    SoundEvents.ENTITY_EVOKER_PREPARE_WOLOLO,
                    SoundCategory.PLAYERS
            );
        }
    }
}
