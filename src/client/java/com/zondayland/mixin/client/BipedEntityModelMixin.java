package com.zondayland.mixin.client;

import com.zondayland.registry.ModItems;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.AnimalModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BipedEntityModel.class)
public abstract class BipedEntityModelMixin<T extends LivingEntity> extends AnimalModel<T> implements ModelWithArms, ModelWithHead {

    @Final
    @Shadow
    public ModelPart rightArm;

    @Final
    @Shadow
    public ModelPart leftArm;

    @Final
    @Shadow
    public ModelPart head;

    @Inject(method = "positionRightArm", at = @At("TAIL"))
    public void zondayland$injectPositionRightArm(T entity, CallbackInfo ci) {
        // positionRightArm(T entity)
        // case TOOT_HORN: {
        //      this.rightArm.pitch = MathHelper.clamp((float)this.head.pitch, (float)-1.2f, (float)1.2f) - 1.4835298f;
        //      this.rightArm.yaw = this.head.yaw - 0.5235988f;
        // }
        ItemStack mainHand = entity.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHand = entity.getStackInHand(Hand.OFF_HAND);
        boolean isMainArm = entity.getMainArm() == Arm.RIGHT;

        if (isMainArm && mainHand.isOf(ModItems.MICROPHONE) || (!isMainArm && offHand.isOf(ModItems.MICROPHONE))) {
            this.rightArm.pitch = MathHelper.clamp((float) this.head.pitch, (float) -0.5f, (float) 0.5f) - 1.4f;
            this.rightArm.yaw = MathHelper.clamp((float) this.head.yaw, (float) -0.3f, (float) 0.7f) - 0.5235988f;
        }
    }

    @Inject(method = "positionLeftArm", at = @At("TAIL"))
    public void zondayland$injectPositionLeftArm(T entity, CallbackInfo ci) {
        // case TOOT_HORN: {
        //     this.leftArm.pitch = MathHelper.clamp((float)this.head.pitch, (float)-1.2f, (float)1.2f) - 1.4835298f;
        //     this.leftArm.yaw = this.head.yaw + 0.5235988f;
        // }
        ItemStack mainHand = entity.getStackInHand(Hand.MAIN_HAND);
        ItemStack offHand = entity.getStackInHand(Hand.OFF_HAND);
        boolean isMainArm = entity.getMainArm() == Arm.LEFT;

        if (isMainArm && mainHand.isOf(ModItems.MICROPHONE) || (!isMainArm && offHand.isOf(ModItems.MICROPHONE))) {
            this.leftArm.pitch = MathHelper.clamp((float) this.head.pitch, (float) -0.5f, (float) 0.5f) - 1.4f;
            this.leftArm.yaw = MathHelper.clamp((float) this.head.yaw, (float) -0.7f, (float) 0.3f) + 0.5235988f;
        }
    }
}


