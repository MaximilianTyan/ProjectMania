package io.github.maximiliantyan.projectmania;

import io.github.maximiliantyan.projectmania.generation.ModLootTableProvider;
import io.github.maximiliantyan.projectmania.generation.ModModelProvider;
import io.github.maximiliantyan.projectmania.generation.lang.ModEnLangProvider;
import io.github.maximiliantyan.projectmania.generation.lang.ModFrLangProvider;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;


public class ModDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ModEnLangProvider::new);
        pack.addProvider(ModFrLangProvider::new);
        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModLootTableProvider::new);
    }
}
