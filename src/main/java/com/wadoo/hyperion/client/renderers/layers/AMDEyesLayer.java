package com.wadoo.hyperion.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.AutoMiningDroidEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AMDEyesLayer<T extends AutoMiningDroidEntity> extends GeoRenderLayer<AutoMiningDroidEntity> {
    private ResourceLocation texture = new ResourceLocation(Hyperion.MODID, "textures/entity/auto_mining_droid_eyes.png");
    public AMDEyesLayer(GeoRenderer<AutoMiningDroidEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, AutoMiningDroidEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.eyes(texture);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}