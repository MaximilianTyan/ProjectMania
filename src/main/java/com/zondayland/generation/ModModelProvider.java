package com.zondayland.generation;

import com.zondayland.registry.ModBlocks;
import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.LOGO, Models.GENERATED);
        itemModelGenerator.register(ModItems.CLAIM_PARCHMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.UNCLAIM_PARCHMENT, Models.GENERATED);
        itemModelGenerator.register(ModItems.SERVER_CLAIM, Models.GENERATED);
        itemModelGenerator.register(ModItems.SERVER_UNCLAIM, Models.GENERATED);
    }
}
