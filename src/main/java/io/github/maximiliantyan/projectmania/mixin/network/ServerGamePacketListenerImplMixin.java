package io.github.maximiliantyan.projectmania.mixin.network;

import io.github.maximiliantyan.projectmania.ModConfig;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @ModifyConstant(
            method = "tick()V",
            constant = {
                    @Constant(longValue = 15000L),
                    //CraftBukkit changes it to 25000.
                    @Constant(longValue = 25000L)
            },
            require = 1
    )
    private long setKeepAliveDurationMs(long constant) {
        return ModConfig.keepAlivePacketIntervalSeconds * 1000L;
    }
}
