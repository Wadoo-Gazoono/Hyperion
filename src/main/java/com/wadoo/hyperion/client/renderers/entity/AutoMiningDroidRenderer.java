package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.AutoMiningDroidModel;
import com.wadoo.hyperion.client.renderers.layers.AMDEyesLayer;
import com.wadoo.hyperion.common.entities.AutoMiningDroidEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AutoMiningDroidRenderer extends GeoEntityRenderer<AutoMiningDroidEntity> {

    public AutoMiningDroidRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AutoMiningDroidModel());
        addRenderLayer(new AMDEyesLayer<>(this));
        this.shadowRadius = 1.86788F;
    }


    @Override
    public void renderRecursively(PoseStack poseStack, AutoMiningDroidEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}