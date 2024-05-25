package com.zondayland.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

import static com.zondayland.ZondayLand.LOGGER;
import static com.zondayland.ZondayLand.MOD_ID;

public class ModTabs {

    public static final Item[] ITEMS = {
            ModItems.LOGO,
            ModItems.CLAIM_PARCHMENT,
            ModItems.UNCLAIM_PARCHMENT,
            ModItems.SERVER_CLAIM,
            ModItems.SERVER_UNCLAIM,
            ModItems.MICROPHONE,
            ModItems.MIC_HOLDER
    };
    public static final CreativeModeTab MOD_TAB = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
                                                                 .title(Component.literal("Zondayland"))
                                                                 .icon(() -> new ItemStack(ModItems.LOGO))
                                                                 .displayItems((params, output) -> Arrays.stream(ITEMS)
                                                                                                         .forEach(output::accept))
                                                                 .build();

    public static void register() {
        LOGGER.info("Registering tab");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(MOD_ID, "group"), MOD_TAB);
    }
}
