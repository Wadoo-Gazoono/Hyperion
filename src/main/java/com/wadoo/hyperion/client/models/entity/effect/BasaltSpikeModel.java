package com.wadoo.hyperion.client.models.entity.effect;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BasaltSpikeModel extends DefaultedEntityGeoModel<BasaltSpikeEntity> {

    public BasaltSpikeModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk_head"));
    }

    @Override
    public ResourceLocation getModelResource(BasaltSpikeEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/effect/basalt_spike.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BasaltSpikeEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/effect/basalt_spike.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BasaltSpikeEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/effect/basalt_spike.animation.json");
    }

    @Override
    public RenderType getRenderType(BasaltSpikeEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
