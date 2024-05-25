package com.zondayland;

import com.zondayland.network.ClaimSuccessPacketHandler;
import com.zondayland.network.PacketIdentifiers;
import com.zondayland.network.UnclaimSuccessPacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModClientPacketHandler {
    public static void register() {
        ZondayLandClient.LOGGER.info("Registering client networking recievers");
        ClientPlayNetworking.registerGlobalReceiver(
                PacketIdentifiers.CLAIM_SUCCESS,
                ClaimSuccessPacketHandler::receive
        );
        ClientPlayNetworking.registerGlobalReceiver(
                PacketIdentifiers.UNCLAIM_SUCCESS,
                UnclaimSuccessPacketHandler::receive
        );
    }
}
