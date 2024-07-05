package com.zondayland.items.claims;

import com.zondayland.ZondayLand;
import com.zondayland.network.PacketsIdentifier;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;

public class ClaimParchmentItem extends AbstractClaimItem {
//    private static final String ClaimCommand = "openpac-claims claim";

    public ClaimParchmentItem(Item.Properties settings) {
        super(settings);
    }

    @Override
    protected boolean claimOperation(net.minecraft.world.level.Level world, Player user) {
        ChunkPos chunkPos = user.chunkPosition();
        ZondayLand.LOGGER.info("Attempting claim of chunk {} as {}", chunkPos.toString(), user.getName());

//        try {
//            return 1 == user.getServer()
//                .getCommandManager()
//                .getDispatcher()
//                .execute(ClaimCommand, user.getCommandSource());
//        } catch (CommandSyntaxException e) {
//            ZondayLand.LOGGER.error(String.format("Error %s executing %s command: %s", e.getType(), ClaimCommand, e.getMessage()));
//            user.sendMessage(Text.of("Could not claim chunk: " + e.getMessage()));
//            return false;
//        }
        return 1 == OpacClaimProcess((ServerPlayer) user, true, false, false);
    }

    @Override
    protected void onClaimSuccess(Level world, Player user, ChunkPos pos) {
        ServerPlayNetworking.send(
                (ServerPlayer) user,
                PacketsIdentifier.S2C.CLAIM_SUCCESS,
                PacketByteBufs.empty()
        );

        world.playSound(
                (Player) null,
                user.blockPosition(),
                SoundEvents.PLAYER_LEVELUP,
                SoundSource.PLAYERS
        );

    }
}
