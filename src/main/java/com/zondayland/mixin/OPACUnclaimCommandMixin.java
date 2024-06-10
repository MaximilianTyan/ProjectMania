package com.zondayland.mixin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.zondayland.ZondayLand;
import com.zondayland.commands.PermissionLevels;
import net.minecraft.commands.CommandSourceStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import xaero.pac.common.server.claims.command.ClaimsClaimCommand;
import xaero.pac.common.server.claims.command.ClaimsUnclaimCommand;
import xaero.pac.common.server.config.ServerConfig;

@Mixin(ClaimsUnclaimCommand.class)
public abstract class OPACUnclaimCommandMixin {

    /** See original call {@link ClaimsUnclaimCommand#register} */
    @ModifyVariable(method = "register", name = "command", at = @At("STORE"))
    private LiteralArgumentBuilder<CommandSourceStack> injected(LiteralArgumentBuilder<CommandSourceStack> original) {
        ZondayLand.LOGGER.info("Adding op requirement for unclaim command: '{}'", original.getLiteral());
        return original.requires(context -> ServerConfig.CONFIG.claimsEnabled.get() && context.hasPermission(
                PermissionLevels.GAMEMASTER));
    }
}
