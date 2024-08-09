package io.github.maximiliantyan.projectmania.registry;

import io.github.maximiliantyan.projectmania.blocks.MicHolderBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import static io.github.maximiliantyan.projectmania.ModMain.LOGGER;
import static io.github.maximiliantyan.projectmania.ModMain.MOD_ID;

public class ModBlocks {
    public static final Block MIC_HOLDER = new MicHolderBlock(FabricBlockSettings.create());

    public static void register() {
        LOGGER.info("Registering blocks");
        Registry.register(BuiltInRegistries.BLOCK, new ResourceLocation(MOD_ID, "mic_holder"), MIC_HOLDER);
    }
}
