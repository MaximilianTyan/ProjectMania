package io.github.maximiliantyan.projectmania.items.claims;

import io.github.maximiliantyan.projectmania.ModMain;
import io.github.maximiliantyan.projectmania.network.PacketsIdentifier;
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
        ModMain.LOGGER.info("Attempting unclaim of chunk {} as {}", chunkPos.toString(), user.getName());

//        try {
//            return 1 == user.getServer()
//                .getCommandManager()
//                .getDispatcher()
//                .execute(UnclaimCommand, user.getCommandSource());
//        } catch (CommandSyntaxException e) {
//            ModMain.LOGGER.error(String.format("Error %s executing %s command: %s", e.getType(), UnclaimCommand, e.getMessage()));
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
                    PacketsIdentifier.S2C.UNCLAIM_SUCCESS,
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
