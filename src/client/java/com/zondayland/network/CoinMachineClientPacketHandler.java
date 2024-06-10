package com.zondayland.network;

import com.zondayland.ZondayLandClient;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;

public class CoinMachineClientPacketHandler {

    public static void receiveWheelStepPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        Player player = minecraft.player;
        if (player != null) {
            player.playSound(SoundEvents.DISPENSER_DISPENSE, 1.0f, 2.0f);
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Wheel Step: Player is null");
        }
    }

    public static void receiveWheelStopPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        Player player = minecraft.player;
        if (player != null) {
            player.playSound(SoundEvents.BELL_BLOCK, 1.0f, 2.0f);
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Wheel Stop: Player is null");
        }
    }

    public static void receiveWinPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        Player player = minecraft.player;
        if (player != null) {
            player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.75f, 2.0f);
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Win packet: Player is null");
        }
    }

    public static void receiveLosePacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        Player player = minecraft.player;
        if (player != null) {
            player.playSound(SoundEvents.ALLAY_HURT, 1.0f, 0.5f);
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Lose packet: Player is null");
        }
    }

}
