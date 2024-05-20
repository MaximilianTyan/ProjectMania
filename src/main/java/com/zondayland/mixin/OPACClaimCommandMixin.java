package com.zondayland.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zondayland.ZondayLand;
import com.zondayland.commands.PermissionLevels;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xaero.pac.common.server.claims.command.ClaimsClaimCommand;
import xaero.pac.common.server.config.ServerConfig;

@Mixin(ClaimsClaimCommand.class)
public abstract class OPACClaimCommandMixin {

    @ModifyVariable(method = "register", ordinal = 0, at = @At("STORE"))
    private LiteralArgumentBuilder<ServerCommandSource> injected(LiteralArgumentBuilder<ServerCommandSource> original) {
        ZondayLand.LOGGER.info("Adding op requirement for command '%s'".formatted(original.toString()));
        return original.requires(context -> ServerConfig.CONFIG.claimsEnabled.get() && context.hasPermissionLevel(
                PermissionLevels.GAMEMASTER));
    }
}
