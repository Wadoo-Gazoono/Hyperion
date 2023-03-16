package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GruskModel extends DefaultedEntityGeoModel<GruskEntity> {

    public GruskModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk"));
    }

    @Override
    public ResourceLocation getModelResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/grusk" + (entity.getHasHead() ? "" : "_headless")+".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/grusk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/grusk.animation.json");
    }

    @Override
    public RenderType getRenderType(GruskEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
