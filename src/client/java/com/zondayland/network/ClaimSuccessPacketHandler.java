package com.zondayland.network;

import com.zondayland.ZondayLandClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.ChunkPos;

public class ClaimSuccessPacketHandler {
    public static void receive(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        ZondayLandClient.LOGGER.info("Claim Success packet received");
        minecraft.execute(() -> action(minecraft, clientPacketListener, friendlyByteBuf, packetSender));
    }

    public static void action(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        ClientLevel world = minecraft.level;
        LocalPlayer player = minecraft.player;
        ChunkPos pos = player.chunkPosition();

        final int particlePerBlock = 4;
        final int maxXOffset = (pos.getMaxBlockX() - pos.getMinBlockX()) * particlePerBlock;
        final int maxZOffset = (pos.getMaxBlockZ() - pos.getMinBlockZ()) * particlePerBlock;
        for (int xOffset = 0; xOffset < maxXOffset; xOffset += 1) {
            for (int zOffset = 0; zOffset < maxZOffset; zOffset += 1) {

                if (xOffset != 0 && xOffset != maxXOffset - 1 && zOffset != 0 && zOffset != maxZOffset - 1) continue;

                world.addParticle(
                        ParticleTypes.HAPPY_VILLAGER,
                        pos.getMinBlockX() + (double) xOffset / particlePerBlock,
                        player.getY() + 0.5,
                        pos.getMinBlockZ() + (double) zOffset / particlePerBlock,
                        0,
                        0,
                        0
                );
            }
        }
    }

}
