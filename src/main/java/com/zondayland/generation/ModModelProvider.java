package com.zondayland.generation;

import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelTemplates;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {

    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.LOGO, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.CLAIM_PARCHMENT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.UNCLAIM_PARCHMENT, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SERVER_CLAIM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SERVER_UNCLAIM, ModelTemplates.FLAT_ITEM);
    }
}
