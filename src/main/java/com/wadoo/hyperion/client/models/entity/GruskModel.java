package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class GruskModel extends AnimatedTickingGeoModel<GruskEntity> {

    @Override
    public ResourceLocation getModelResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/grusk.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/grusk.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GruskEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/grusk.animation.json");
    }
}
