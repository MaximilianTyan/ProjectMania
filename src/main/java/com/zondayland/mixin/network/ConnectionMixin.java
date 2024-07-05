package com.zondayland.mixin.network;

import com.zondayland.ModConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(targets = "net/minecraft/network/Connection$1")
public class ConnectionMixin {
    @ModifyArg(
            method = "initChannel(Lio/netty/channel/Channel;)V",
            at = @At(value = "INVOKE", target = "Lio/netty/handler/timeout/ReadTimeoutHandler;<init>(I)V")
    )
    private int setTimeoutDuration(int timeoutSeconds) {
        return ModConfig.readTimeoutSeconds * 20;
    }
}
