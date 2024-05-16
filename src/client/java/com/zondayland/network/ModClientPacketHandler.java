package com.zondayland.network;

import com.zondayland.ZondayLandClient;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModClientPacketHandler {
    public static void register()
    {
        ZondayLandClient.LOGGER.info("Registering client networking recievers");
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.CLAIM_SUCCESS, ClaimSuccessPacketHandler::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketIdentifiers.UNCLAIM_SUCCESS, UnclaimSuccessPacketHandler::receive);
    }
}
