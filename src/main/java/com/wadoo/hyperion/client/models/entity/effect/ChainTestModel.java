package com.wadoo.hyperion.client.models.entity.effect;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.effects.ChainTestEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ChainTestModel extends DefaultedEntityGeoModel<ChainTestEntity> {

    public ChainTestModel() {
        super(new ResourceLocation(Hyperion.MODID, "grusk_head"));
    }

    @Override
    public ResourceLocation getModelResource(ChainTestEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/chain_test.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ChainTestEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/chain_test.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ChainTestEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/chain_test.animation.json");
    }

    @Override
    public RenderType getRenderType(ChainTestEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
