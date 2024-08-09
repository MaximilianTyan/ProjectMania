package io.github.maximiliantyan.projectmania.mixin.client;

import io.github.maximiliantyan.projectmania.registry.ModItems;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> implements ArmedModel, HeadedModel {
    @Final
    @Shadow
    public ModelPart rightArm;

    @Final
    @Shadow
    public ModelPart leftArm;

    @Final
    @Shadow
    public ModelPart head;

    @Inject(method = "poseRightArm", at = @At("TAIL"))
    public void projectmania$injectPositionRightArm(T entity, CallbackInfo ci) {
        // positionRightArm(T entity)
        // case TOOT_HORN: {
        //      this.rightArm.pitch = MathHelper.clamp((float)this.head.pitch, (float)-1.2f, (float)1.2f) - 1.4835298f;
        //      this.rightArm.yaw = this.head.yaw - 0.5235988f;
        // }
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = entity.getItemInHand(InteractionHand.OFF_HAND);
        boolean isMainArm = entity.getMainArm() == HumanoidArm.RIGHT;

        if (isMainArm && mainHand.is(ModItems.MICROPHONE) || (!isMainArm && offHand.is(ModItems.MICROPHONE))) {
            this.rightArm.xRot = Mth.clamp((float) this.head.xRot, (float) -0.5f, (float) 0.5f) - 1.4f;
            this.rightArm.yRot = Mth.clamp((float) this.head.yRot, (float) -0.3f, (float) 0.7f) - 0.5235988f;
        }
    }

    @Inject(method = "poseLeftArm", at = @At("TAIL"))
    public void projectmania$injectPositionLeftArm(T entity, CallbackInfo ci) {
        // case TOOT_HORN: {
        //     this.leftArm.pitch = MathHelper.clamp((float)this.head.pitch, (float)-1.2f, (float)1.2f) - 1.4835298f;
        //     this.leftArm.yaw = this.head.yaw + 0.5235988f;
        // }
        ItemStack mainHand = entity.getItemInHand(InteractionHand.MAIN_HAND);
        ItemStack offHand = entity.getItemInHand(InteractionHand.OFF_HAND);
        boolean isMainArm = entity.getMainArm() == HumanoidArm.LEFT;

        if (isMainArm && mainHand.is(ModItems.MICROPHONE) || (!isMainArm && offHand.is(ModItems.MICROPHONE))) {
            this.leftArm.xRot = Mth.clamp((float) this.head.xRot, (float) -0.5f, (float) 0.5f) - 1.4f;
            this.leftArm.yRot = Mth.clamp((float) this.head.yRot, (float) -0.7f, (float) 0.3f) + 0.5235988f;
        }
    }
}


