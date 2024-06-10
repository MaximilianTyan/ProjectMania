package com.zondayland.gui;


import com.zondayland.ModConfig;
import com.zondayland.ZondayLand;
import com.zondayland.registry.ModScreenHandlersType;
import com.zondayland.utils.PlayerTicked;
import com.zondayland.utils.PlayerTicksAccessor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import static com.zondayland.gui.GUIConstants.INV_HOTBAR_GAP;
import static com.zondayland.gui.GUIConstants.SLOT_SIZE;

public class BlackjackMenu extends AbstractContainerMenu implements PlayerTicked {
    private static final String SCREEN_NAME = "Blackjack";
    public static final int COIN_INPUT_SLOT = 0;

    public static final int PLAYER_INVENTORY_FIRST_SLOT = 10;
    public static final int PLAYER_INVENTORY_LAST_SLOT = PLAYER_INVENTORY_FIRST_SLOT + 9 * 4;

    public final Player player;
    public final Level level;
    private final Container screenContainer = new SimpleContainer(3);
    private final ContainerLevelAccess access;

    public static BlackjackMenu create(int containerId, Inventory playerInventory) {
        return new BlackjackMenu(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public BlackjackMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(ModScreenHandlersType.BLACKJACK, containerId);

        this.access = access;
        this.player = playerInventory.player;
        this.level = this.player.level();

        // Screen inventory -------------------------------------------------------
        {
            int screenSlotIdx = 0;
            addSlot(new InputSlot(screenContainer, screenSlotIdx++, 18, 33));

        }

        // Player inventory -------------------------------------------------------
        {
            final int playerInvTopLeftX = 8;
            final int playerInvTopLeftY = 84;

            int playerSlotIdx = 0;
            final int playerInvStartY = playerInvTopLeftY + 3 * SLOT_SIZE + INV_HOTBAR_GAP;
            for (int rows = 0; rows < 4; rows++) {
                for (int cols = 0; cols < 9; cols++) {
                    int slotX = playerInvTopLeftX + cols * (SLOT_SIZE);
                    int slotY = playerInvStartY - rows * (SLOT_SIZE);
                    if (rows > 0) slotY -= INV_HOTBAR_GAP;
                    Slot slot = this.addSlot(new Slot(playerInventory, playerSlotIdx++, slotX, slotY));
                }
            }
        }

    }

    public static MenuProvider getMenuProvider() {
        return new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new BlackjackMenu(
                        containerId,
                        playerInventory,
                        ContainerLevelAccess.NULL
                ),
                Component.literal(SCREEN_NAME)
        );
    }

    @Override
    public void tick(Level level, Player player) {

    }

    @Override
    public @NotNull ItemStack quickMoveStack(Player player, int quickMovedSlotIndex) {
        ItemStack quickMoveFailed = ItemStack.EMPTY;

        Slot quickMovedSlot = this.slots.get(quickMovedSlotIndex);
        if (quickMovedSlot == null || !quickMovedSlot.hasItem()) {
            return quickMoveFailed;
        }

        ItemStack rawStack = quickMovedSlot.getItem();
        // Create a copy to avoid a miriad of state/concurrency problems
        ItemStack quickMovedStack = rawStack.copy();

        // From screen slots
        if (quickMovedSlotIndex == COIN_INPUT_SLOT) {

            boolean success =
                    this.moveItemStackTo(rawStack, PLAYER_INVENTORY_FIRST_SLOT, PLAYER_INVENTORY_LAST_SLOT, false);
            if (!success) {
                return quickMoveFailed;
            }

            // Perform logic on result slot quick move
            quickMovedSlot.onQuickCraft(rawStack, quickMovedStack);
        }

        // From player inventory / hotbar
        else if (PLAYER_INVENTORY_FIRST_SLOT <= quickMovedSlotIndex && quickMovedSlotIndex <= PLAYER_INVENTORY_LAST_SLOT) {
            boolean success = this.moveItemStackTo(rawStack, COIN_INPUT_SLOT, COIN_INPUT_SLOT, false);
            if (!success) {
                return quickMoveFailed;
            }
        }

        // Perform post move checks
        if (rawStack.isEmpty()) {
            quickMovedSlot.set(ItemStack.EMPTY);
        }

        quickMovedSlot.setChanged();

        if (rawStack.getCount() == quickMovedStack.getCount()) {
            // If the raw stack was not able to be moved to another slot, no longer quick move
            ZondayLand.LOGGER.info("Rawstack and Quickmovedstack identical, error");
            return quickMoveFailed;
        }
        // Execute logic on what to do post move with the remaining stack
        quickMovedSlot.onTake(player, rawStack);

        // Return the slot stack
        return quickMovedStack;
    }


    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public void removed(@NotNull Player player) {
        ((PlayerTicksAccessor) this.player).zondayLand$removeTickedClass(this);

        Slot coinSlot = this.getSlot(COIN_INPUT_SLOT);
        if (coinSlot.hasItem()) {
            ZondayLand.LOGGER.info("Coin Machine: Dropping coin slot: {}", coinSlot.getItem());
            this.player.drop(coinSlot.getItem().copy(), false);
        }

        super.removed(player);
    }

    static class InputSlot extends Slot {
        public InputSlot(Container container, int slot, int x, int y) {
            super(container, slot, x, y);
        }

        @Override
        public boolean mayPlace(@NotNull ItemStack stack) {
            return ModConfig.ValidCoins.stream()
                                       .map(s -> BuiltInRegistries.ITEM.get(new ResourceLocation(s)))
                                       .anyMatch(item -> item == stack.getItem());
        }
    }

}
