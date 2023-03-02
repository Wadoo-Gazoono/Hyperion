package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class CapslingModel extends DefaultedEntityGeoModel<CapslingEntity> {

    public CapslingModel() {
        super(new ResourceLocation(Hyperion.MODID, "capsling"));
    }

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

    @Override
    public RenderType getRenderType(CapslingEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
