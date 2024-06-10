package com.zondayland.registry;

import com.zondayland.ZondayLandClient;
import com.zondayland.registry.ModScreenHandlersType;
import com.zondayland.screens.BlackjackScreen;
import com.zondayland.screens.CoinMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModClientScreen {
    public static void register() {
        ZondayLandClient.LOGGER.info("Registering client screens");
        MenuScreens.register(ModScreenHandlersType.COIN_MACHINE, CoinMachineScreen::new);
        MenuScreens.register(ModScreenHandlersType.BLACKJACK, BlackjackScreen::new);
    }
}
