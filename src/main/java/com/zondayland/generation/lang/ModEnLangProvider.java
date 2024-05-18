package com.zondayland.generation.lang;

import com.zondayland.registry.ModBlocks;
import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModEnLangProvider extends FabricLanguageProvider {
    public ModEnLangProvider(FabricDataOutput dataGenerator) {
        // Specifying en_us is optional, by default is is en_us.
        super(dataGenerator, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        // Items
        translationBuilder.add(ModItems.LOGO, "Logo");
        translationBuilder.add(ModItems.CLAIM_PARCHMENT, "Claim Parchment");
        translationBuilder.add(ModItems.UNCLAIM_PARCHMENT, "Unclaim Parchment");
        translationBuilder.add(ModItems.SERVER_CLAIM, "Server Claim");
        translationBuilder.add(ModItems.SERVER_UNCLAIM, "Server Unclaim");
        translationBuilder.add(ModItems.MICROPHONE, "Microphone");
        translationBuilder.add(ModItems.MIC_HOLDER, "Microphone holder");
    }
}