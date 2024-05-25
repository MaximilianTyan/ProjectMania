package com.zondayland.gui;

import com.zondayland.registry.ModScreenHandlersType;
import net.minecraft.network.chat.Component;
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

import static com.zondayland.gui.GUIConstants.*;

public class CoinMachineScreenHandler extends AbstractContainerMenu {
    private static final String SCREEN_NAME = "Coin Machine";
    private final Container screenContainer = new SimpleContainer(2);
    private final ContainerLevelAccess access;
    private final Slot coinInputSlot;
    private final Slot rewardOutputSlot;

    public static CoinMachineScreenHandler create(int containerId, Inventory playerInventory) {
        return new CoinMachineScreenHandler(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CoinMachineScreenHandler(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(ModScreenHandlersType.COIN_MACHINE, containerId);

        this.access = access;

        //some inventories do custom logic when a player opens it.
        playerInventory.startOpen(playerInventory.player);

        // Screen local inventory
        int slotIdx = 0;
        coinInputSlot = new Slot(screenContainer, slotIdx++, 0, 0);
        rewardOutputSlot = new Slot(screenContainer, slotIdx++, 0, SLOT_SIZE + INTER_SLOT_GAP);

        //The player inventory
        int playerSlotIdx = 0;
        final int playerInvStartX = 8;
        final int playerInvStartY = 84;
        for (int rows = 0; rows < 4; rows++) {
            for (int cols = 0; cols < 9; cols++) {
                int slotX = playerInvStartX + cols * (SLOT_SIZE + INTER_SLOT_GAP);
                int slotY = playerInvStartY + rows * (SLOT_SIZE + INTER_SLOT_GAP);
                if (rows == 3) slotY += INV_HOTBAR_GAP;
                this.addSlot(new Slot(playerInventory, playerSlotIdx++, slotX, slotY));
            }
        }
    }

    public static MenuProvider getMenuProvider() {
        return new SimpleMenuProvider(
                (containerId, playerInventory, player) -> new CoinMachineScreenHandler(
                        containerId, playerInventory, ContainerLevelAccess.NULL
                ),
                Component.literal(SCREEN_NAME)
        );
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     *
     * @param player
     * @param index
     */
    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack movedStack = player.getInventory().getItem(index);
        return coinInputSlot.safeInsert(movedStack);
    }

    /**
     * Determines whether supplied player can use this container
     *
     * @param player
     */
    @Override
    public boolean stillValid(Player player) {
        return true;
    }


}
