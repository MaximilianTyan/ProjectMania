package com.zondayland.registry;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

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
    public static final ItemGroup MOD_TAB = ItemGroup.create(ItemGroup.Row.TOP, 0)
                                                     .displayName(Text.of("Zondayland"))
                                                     .icon(() -> new ItemStack(ModItems.LOGO))
                                                     .entries(ModTabs::registerEntries)
                                                     .build();

    private static void registerEntries(ItemGroup.DisplayContext displayContext, ItemGroup.Entries entries) {
        Arrays.stream(ITEMS).forEach(entries::add);
    }

    public static void register() {
        LOGGER.info("Registering tab");

        Registry.register(Registries.ITEM_GROUP, Identifier.of(MOD_ID, "group"), MOD_TAB);
    }
}
