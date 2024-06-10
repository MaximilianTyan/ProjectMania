package com.zondayland.registry;

import com.zondayland.ZondayLandClient;
import com.zondayland.network.ClaimSuccessPacketHandler;
import com.zondayland.network.CoinMachineClientPacketHandler;
import com.zondayland.network.PacketsIdentifier;
import com.zondayland.network.UnclaimSuccessPacketHandler;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModClientPacketHandler {
    public static void register() {
        ZondayLandClient.LOGGER.info("Registering client networking recievers");
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.CLAIM_SUCCESS, ClaimSuccessPacketHandler::receive);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.UNCLAIM_SUCCESS, UnclaimSuccessPacketHandler::receive);

        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.COIN_MACHINE_WHEEL_STEP, CoinMachineClientPacketHandler::receiveWheelStepPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.COIN_MACHINE_WHEEL_STOP, CoinMachineClientPacketHandler::receiveWheelStopPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.COIN_MACHINE_WIN, CoinMachineClientPacketHandler::receiveWinPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.COIN_MACHINE_LOSE, CoinMachineClientPacketHandler::receiveLosePacket);
    }
}
