package com.Wadoo.hyperion.client.renderer;

import com.Wadoo.hyperion.client.model.BasaltArrowModel;
import com.Wadoo.hyperion.server.entity.BasaltArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoProjectilesRenderer;

import javax.annotation.Nullable;

public class BasaltArrowRenderer extends GeoProjectilesRenderer<BasaltArrowEntity> {

    public BasaltArrowRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BasaltArrowModel());
    }

    @Override
    public void render(GeoModel model, BasaltArrowEntity animatable, float partialTicks, RenderType type, PoseStack matrixStackIn, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTicks, type, matrixStackIn, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.pushPose();
        matrixStackIn.popPose();
    }
}
