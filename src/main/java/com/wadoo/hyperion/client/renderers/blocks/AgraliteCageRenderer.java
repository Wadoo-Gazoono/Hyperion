package com.wadoo.hyperion.client.renderers.blocks;

import com.wadoo.hyperion.client.models.blocks.AgraliteCageModel;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.example.block.tile.FertilizerTileEntity;
import software.bernie.example.client.model.tile.FertilizerModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AgraliteCageRenderer extends GeoBlockRenderer<AgraliteCageBlockEntity> {
    public AgraliteCageRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AgraliteCageModel());
    }
}
