package com.zondayland.network;

import com.zondayland.ZondayLandClient;
import com.zondayland.screens.CoinMachineScreen;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;

public class CoinMachineClientPacketHandler {

    public static void receiveGameStartPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        if (minecraft.screen instanceof CoinMachineScreen screen) {
            screen.onGameStart();
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Start packet: invalid screen {}", minecraft.screen);
        }
    }

    public static void receiveWheelStepPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        if (minecraft.player == null) {
            ZondayLandClient.LOGGER.warn("Coin Machine: Wheel Step packet: Player is null");
            return;
        }
        minecraft.player.playSound(SoundEvents.DISPENSER_DISPENSE, 1.0f, 2.0f);
    }

    public static void receiveWheelStopPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        if (minecraft.player == null) {
            ZondayLandClient.LOGGER.warn("Coin Machine: Wheel Stop packet: Player is null");
            return;
        }
        minecraft.player.playSound(SoundEvents.BELL_BLOCK, 1.0f, 2.0f);
    }

    public static void receiveWinPacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        if (minecraft.player == null) {
            ZondayLandClient.LOGGER.warn("Coin Machine: Win packet: Player is null");
            return;
        }
        minecraft.player.playSound(SoundEvents.UI_TOAST_CHALLENGE_COMPLETE, 0.75f, 2.0f);

        if (minecraft.screen instanceof CoinMachineScreen screen) {
            screen.onGameEnd();
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Win packet: invalid screen {}", minecraft.screen);
        }
    }

    public static void receiveLosePacket(
            Minecraft minecraft, ClientPacketListener clientPacketListener, FriendlyByteBuf friendlyByteBuf,
            PacketSender packetSender
    ) {
        if (minecraft.player == null) {
            ZondayLandClient.LOGGER.warn("Coin Machine: Lose packet: Player is null");
            return;
        }
        minecraft.player.playSound(SoundEvents.ALLAY_HURT, 1.0f, 0.5f);

        if (minecraft.screen instanceof CoinMachineScreen screen) {
            screen.onGameEnd();
        } else {
            ZondayLandClient.LOGGER.warn("Coin Machine: Lose packet: invalid screen {}", minecraft.screen);
        }
    }

}
