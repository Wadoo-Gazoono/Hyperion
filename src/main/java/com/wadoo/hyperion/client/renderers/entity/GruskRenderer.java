package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.common.entities.GruskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class GruskRenderer extends GeoEntityRenderer<GruskEntity> {

    public GruskRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GruskModel());
        this.shadowRadius = 0.26788F;
    }

    @Override
    public RenderType getRenderType(GruskEntity animatable, float partialTick, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}