package com.wadoo.hyperion.client.models.blocks;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;


public class AgraliteCageModel extends AnimatedGeoModel<AgraliteCageBlockEntity> {


    @Override
    public ResourceLocation getModelResource(AgraliteCageBlockEntity object) {
        return new ResourceLocation(Hyperion.MODID, "geo/block/agralite_cage.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AgraliteCageBlockEntity object) {
        return new ResourceLocation(Hyperion.MODID, "textures/block/agralite_cage.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AgraliteCageBlockEntity animatable) {
        return new ResourceLocation(Hyperion.MODID, "animations/block/agralite_cage.animation.json");
    }
}
