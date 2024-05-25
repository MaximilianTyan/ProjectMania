package com.zondayland;

import com.zondayland.registry.ModScreenHandlersType;
import com.zondayland.screens.CoinMachineScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModClientScreen {
    public static void register() {
        ZondayLandClient.LOGGER.info("Registering client screens");
        MenuScreens.register(ModScreenHandlersType.COIN_MACHINE, CoinMachineScreen::new);
    }
}
