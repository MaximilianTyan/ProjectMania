package com.zondayland.network;

import com.zondayland.ZondayLand;
import com.zondayland.gui.CoinMachineMenu;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;

public class CoinMachinePacketHandler {

    public static void receive(
            MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf,
            PacketSender responseSender
    ) {
        server.execute(() -> action(server, player, handler, buf, responseSender));
    }

    private static void action(
            MinecraftServer server, ServerPlayer player, ServerGamePacketListenerImpl handler, FriendlyByteBuf buf,
            PacketSender responseSender
    ) {
        if (player.containerMenu instanceof CoinMachineMenu menu) {
            menu.startGame();
            return;
        }
        ZondayLand.LOGGER.warn(
                "Received Coin Machine Play packet from {} but Coin Machine menu not opened {} instead",
                player.getName(),
                player.containerMenu
        );
    }
}
