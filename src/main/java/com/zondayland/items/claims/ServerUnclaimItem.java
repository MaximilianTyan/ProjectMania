package com.zondayland.items.claims;

import com.zondayland.ZondayLand;
import com.zondayland.network.PacketsIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ServerUnclaimItem extends AbstractClaimItem {
//    private static final String UnclaimCommand = "openpac-claims server unclaim anyway";

    public ServerUnclaimItem(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean claimOperation(Level world, Player user) {
        ChunkPos chunkPos = user.chunkPosition();
        ZondayLand.LOGGER.info("Attempting server unclaim of chunk {} as {}", chunkPos.toString(), user.getName());

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
        return 1 == OpacClaimProcess((ServerPlayer) user, false, true, true);
    }

    @Override
    protected void onClaimSuccess(Level world, Player user, ChunkPos pos) {
        {
            ServerPlayNetworking.send(
                    (ServerPlayer) user,
                    PacketsIdentifier.S2C.UNCLAIM_SUCCESS,
                    PacketByteBufs.empty()
            );

            world.playSound(
                    (Player) null,
                    user.blockPosition(),
                    SoundEvents.ANVIL_DESTROY,
                    SoundSource.PLAYERS
            );
        }
    }
}
