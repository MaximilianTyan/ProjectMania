package com.zondayland.mixin;

import com.zondayland.ZondayLand;
import com.zondayland.utils.PlayerTicked;
import com.zondayland.utils.PlayerTicksAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerTicksAccessor {

    @Unique
    private static final List<PlayerTicked> playerTicked = new ArrayList<>();
    @Unique
    private static final List<PlayerTicked> pendingRemoval = new ArrayList<>();

    @Shadow @Final private Inventory inventory;

    @Shadow @Final public InventoryMenu inventoryMenu;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    public void zondayLand$registerTickedClass(PlayerTicked tickedClass) {
        if (zondayLand$hasTickedClass(tickedClass)) {
            ZondayLand.LOGGER.info(
                    "Player ticked class already present: {}",
                    tickedClass
            );
            return;
        }
        playerTicked.add(tickedClass);
    }

    @Unique
    public void zondayLand$removeTickedClass(PlayerTicked tickedClass) {
        if (zondayLand$hasTickedClass(tickedClass)) {
            pendingRemoval.add(tickedClass);
        }
    }

    @Unique
    public boolean zondayLand$hasTickedClass(PlayerTicked tickedClass) {
        return playerTicked.contains(tickedClass) && !pendingRemoval.contains(tickedClass);
    }

    @Inject(
            method = "tick()V", at = @At(value = "TAIL")
    )
    private void tickClasses(CallbackInfo ci) {
        if (this.level().isClientSide()) return;
        if (playerTicked.isEmpty()) return;

        if (!pendingRemoval.isEmpty()) {
            for (PlayerTicked tickedClass : pendingRemoval) {
                playerTicked.remove(tickedClass);
            }
            pendingRemoval.clear();
        }

        for (PlayerTicked tickedClass : playerTicked) {
            tickedClass.tick(this.level(), this.inventory.player);
        }
    }
}
