package io.github.maximiliantyan.projectmania.screens;

import io.github.maximiliantyan.projectmania.ModClient;
import io.github.maximiliantyan.projectmania.ModMain;
import io.github.maximiliantyan.projectmania.gui.CoinMachineMenu;
import io.github.maximiliantyan.projectmania.gui.GUIConstants;
import io.github.maximiliantyan.projectmania.network.PacketsIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class CoinMachineScreen extends AbstractContainerScreen<CoinMachineMenu> {
    private static final ResourceLocation BACKGROUND =
            new ResourceLocation(ModMain.MOD_ID, "textures/gui/menus/coin_machine_gui.png");

    private Button playButton;

    public CoinMachineScreen(CoinMachineMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
        playButton = Button.builder(Component.literal("Jouer"), this::onPlayButtonPressed)
                           .pos(this.leftPos + 50, this.topPos + 32)
                           .size(GUIConstants.SLOT_SIZE * 2, GUIConstants.SLOT_SIZE)
                           .build();
        addRenderableWidget(playButton);
    }

    public void onPlayButtonPressed(Button button) {
        ModClient.LOGGER.info("Coin Machine: Play button pressed");
        ClientPlayNetworking.send(PacketsIdentifier.C2S.COIN_MACHINE_PLAY, PacketByteBufs.empty());
    }

    public void onGameStart() {
        ModClient.LOGGER.info("Coin Machine: Game started");
        playButton.active = false;
    }

    public void onGameEnd() {
        ModClient.LOGGER.info("Coin Machine: Game ended");
        playButton.active = true;
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
