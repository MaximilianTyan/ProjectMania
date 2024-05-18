package com.zondayland;

import com.zondayland.registry.ModBlocks;
import com.zondayland.registry.ModCommands;
import com.zondayland.registry.ModItems;
import com.zondayland.registry.ModTabs;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ZondayLand implements ModInitializer {

    public static final String MOD_ID = "zondayland";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        ModBlocks.register();
        ModItems.register();
        ModTabs.register();
        ModCommands.register();

        LOGGER.info("Initializing %s".formatted(MOD_ID));
    }
}