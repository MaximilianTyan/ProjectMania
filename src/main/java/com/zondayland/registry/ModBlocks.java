package com.zondayland.registry;

import com.zondayland.blocks.MicHolderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

import static com.zondayland.ZondayLand.LOGGER;
import static com.zondayland.ZondayLand.MOD_ID;

public class ModBlocks {
    public static final Block MIC_HOLDER = new MicHolderBlock(FabricBlockSettings.create());

    public static void register() {
        LOGGER.info("Registering items");
        Registry.register(Registries.BLOCK, Identifier.of(MOD_ID, "mic_holder"), MIC_HOLDER);
    }
}
