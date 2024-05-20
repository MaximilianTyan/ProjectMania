package com.zondayland.items.claims;

import com.zondayland.ZondayLand;
import com.zondayland.network.PacketIdentifiers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;

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
    protected void onClaimSuccess(World world, PlayerEntity user, ChunkPos pos) {
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
