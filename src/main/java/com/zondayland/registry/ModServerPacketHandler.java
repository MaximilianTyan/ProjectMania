package com.zondayland.registry;

import com.zondayland.ZondayLand;
import com.zondayland.network.CoinMachinePacketHandler;
import com.zondayland.network.PacketsIdentifier;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class ModServerPacketHandler {
    public static void register() {
        ZondayLand.LOGGER.info("Registering server networking receivers");
        ServerPlayNetworking.registerGlobalReceiver(PacketsIdentifier.C2S.COIN_MACHINE_PLAY, CoinMachinePacketHandler::receive);
    }
}
