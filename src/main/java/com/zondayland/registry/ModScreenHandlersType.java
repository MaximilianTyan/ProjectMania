package com.zondayland.registry;

import com.zondayland.gui.CoinMachineScreenHandler;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.MenuType;

import static com.zondayland.ZondayLand.LOGGER;
import static com.zondayland.ZondayLand.MOD_ID;

public class ModScreenHandlersType {
    public static final MenuType<CoinMachineScreenHandler> COIN_MACHINE = new MenuType<CoinMachineScreenHandler>(CoinMachineScreenHandler::create, FeatureFlagSet.of());
    public static void register()
    {
        LOGGER.info("Registering screen handlers");
        //register(@NotNull  Registry<V> registry, ResourceLocation name, T value )
        Registry.register(BuiltInRegistries.MENU, new ResourceLocation(MOD_ID, "coin_machine"), COIN_MACHINE);
    }
}
