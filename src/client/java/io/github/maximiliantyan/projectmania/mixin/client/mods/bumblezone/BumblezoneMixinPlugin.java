package io.github.maximiliantyan.projectmania.mixin.client.mods.bumblezone;

import io.github.maximiliantyan.projectmania.mixin.client.mods.ModClientMixinPlugin;

public class BumblezoneMixinPlugin extends ModClientMixinPlugin {
    @Override
    public String getTargetModId() {
        return "the_bumblezone";
    }

    @Override
    public String getTargetModName() {
        return "The Bumblezone";
    }

    @Override
    public boolean shouldApplyMixinsWhithMod() {
        return true;
    }
}
