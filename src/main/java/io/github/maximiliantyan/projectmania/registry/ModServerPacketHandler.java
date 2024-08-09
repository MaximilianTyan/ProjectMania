package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.ModMain;
import io.github.maximiliantyan.projectmania.network.CoinMachinePacketHandler;
import io.github.maximiliantyan.projectmania.network.PacketsIdentifier;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModServerPacketHandler {
    public static void register() {
        ModMain.LOGGER.info("Registering server networking receivers");
        ServerPlayNetworking.registerGlobalReceiver(PacketsIdentifier.C2S.COIN_MACHINE_PLAY, CoinMachinePacketHandler::receive);
    }
}
