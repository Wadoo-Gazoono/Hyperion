package com.wadoo.hyperion.client.models.block;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import com.wadoo.hyperion.common.blocks.entities.GrimSpireDoorBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;


public class GrimSpireDoorModel extends DefaultedBlockGeoModel<GrimSpireDoorBlockEntity> {

    public GrimSpireDoorModel() {
        super(new ResourceLocation(Hyperion.MODID, "grimspire_door"));    }

    @Override
    public ResourceLocation getModelResource(GrimSpireDoorBlockEntity object) {
        return buildFormattedModelPath(new ResourceLocation(Hyperion.MODID, "grimspire_door"));
    }

    @Override
    public ResourceLocation getTextureResource(GrimSpireDoorBlockEntity object) {
        return buildFormattedTexturePath(new ResourceLocation(Hyperion.MODID, "grimspire_door"));
    }

    @Override
    public ResourceLocation getAnimationResource(GrimSpireDoorBlockEntity animatable) {
        return buildFormattedAnimationPath(new ResourceLocation(Hyperion.MODID, "grimspire_door"));
    }
}
