package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GruskHeadModel extends DefaultedEntityGeoModel<GruskHeadEntity> {

    public GruskHeadModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk_head"));
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

        CoreGeoBone head = getAnimationProcessor().getBone("bodyController");

        if (!animatable.isOnGround() && head != null && head.getInitialSnapshot() != null) {

           // head.setRotX(head.getRotX() + 0.1f);
        }
    }
}
