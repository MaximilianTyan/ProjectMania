package io.github.maximiliantyan.projectmania.screens;

import io.github.maximiliantyan.projectmania.ModClient;
import io.github.maximiliantyan.projectmania.ModMain;
import io.github.maximiliantyan.projectmania.gui.BlackjackMenu;
import io.github.maximiliantyan.projectmania.gui.GUIConstants;
import io.github.maximiliantyan.projectmania.network.PacketsIdentifier;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.CartographyTableScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.CartographyTableMenu;
import org.jetbrains.annotations.NotNull;

public class BlackjackScreen extends AbstractContainerScreen<BlackjackMenu> {
    private static final ResourceLocation BACKGROUND = new ResourceLocation(ModMain.MOD_ID, "textures/gui/menus/blackjack_gui.png");

    private Button PlayButton;
    private Button ContinueButton;
    private Button RetractButton;

    public BlackjackScreen(BlackjackMenu handler, Inventory inventory, Component title) {
        super(handler, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        /** {@link CartographyTableScreen} */
        /** {@link CartographyTableMenu} */

        PlayButton = Button.builder(Component.literal("Jouer"), this::onPlayButtonPressed)
                                                .pos(this.leftPos + 50, this.topPos + 32)
                                                .size(GUIConstants.SLOT_SIZE * 2, GUIConstants.SLOT_SIZE)
                                                .build();
        ContinueButton = Button.builder(Component.literal("Miser"), this::onContinueButtonPressed)
                                                    .pos(this.leftPos + 42, this.topPos + 63)
                                                    .size(GUIConstants.SLOT_SIZE * 2, GUIConstants.SLOT_SIZE)

                                                    .build();
        RetractButton = Button.builder(Component.literal("Se retirer"), this::onRetractButtonPressed)
                                                   .pos(this.leftPos + 96, this.topPos + 63)
                                                   .size(GUIConstants.SLOT_SIZE * 2, GUIConstants.SLOT_SIZE)
                                                   .build();

        ContinueButton.active = false;
        RetractButton.active = false;

        addRenderableWidget(PlayButton);
        addRenderableWidget(ContinueButton);
        addRenderableWidget(RetractButton);
    }

    public void onPlayButtonPressed(Button button) {
        ModClient.LOGGER.info("Blackjack: Play button pressed");
        ClientPlayNetworking.send(PacketsIdentifier.C2S.BLACKJACK_PLAY, PacketByteBufs.empty());
    }

    public void onContinueButtonPressed(Button button) {
        ModClient.LOGGER.info("Blackjack: Continue button pressed");
        ClientPlayNetworking.send(PacketsIdentifier.C2S.BLACKJACK_CONTINUE, PacketByteBufs.empty());
    }

    public void onRetractButtonPressed(Button button) {
        ModClient.LOGGER.info("Blackjack: Retract button pressed");
        ClientPlayNetworking.send(PacketsIdentifier.C2S.BLACKJACK_RETRACT, PacketByteBufs.empty());
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        this.renderBackground(guiGraphics);
        guiGraphics.blit(BACKGROUND, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
    }
}
