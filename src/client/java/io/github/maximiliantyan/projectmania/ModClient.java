package io.github.maximiliantyan.projectmania;

import io.github.maximiliantyan.projectmania.registry.ModClientScreen;
import net.fabricmc.api.ClientModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.github.maximiliantyan.projectmania.registry.ModClientPacketHandler;

public class ModClient implements ClientModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Projectmania-Client");

    @Override
    public void onInitializeClient() {
        ModClientPacketHandler.register();
        ModClientScreen.register();
    }
}