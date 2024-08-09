package io.github.maximiliantyan.projectmania;

import eu.midnightdust.lib.config.MidnightConfig;
import io.github.maximiliantyan.projectmania.registry.*;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModMain implements ModInitializer {

    public static final String MOD_ID = "projectmania";
    public static final Logger LOGGER = LoggerFactory.getLogger("Projectmania");

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing mod {}", MOD_ID);

        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.
        MidnightConfig.init(MOD_ID, ModConfig.class);
        ModServerPacketHandler.register();
        ModScreenHandlersType.register();
        ModBlocks.register();
        ModItems.register();
        ModTabs.register();
        ModCommands.register();

    }
}