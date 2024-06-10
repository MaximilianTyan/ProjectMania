package com.zondayland.gui;

import com.zondayland.ModConfig;
import com.zondayland.ZondayLand;
import com.zondayland.network.PacketsIdentifier;
import com.zondayland.registry.ModScreenHandlersType;
import com.zondayland.utils.PlayerTicked;
import com.zondayland.utils.PlayerTicksAccessor;
import com.zondayland.utils.RandomPick;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static com.zondayland.gui.GUIConstants.INV_HOTBAR_GAP;
import static com.zondayland.gui.GUIConstants.SLOT_SIZE;
import static java.lang.Math.min;

public class CoinMachineMenu extends AbstractContainerMenu implements PlayerTicked {
    private static final String SCREEN_NAME = "Coin Machine";
    public static final int COIN_INPUT_SLOT = 0;

    public static final int NEXT_DRAW_FIRST_SLOT = 1;
    public static final int NEXT_DRAW_LAST_SLOT = 3;
    public static final int CURRENT_DRAW_FIRST_SLOT = 4;
    public static final int CURRENT_DRAW_LAST_SLOT = 6;
    public static final int PREVIOUS_DRAW_FIRST_SLOT = 7;
    public static final int PREVIOUS_DRAW_LAST_SLOT = 9;

    public static final int REWARD_OUTPUT_SLOT = CURRENT_DRAW_FIRST_SLOT + 1;

    public static final int PLAYER_INVENTORY_FIRST_SLOT = 10;
    public static final int PLAYER_INVENTORY_LAST_SLOT = PLAYER_INVENTORY_FIRST_SLOT + 9 * 4;

    private static final int NUMBER_OF_WHEELS = 3;
    private static final int RESULT_VIEWING_TICKS = 20;
    private static final int START_PAUSE_TICKS = 20;

    private List<Wheel> wheels = new ArrayList<>(NUMBER_OF_WHEELS);
    public final Player player;
    public final Level level;
    private final Container screenContainer = new SimpleContainer(10);
    private final ContainerLevelAccess access;
    private int startDelayTicksLeft = 0;
    private int resultViewingTicksLeft = 0;
    private boolean isGameRunning = false;

    public static CoinMachineMenu create(int containerId, Inventory playerInventory) {
        return new CoinMachineMenu(containerId, playerInventory, ContainerLevelAccess.NULL);
    }

    public CoinMachineMenu(int containerId, Inventory playerInventory, final ContainerLevelAccess access) {
        super(ModScreenHandlersType.COIN_MACHINE, containerId);
        this.access = access;
        this.player = playerInventory.player;
        this.level = this.player.level();

        // Some inventories do custom logic when a player opens it.
        playerInventory.startOpen(playerInventory.player);

        // Screen inventory -------------------------------------------------------
        {
            int screenSlotIdx = 0;
            addSlot(new InputSlot(screenContainer, screenSlotIdx++, 18, 33));

            int rewardsTopLeftX = 103;
            int rewardsTopLeftY = 11;
            // Next slots, from left to right
            // @formatter:off
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 2, rewardsTopLeftY + 2, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 22, rewardsTopLeftY + 1, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 42, rewardsTopLeftY + 2, true));
            // @formatter:on

            // Current slots, from left to right
            // @formatter:off
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 1, rewardsTopLeftY + 22, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 22, rewardsTopLeftY + 22, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 43, rewardsTopLeftY + 22, true));
            // @formatter:on

            // Previous slots, from left to right
            // @formatter:off
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 2, rewardsTopLeftY + 42, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 22, rewardsTopLeftY + 43, true));
            addSlot(new LockableSlot(screenContainer, screenSlotIdx++, rewardsTopLeftX + 42, rewardsTopLeftY + 42, true));
            // @formatter:on
        }

        // Coin machine wheel logic -------------------------------------------------------
        {
            RandomPick<String> pool = new RandomPick<>(ModConfig.CoinMachineLootTable);
            this.wheels.add(new Wheel(this, player, pool)
                    .named("Left")
                    .withSlots(
                            NEXT_DRAW_FIRST_SLOT,
                            CURRENT_DRAW_FIRST_SLOT,
                            PREVIOUS_DRAW_FIRST_SLOT
                    )
            );
            this.wheels.add(new Wheel(this, player, pool)
                    .named("Middle")
                    .withSlots(
                            NEXT_DRAW_FIRST_SLOT + 1,
                            REWARD_OUTPUT_SLOT,
                            PREVIOUS_DRAW_FIRST_SLOT + 1
                    )
                    .willGiveAwardOnWin()
            );
            this.wheels.add(new Wheel(this, player, pool)
                    .named("Right")
                    .withSlots(
                            NEXT_DRAW_LAST_SLOT,
                            CURRENT_DRAW_LAST_SLOT,
                            PREVIOUS_DRAW_LAST_SLOT
                    )
            );
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
                (containerId, playerInventory, player) -> new CoinMachineMenu(
                        containerId,
                        playerInventory,
                        ContainerLevelAccess.NULL
                ),
                Component.literal(SCREEN_NAME)
        );
    }

    public int getMovingWheelsNumber() {
        return (int) this.wheels.stream().filter(Wheel::isRolling).count();
    }

    public boolean isWinningRoll() {
        Item wheelResult = this.wheels.get(0).getCurrentItem().getItem();
        return this.wheels.stream().allMatch(wheel -> wheel.getCurrentItem().is(wheelResult));
    }

    public boolean mayStartGame() {
        Slot coinSlot = this.getSlot(COIN_INPUT_SLOT);
        if (!coinSlot.hasItem()) {
            ZondayLand.LOGGER.info("Coin Machine: No coin");
            return false;
        }
        if (isGameRunning) {
            ZondayLand.LOGGER.info("Coin Machine: Last game not over");
            return false;
        }
        return true;
    }

    public void startGame() {
        if (!mayStartGame()) return;

        ZondayLand.LOGGER.info("Coin Machine: Starting game");

        Slot coinSlot = this.getSlot(COIN_INPUT_SLOT);
        coinSlot.getItem().shrink(1);

        startDelayTicksLeft = START_PAUSE_TICKS;
        resultViewingTicksLeft = RESULT_VIEWING_TICKS;
        this.wheels.forEach(Wheel::startRoll);

        broadcastChanges();
        ((PlayerTicksAccessor) this.player).zondayLand$registerTickedClass(this);

        isGameRunning = true;
    }

    @Override
    public void tick(Level level, Player player) {
        if (!isGameRunning) return;

        this.wheels.forEach(Wheel::tick);

        if (startDelayTicksLeft != 0) {
            startDelayTicksLeft--;
            return;
        } else {
            Wheel firstWheel = this.wheels.get(0);
            if (!firstWheel.isBreaking()) {
                firstWheel.startBreaking();
            }
        }

        if (resultViewingTicksLeft != 0) {
            if (getMovingWheelsNumber() == 0) {
                resultViewingTicksLeft--;
                return;
            }
        } else {
            stopGame();
        }

        // Chains wheels stopping
        for (int i = 0; i < this.wheels.size() - 1; i++) {
            Wheel wheel = this.wheels.get(i);
            if (wheel.isRolling()) break;

            Wheel nextWheel = this.wheels.get(i + 1);
            if (!nextWheel.isBreaking()) {
                nextWheel.startBreaking();
            }
        }
    }

    public void stopGame() {
        isGameRunning = false;
        ZondayLand.LOGGER.info("Coin Machine: Stopping game");

        ((PlayerTicksAccessor) this.player).zondayLand$removeTickedClass(this);
        startDelayTicksLeft = 0;

        boolean winningRoll = isWinningRoll();
        this.wheels.forEach(wheel -> wheel.onResult(winningRoll));
        broadcastChanges();

        ServerPlayNetworking.send(
                (ServerPlayer) this.player,
                winningRoll ? PacketsIdentifier.COIN_MACHINE_WIN : PacketsIdentifier.COIN_MACHINE_LOSE,
                PacketByteBufs.empty()
        );
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player inventory and the other inventory(s).
     */
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
        if (quickMovedSlotIndex == REWARD_OUTPUT_SLOT || quickMovedSlotIndex == COIN_INPUT_SLOT) {

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
            boolean success = this.moveItemStackTo(rawStack, COIN_INPUT_SLOT, REWARD_OUTPUT_SLOT, false);
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

    /**
     * Determines whether supplied player can use this container
     */
    @Override
    public boolean stillValid(@NotNull Player player) {
        return true;
    }

    public static class Wheel {
        private static final int BREAKING_TICKS = 3 * 20;
        private static final int MAX_TICKS_PER_STEP = 5;

        private boolean isRolling = false;
        private boolean isBreaking = false;
        private boolean giveAwardOnWin = false;
        private int breakTicksLeft = 0;
        private int ticksPerStep = 1;
        private String name;

        private final AbstractContainerMenu menu;
        private final Player player;
        private final RandomPick<String> pool;
        private int nextSlotId;
        private int currentSlotId;
        private int previousSlotId;

        Wheel(AbstractContainerMenu menu, Player player, RandomPick<String> pool) {
            this.menu = menu;
            this.player = player;
            this.pool = pool;
        }

        public Wheel named(String name) {
            this.name = name;
            return this;
        }

        public Wheel withSlots(int nextSlotId, int currentSlotId, int previousSlotId) {
            this.nextSlotId = nextSlotId;
            this.currentSlotId = currentSlotId;
            this.previousSlotId = previousSlotId;
            return this;
        }

        public Wheel willGiveAwardOnWin() {
            this.giveAwardOnWin = true;
            return this;
        }

        public ItemStack getCurrentItem() {
            return this.menu.getSlot(currentSlotId).getItem().copy();
        }

        public Item getNextItem() {
            return BuiltInRegistries.ITEM.get(new ResourceLocation(pool.getNext()));
        }

        public boolean isRolling() {
            return isRolling;
        }

        public boolean isBreaking() {
            return isBreaking;
        }

        public void startRoll() {
            ZondayLand.LOGGER.info("Starting {} wheel", name);
            for (int slotId : new int[]{previousSlotId, currentSlotId, nextSlotId}) {
                LockableSlot slot = (LockableSlot) menu.getSlot(slotId);
                slot.setLocked(true);
                slot.set(new ItemStack(getNextItem()));
            }

            breakTicksLeft = 0;
            ticksPerStep = 1;
            isBreaking = false;
            isRolling = true;
        }

        public void startBreaking() {
            ZondayLand.LOGGER.info("Coin Machine: Breaking {} wheel", name);

            breakTicksLeft = BREAKING_TICKS;
            ticksPerStep = 1;
            isBreaking = true;
        }

        public void tick() {
            if (!isRolling) return;

            if (breakTicksLeft % ticksPerStep == 0) step();

            if (isBreaking) {
                if (breakTicksLeft == 0) {
                    stopRoll();
                } else {
                    breakTicksLeft--;
                    ticksPerStep = min(ticksPerStep + 1, MAX_TICKS_PER_STEP);
                }
            }
        }

        public void step() {
            Slot prevSlot = menu.getSlot(previousSlotId);
            Slot currentSlot = menu.getSlot(currentSlotId);
            Slot nextSlot = menu.getSlot(nextSlotId);

            ItemStack newItem = new ItemStack(getNextItem());

            prevSlot.set(currentSlot.getItem().copy());
            currentSlot.set(nextSlot.getItem().copy());
            nextSlot.set(newItem.copy());

            menu.broadcastChanges();

            ServerPlayNetworking.send(
                    (ServerPlayer) this.player,
                    PacketsIdentifier.COIN_MACHINE_WHEEL_STEP,
                    PacketByteBufs.empty()
            );
        }

        private void stopRoll() {
            isRolling = false;
            isBreaking = false;
            breakTicksLeft = 0;
            ticksPerStep = 1;

            ZondayLand.LOGGER.info("Coin Machine: Stopping {} wheel", name);

            Slot prevSlot = menu.getSlot(previousSlotId);
            Slot nextSlot = menu.getSlot(nextSlotId);

            prevSlot.set(ItemStack.EMPTY);
            nextSlot.set(ItemStack.EMPTY);

            ServerPlayNetworking.send(
                    (ServerPlayer) this.player,
                    PacketsIdentifier.COIN_MACHINE_WHEEL_STOP,
                    PacketByteBufs.empty()
            );
        }

        public void onResult(boolean isWin) {
            ZondayLand.LOGGER.info("Coin Machine: Getting result for {} wheel", name);

            LockableSlot currentSlot = (LockableSlot) menu.getSlot(currentSlotId);

            if (isWin && giveAwardOnWin) {
                currentSlot.setLocked(false);
            } else {
                currentSlot.set(ItemStack.EMPTY);
            }
        }
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

    @Override
    public void removed(@NotNull Player player) {
        ((PlayerTicksAccessor) this.player).zondayLand$removeTickedClass(this);

        Slot coinSlot = this.getSlot(COIN_INPUT_SLOT);
        if (coinSlot.hasItem()) {
            ZondayLand.LOGGER.info("Coin Machine: Dropping coin slot: {}", coinSlot.getItem());
            this.player.drop(coinSlot.getItem().copy(), false);
        }

        Slot rewardSlot = this.getSlot(REWARD_OUTPUT_SLOT);
        if (rewardSlot.hasItem() && this.isGameRunning) {
            ZondayLand.LOGGER.info("Coin Machine: Dropping reward slot: {}", rewardSlot.getItem());
            this.player.drop(rewardSlot.getItem().copy(), false);
        }

        super.removed(player);
    }
}
