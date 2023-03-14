package com.wadoo.hyperion.client.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.GigaHammerItem;
import com.wadoo.hyperion.common.items.VolatileGoopItem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GigaHammerRenderer extends GeoItemRenderer<GigaHammerItem> {
    public GigaHammerRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Hyperion.MODID, "giga_hammer")));
    }

    @Override
    public void renderRecursively(PoseStack poseStack, GigaHammerItem animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}