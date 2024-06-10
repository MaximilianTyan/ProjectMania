package com.zondayland.gui;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class LockableSlot extends Slot {

    public boolean isLocked = false;

    public LockableSlot(Container inventoryIn, int containerSlot, int xPosition, int yPosition, boolean isLocked) {
        super(inventoryIn, containerSlot, xPosition, yPosition);
        this.isLocked = isLocked;
    }

    public LockableSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        this(inventoryIn, index, xPosition, yPosition, false);
    }

    @Override
    public boolean mayPickup(@NotNull Player player) {
        return !isLocked;
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return false;
    }

    public void setLocked(boolean locked) {
        isLocked = locked;
    }
}
