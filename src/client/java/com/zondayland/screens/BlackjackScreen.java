package com.zondayland.screens;

import com.zondayland.ZondayLand;
import com.zondayland.ZondayLandClient;
import com.zondayland.gui.BlackjackMenu;
import com.zondayland.gui.CoinMachineMenu;
import com.zondayland.gui.GUIConstants;
import com.zondayland.network.PacketsIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BlackjackScreen extends AbstractContainerScreen<BlackjackMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(ZondayLand.MOD_ID, "textures/gui/menus/blackjack_gui.png");

    public BlackjackScreen(BlackjackMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        Button PlayButton = Button.builder(Component.literal("Jouer"), this::onPlayButtonPressed)
                                  .pos(this.leftPos + 50, this.topPos + 32)
                                  .size(GUIConstants.SLOT_SIZE * 2, GUIConstants.SLOT_SIZE)
                                  .build();

        addRenderableWidget(PlayButton);
    }

    public void onPlayButtonPressed(Button button) {
        ZondayLandClient.LOGGER.info("Coin Machine: Play button pressed");
        ClientPlayNetworking.send(PacketsIdentifier.BLACKJACK_PLAY, PacketByteBufs.empty());
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
