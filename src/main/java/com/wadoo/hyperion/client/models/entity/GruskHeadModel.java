package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class GruskHeadModel extends AnimatedTickingGeoModel<GruskHeadEntity> {

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
}
