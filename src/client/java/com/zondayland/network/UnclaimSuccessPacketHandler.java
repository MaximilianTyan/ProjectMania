package com.zondayland.network;

import com.zondayland.ZondayLandClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.ChunkPos;

public class UnclaimSuccessPacketHandler {
    public static void receive(
            MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender
    ) {
        ZondayLandClient.LOGGER.info("Unclaim Success packet received");
        client.execute(() -> action(client, handler, buf, responseSender));
    }

    public static void action(
            MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender
    ) {
        ClientWorld world = client.world;
        ClientPlayerEntity player = client.player;
        ChunkPos pos = player.getChunkPos();

        final int particlePerBlock = 4;
        final int maxXOffset = 16 * particlePerBlock;
        final int maxZOffset = 16 * particlePerBlock;
        for (int xOffset = 0; xOffset < maxXOffset; xOffset += 1) {
            for (int zOffset = 0; zOffset < maxZOffset; zOffset += 1) {

                if (xOffset != 0 && xOffset != maxXOffset-1 && zOffset != 0 && zOffset != maxZOffset-1) continue;

                world.addParticle(
                        ParticleTypes.FLAME,
                        pos.getStartX() + (double) xOffset / particlePerBlock,
                        player.getY() + 0.5,
                        pos.getStartZ() + (double) zOffset / particlePerBlock,
                        0, 0, 0
                );
            }
        }
    }
}

