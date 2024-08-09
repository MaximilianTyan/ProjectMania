package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.ModMain;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

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
                                                                 .title(Component.literal("Venezia"))
                                                                 .icon(() -> new ItemStack(ModItems.LOGO))
                                                                 .displayItems((params, output) -> Arrays.stream(ITEMS)
                                                                                                         .forEach(output::accept))
                                                                 .build();

    public static void register() {
        ModMain.LOGGER.info("Registering creative tab");

        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, new ResourceLocation(ModMain.MOD_ID, "group"), MOD_TAB);
    }
}
