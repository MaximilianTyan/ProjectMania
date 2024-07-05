package com.zondayland.mixin.network;

import com.zondayland.ModConfig;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerLoginPacketListenerImpl.class)
public class ServerLoginPacketListenerImplMixin {
    @ModifyConstant(
            method = "tick()V",
            constant = @Constant(intValue = 600)
    )
    private int tick(int constant) {
        return ModConfig.loginTimeoutSeconds * 20;
    }
}
