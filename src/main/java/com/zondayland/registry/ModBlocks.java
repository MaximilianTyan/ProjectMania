package com.zondayland.registry;

import com.zondayland.blocks.MicHolderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import static com.zondayland.ZondayLand.LOGGER;
import static com.zondayland.ZondayLand.MOD_ID;

public class ModBlocks {
    public static final Block MIC_HOLDER = new MicHolderBlock(FabricBlockSettings.create());

    public static void register() {
        LOGGER.info("Registering items");
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "mic_holder"), MIC_HOLDER);
    }
}
