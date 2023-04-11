package com.wadoo.hyperion.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.CoolKidEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CrucibleLavaLayer<T extends CrucibleEntity> extends GeoRenderLayer<CrucibleEntity> {
    private ResourceLocation texture = new ResourceLocation(Hyperion.MODID, "textures/entity/crucible/crucible_lava_01.png");
    public CrucibleLavaLayer(GeoRenderer<CrucibleEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void render(PoseStack poseStack, CrucibleEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(animatable.tickCount % 4 == 0){
            if (animatable.texture_frame < 20) animatable.texture_frame++;
            else animatable.texture_frame = 1;
        }
        texture = new ResourceLocation(Hyperion.MODID, "textures/entity/crucible/crucible_lava_" + animatable.texture_frame + ".png");
        RenderType armorRenderType = RenderType.eyes(texture);
        getRenderer().reRender(getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType,
                bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY,
                1, 1, 1, 1);
    }
}
