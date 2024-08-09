package io.github.maximiliantyan.projectmania.mixin.network;

import io.github.maximiliantyan.projectmania.mixin.ModConditionnalMixinPlugin;

public class ConnectionConditionnalMixinPlugin extends ModConditionnalMixinPlugin {
    @Override
    public String getTargetModId() {
        return "connectivity";
    }

    @Override
    public String getTargetModName() {
        return "Connectivity";
    }

    @Override
    public boolean shouldApplyMixinsWhithMod() {
        return false;
    }
}
