package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GruskModel extends DefaultedEntityGeoModel<GruskEntity> {

    public GruskModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk"));
    }

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

    @Override
    public RenderType getRenderType(GruskEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
