package com.zondayland;

import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zondayland.registry.ModClientPacketHandler;
import com.zondayland.registry.ModClientScreen;

import static com.zondayland.ZondayLand.MOD_ID;

public class ZondayLandClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Zondayland-Client");

    @Override
    public void onInitializeClient() {
        ModClientPacketHandler.register();
        ModClientScreen.register();
    }
}