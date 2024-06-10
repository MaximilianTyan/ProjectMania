package com.zondayland.mixin;

import com.simibubi.create.content.kinetics.deployer.DeployerBlockEntity;
import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;
import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
import com.zondayland.ZondayLand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Desc;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xaero.pac.common.server.claims.command.ClaimsUnclaimCommand;

import java.util.List;

@Mixin(DeployerHandler.class)
public class CreateDeployerHandlerMixin {

    /** Fix for null pointer exception
     * See original function {@link DeployerHandler#activateInner}
     * <pre>
     * {@code
     * List<ItemEntity> capturedDrops = entity.finishCapturingDrops();
     * capturedDrops.forEach(e -> player.getInventory()
     *  .placeItemBackInInventory(e.getItem()));
     * }
     * </pre>
     * */

    @ModifyVariable(method = "activateInner", at = @At("STORE"), name = "capturedDrops")
    private static List<ItemEntity> capturedDropsInject(List<ItemEntity> capturedDrops)
    {
        if (capturedDrops == null)
        {
            // ZondayLand.LOGGER.info("Overriding null value of 'capturedDrops' with empty List<ItemEntity>");
            capturedDrops = List.of();
        }
        return capturedDrops;
    }
}
