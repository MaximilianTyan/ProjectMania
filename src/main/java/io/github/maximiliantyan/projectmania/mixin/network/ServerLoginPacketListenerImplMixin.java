package io.github.maximiliantyan.projectmania.mixin.network;

import io.github.maximiliantyan.projectmania.ModConfig;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerLoginPacketListenerImpl.class)
public class ServerLoginPacketListenerImplMixin {
    @ModifyConstant(
            method = "tick()V",
            constant = @Constant(intValue = 600),
            require = 1
    )
    private int tick(int constant) {
        return ModConfig.loginTimeoutSeconds * 20;
    }
}
