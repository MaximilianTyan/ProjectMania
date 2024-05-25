package com.zondayland.items.claims;

import com.zondayland.ZondayLand;
import com.zondayland.network.PacketIdentifiers;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class UnclaimParchmentItem extends AbstractClaimItem {
//    private static final String UnclaimCommand = "openpac-claims unclaim";

    public UnclaimParchmentItem(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean claimOperation(Level world, Player user) {
        ChunkPos chunkPos = user.chunkPosition();
        ZondayLand.LOGGER.info("Attempting unclaim of chunk " + chunkPos.toString() + " as " + user.getName());

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
        return 1 == OpacClaimProcess((ServerPlayer) user, false, false, false);
    }

    @Override
    protected void onClaimSuccess(Level world, Player user, ChunkPos pos) {
        {
            ServerPlayNetworking.send(
                    (ServerPlayer) user,
                    PacketIdentifiers.UNCLAIM_SUCCESS,
                    PacketByteBufs.empty()
            );

            world.playSound(
                    (Player) null,
                    user.blockPosition(),
                    SoundEvents.ANVIL_BREAK,
                    SoundSource.PLAYERS
            );
        }
    }
}
