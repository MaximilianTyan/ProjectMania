package com.zondayland.generation.lang;

import com.zondayland.registry.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class ModFrLangProvider extends FabricLanguageProvider {
    public ModFrLangProvider(FabricDataOutput dataGenerator) {
        // Specifying en_us is optional, by default is is en_us.
        super(dataGenerator, "fr");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        translationBuilder.add(ModItems.LOGO, "Logo");
        translationBuilder.add(ModItems.CLAIM_PARCHMENT, "Parchemin de Claim");
        translationBuilder.add(ModItems.UNCLAIM_PARCHMENT, "Parchemin de Déclaim");
        translationBuilder.add(ModItems.SERVER_CLAIM, "Claim Serveur");
        translationBuilder.add(ModItems.SERVER_UNCLAIM, "Déclaim Serveur");
    }
}
