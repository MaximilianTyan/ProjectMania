package io.github.maximiliantyan.projectmania.mixin.mods.opac;

import io.github.maximiliantyan.projectmania.mixin.ModConditionnalMixinPlugin;

public class OPACConditionnalMixinPlugin extends ModConditionnalMixinPlugin {
    @Override
    public String getTargetModId() {
        return "openpartiesandclaims";
    }

    @Override
    public String getTargetModName() {
        return "Open Parties and Claims";
    }

    @Override
    public boolean shouldApplyMixinsWhithMod() {
        return true;
    }
}
