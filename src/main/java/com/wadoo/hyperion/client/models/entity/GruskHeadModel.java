package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GruskHeadModel extends DefaultedEntityGeoModel<GruskHeadEntity> {
    public GruskHeadModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk"));
    }

    @Override
    public ResourceLocation getModelResource(GruskHeadEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/grusk_head.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GruskHeadEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/grusk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GruskHeadEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/grusk_head.animation.json");
    }

    @Override
    public RenderType getRenderType(GruskHeadEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(GruskHeadEntity animatable, long instanceId, AnimationState<GruskHeadEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
