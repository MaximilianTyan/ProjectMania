package io.github.maximiliantyan.projectmania.mixin.mods.create;

import com.simibubi.create.content.kinetics.deployer.DeployerHandler;
import net.minecraft.world.entity.item.ItemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.List;

@Mixin(DeployerHandler.class)
public class CreateDeployerHandlerMixin {

    /**
     * Fix for null pointer exception
     * See original function {@link DeployerHandler#activateInner}
     * <pre>
     * {@code
     * List<ItemEntity> capturedDrops = entity.finishCapturingDrops();
     * capturedDrops.forEach(e -> player.getInventory()
     *  .placeItemBackInInventory(e.getItem()));
     * }
     * </pre>
     */
    @ModifyVariable(
            method = "activateInner",
            at = @At("STORE"),
            name = "capturedDrops"
    )
    private static List<ItemEntity> capturedDropsInject(List<ItemEntity> capturedDrops) {
        if (capturedDrops == null) {
            // ModMain.LOGGER.info("Overriding null value of 'capturedDrops' with empty List<ItemEntity>");
            capturedDrops = List.of();
        }
        return capturedDrops;
    }
}
