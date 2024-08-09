package io.github.maximiliantyan.projectmania.mixin.mods.create;

import io.github.maximiliantyan.projectmania.mixin.ModConditionnalMixinPlugin;

public class CreateConditionnalMixinPlugin extends ModConditionnalMixinPlugin {
    @Override
    public String getTargetModId() {
        return "create";
    }

    @Override
    public String getTargetModName() {
        return "Create";
    }

    @Override
    public boolean shouldApplyMixinsWhithMod() {
        return true;
    }
}