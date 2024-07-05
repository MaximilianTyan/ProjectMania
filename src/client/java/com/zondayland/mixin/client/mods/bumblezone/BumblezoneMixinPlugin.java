package com.zondayland.mixin.client.mods.bumblezone;

import com.zondayland.mixin.client.mods.ModClientMixinPlugin;

public class BumblezoneMixinPlugin extends ModClientMixinPlugin {

    @Override
    public String getModId() {
        return "the_bumblezone";
    }

    @Override
    public String getModName() {
        return "The Bumblezone";
    }
}
