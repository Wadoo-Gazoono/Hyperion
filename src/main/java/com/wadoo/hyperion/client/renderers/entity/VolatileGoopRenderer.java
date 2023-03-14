package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.client.models.entity.VolatileGoopModel;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;


public class VolatileGoopRenderer extends GeoEntityRenderer<VolatileGoopProjectile> {

    public VolatileGoopRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new VolatileGoopModel());
        this.shadowRadius = 0.02F;
    }

    @Override
    public void preRender(PoseStack poseStack, VolatileGoopProjectile animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        RenderUtils.faceRotation(poseStack,animatable,partialTick);
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}