package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.ModClient;
import io.github.maximiliantyan.projectmania.network.ClaimPacketHandler;
import io.github.maximiliantyan.projectmania.network.CoinMachineClientPacketHandler;
import io.github.maximiliantyan.projectmania.network.PacketsIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

public class ModClientPacketHandler {
    public static void register() {
        ModClient.LOGGER.info("Registering client networking recievers");
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.CLAIM_SUCCESS, ClaimPacketHandler::receiveClaimSuccess);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.UNCLAIM_SUCCESS, ClaimPacketHandler::receiveUnclaimSuccess);

        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.COIN_MACHINE_STARTED, CoinMachineClientPacketHandler::receiveGameStartPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.COIN_MACHINE_WHEEL_STEP, CoinMachineClientPacketHandler::receiveWheelStepPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.COIN_MACHINE_WHEEL_STOP, CoinMachineClientPacketHandler::receiveWheelStopPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.COIN_MACHINE_WIN, CoinMachineClientPacketHandler::receiveWinPacket);
        ClientPlayNetworking.registerGlobalReceiver(PacketsIdentifier.S2C.COIN_MACHINE_LOSE, CoinMachineClientPacketHandler::receiveLosePacket);
    }
}
