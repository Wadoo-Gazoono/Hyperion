package com.wadoo.hyperion.client.models.block;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;


public class AgraliteCageModel extends DefaultedBlockGeoModel<AgraliteCageBlockEntity> {

    public AgraliteCageModel() {
        super(new ResourceLocation(Hyperion.MODID, "agralite_cage"));    }

    @Override
    public ResourceLocation getModelResource(AgraliteCageBlockEntity object) {
        if(object.isFull){
            return buildFormattedModelPath(new ResourceLocation(Hyperion.MODID, "agralite_cage_capsling"));
        }
        return buildFormattedModelPath(new ResourceLocation(Hyperion.MODID, "agralite_cage"));
    }

    @Override
    public ResourceLocation getTextureResource(AgraliteCageBlockEntity object) {
        return buildFormattedTexturePath(new ResourceLocation(Hyperion.MODID, "agralite_cage"));
    }

    @Override
    public ResourceLocation getAnimationResource(AgraliteCageBlockEntity animatable) {
        return buildFormattedAnimationPath(new ResourceLocation(Hyperion.MODID, "agralite_cage"));
    }
}
