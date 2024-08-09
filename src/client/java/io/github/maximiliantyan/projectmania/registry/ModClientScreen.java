package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.ModClient;
import io.github.maximiliantyan.projectmania.screens.BlackjackScreen;
import io.github.maximiliantyan.projectmania.screens.CoinMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModClientScreen {
    public static void register() {
        ModClient.LOGGER.info("Registering client screens");
        MenuScreens.register(ModScreenHandlersType.COIN_MACHINE, CoinMachineScreen::new);
        MenuScreens.register(ModScreenHandlersType.BLACKJACK, BlackjackScreen::new);
    }
}
