package io.github.maximiliantyan.projectmania.mixin.client.mods.bumblezone;

import com.mojang.blaze3d.vertex.PoseStack;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenModel;
import com.telepathicgrunt.the_bumblezone.client.rendering.beequeen.BeeQueenRenderer;
import com.telepathicgrunt.the_bumblezone.entities.mobs.BeeQueenEntity;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(BeeQueenRenderer.class)
public abstract class BeeQueenRendererMixin extends MobRenderer<BeeQueenEntity, BeeQueenModel> {

    @Final
    @Shadow
    private ItemInHandRenderer itemRenderer;

    public BeeQueenRendererMixin(
            EntityRendererProvider.Context context, BeeQueenModel model,
            float shadowRadius
    ) {
        super(context, model, shadowRadius);
    }

//    /**
//     * Fix for cast crash, adding sodium exception
//     * See original function {@link BeeQueenRenderer#render}
//     * <pre>
//     * {@code
//     * MultiBufferSource bufferToUse = buffer;
//     * if (!ModChecker.rubidiumPresent) {
//     *      bufferToUse = new TranslucentItemRenderTypeBuffer(buffer, alpha);
//     * }
//     * </pre>
//     *
//     * @return
//     * @return
//     */
//    @ModifyExpressionValue(
//            method = "render(Lcom/telepathicgrunt/the_bumblezone/entities/mobs/BeeQueenEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
//            at = @At(value = "FIELD", target = "Lcom/telepathicgrunt/the_bumblezone/modcompat/ModChecker;rubidiumPresent:Z"))
//    private boolean hasRubidiumOrSodiumPresent(boolean rubidiumPresent) {
//        return rubidiumPresent || FabricLoader.getInstance().isModLoaded("sodium");
//    }

    /**
     * Fix for cast crash, adding sodium exception
     * See original function {@link BeeQueenRenderer#render}
     * <pre>
     * {@code
     * MultiBufferSource bufferToUse = buffer; <<<<<<<<<<< We want to keep this
     *   if (!ModChecker.rubidiumPresent) {
     *       bufferToUse = new TranslucentItemRenderTypeBuffer(buffer, alpha); <<<<<< We want to cancel this
     *   }
     *   this.itemRenderer.renderItem(beeQueenEntity, beeQueenEntity.getBonusTradeItem(), ItemDisplayContext.GROUND, false, stack, (MultiBufferSource)bufferToUse, packedLight);
     *   stack.popPose();
     * }
     * </pre>
     */
    @Inject(
            method = "render(Lcom/telepathicgrunt/the_bumblezone/entities/mobs/BeeQueenEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/telepathicgrunt/the_bumblezone/client/rendering/transparentitem/TranslucentItemRenderTypeBuffer;<init>(Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
            ),
            cancellable = true,
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void inject$bufferSource(
            BeeQueenEntity beeQueenEntity, float entityYaw, float partialTicks, PoseStack stack,
            MultiBufferSource buffer, int packedLight, CallbackInfo ci, float scale, float rotYaw, int alpha,
            MultiBufferSource bufferToUse
    ) {
        this.itemRenderer.renderItem(
                beeQueenEntity,
                beeQueenEntity.getBonusTradeItem(),
                ItemDisplayContext.GROUND,
                false,
                stack,
                (MultiBufferSource) bufferToUse,
                packedLight
        );
        stack.popPose();
        ci.cancel();
    }
}
