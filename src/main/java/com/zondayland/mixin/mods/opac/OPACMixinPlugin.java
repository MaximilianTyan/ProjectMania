package com.zondayland.mixin.mods.opac;

import com.zondayland.ZondayLand;
import com.zondayland.mixin.mods.ModMixinPlugin;
import net.fabricmc.loader.api.FabricLoader;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class OPACMixinPlugin extends ModMixinPlugin {

    @Override
    public String getModId() {
        return "openpartiesandclaims";
    }

    @Override
    public String getModName() {
        return "Open Parties and Claims";
    }
}
