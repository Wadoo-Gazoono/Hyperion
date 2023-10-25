package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolWalkerModel extends DefaultedEntityGeoModel<AgolWalkerEntity> {
    public AgolWalkerModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol_core_walker"));
    }

    @Override
    public ResourceLocation getModelResource(AgolWalkerEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/agol/agol_core_walker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AgolWalkerEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/agol/agol_core_walker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AgolWalkerEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/agol/agol_core_walker.animation.json");
    }

    @Override
    public RenderType getRenderType(AgolWalkerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(AgolWalkerEntity animatable, long instanceId, AnimationState<AgolWalkerEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
