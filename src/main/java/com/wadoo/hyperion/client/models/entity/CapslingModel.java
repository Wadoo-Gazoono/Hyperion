package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedTickingGeoModel;

public class CapslingModel extends AnimatedTickingGeoModel<CapslingEntity> {

    @Override
    public ResourceLocation getModelResource(CapslingEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/capsling.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CapslingEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/capsling" + (entity.getAnimState() == 2 ? "_heat" : "") + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(CapslingEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/capsling.animation.json");
    }
}
