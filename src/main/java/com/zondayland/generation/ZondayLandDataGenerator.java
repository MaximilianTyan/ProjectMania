package com.zondayland.generation;

import com.zondayland.generation.lang.ModEnLangProvider;
import com.zondayland.generation.lang.ModFrLangProvider;
import com.zondayland.generation.textures.ModModelProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class ZondayLandDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnLangProvider::new);
        pack.addProvider(ModFrLangProvider::new);
        pack.addProvider(ModModelProvider::new);
    }
}
