package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.models.entity.ForgenautModel;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.DynamicGeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.function.Function;


public class ForgenautRenderer extends DynamicGeoEntityRenderer<ForgenautEntity> {
    private int currentTick = -1;
    private BakedGeoModel LASER_MODEL = getGeoModel().getBakedModel(new ResourceLocation(Hyperion.MODID, "geo/entity/effect/laser.geo.json"));
    private ResourceLocation LASER_TEXTURE = new ResourceLocation(Hyperion.MODID, "textures/entity/effect/forgenaut_laser.png");

    public ForgenautRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ForgenautModel());

        this.shadowRadius = 1.86788F;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, ForgenautEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("fire") && animatable.getFiring()){
            if(animatable.getFiring()){bone.setHidden(false);}
            else{bone.setHidden(true);}
        }
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Nullable
    @Override
    protected RenderType getRenderTypeOverrideForBone(GeoBone bone, ForgenautEntity animatable, ResourceLocation texturePath, MultiBufferSource bufferSource, float partialTick) {
        if(bone.getName().equals("fire")){
            return RenderType.eyes(new ResourceLocation(Hyperion.MODID, "textures/entity/forgenaut.png"));
        }
        if(bone.getName().equals("inner_fire")){
            return RenderType.entityCutout(new ResourceLocation(Hyperion.MODID, "textures/entity/forgenaut.png"));
            //return RenderType.create("solid", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 2097152, true, false, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_SOLID_SHADER).createCompositeState(true));
        }
        return super.getRenderTypeOverrideForBone(bone, animatable, texturePath, bufferSource, partialTick);
    }
}