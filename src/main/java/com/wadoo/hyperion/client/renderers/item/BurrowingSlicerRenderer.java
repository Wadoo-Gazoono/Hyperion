package com.wadoo.hyperion.client.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.BurrowingSlicerItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class BurrowingSlicerRenderer extends GeoItemRenderer<BurrowingSlicerItem> {
    public BurrowingSlicerRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Hyperion.MODID, "burrowing_slicer")));
    }
}