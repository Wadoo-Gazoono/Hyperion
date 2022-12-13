package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import com.wadoo.hyperion.client.models.entity.CapslingModel;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;

import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class CapslingRenderer extends GeoEntityRenderer<CapslingEntity> {
    public CapslingEntity entity;

    public CapslingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CapslingModel());
        this.shadowRadius = 0.16788F;
    }

    @Override
    public RenderType getRenderType(CapslingEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void renderEarly(CapslingEntity animatable, PoseStack poseStack, float partialTick, MultiBufferSource bufferSource, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, poseStack, partialTick, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, partialTicks);
        this.entity = animatable;

    }

    @Override
    public void renderRecursively(GeoBone bone, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if(bone.getName().equals("item")){
            poseStack.pushPose();
            poseStack.translate(0d,0.55d,0.0d);
            poseStack.scale(0.5f, 0.5f, 0.5f);
            poseStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemTransforms.TransformType.GROUND, packedLight, packedOverlay, poseStack, this.rtb, entity.getId());
            poseStack.popPose();
            buffer = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }

        super.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}