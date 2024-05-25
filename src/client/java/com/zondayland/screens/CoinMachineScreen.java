package com.zondayland.screens;

import com.zondayland.ZondayLandClient;
import com.zondayland.gui.CoinMachineScreenHandler;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;


public class CoinMachineScreen extends AbstractContainerScreen<CoinMachineScreenHandler> {
    private static final ResourceLocation BACKGROUND =
            new ResourceLocation("minecraft", "textures/gui/container/dispenser.png");
    public static Button RollButton =
            Button.builder(Component.literal("Test1"), button -> {ZondayLandClient.LOGGER.info("Pressed 1");})
                  .pos(100, 100)
                  .size(300, 200)
                  .tooltip(Tooltip.create(Component.literal("This is a test button")))
                  .build();

    public CoinMachineScreen(CoinMachineScreenHandler handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(RollButton);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        int i = this.leftPos;
        int j = this.topPos;
        guiGraphics.blit(BACKGROUND, i, j, 0, 0, this.imageWidth, this.imageHeight);
    }
}
