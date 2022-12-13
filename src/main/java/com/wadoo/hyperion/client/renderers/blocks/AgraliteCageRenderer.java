package com.wadoo.hyperion.client.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.blocks.AgraliteCageModel;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.block.tile.FertilizerTileEntity;
import software.bernie.example.client.model.tile.FertilizerModel;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoBlockRenderer;

public class AgraliteCageRenderer extends GeoBlockRenderer<AgraliteCageBlockEntity> {
    public AgraliteCageRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(rendererDispatcherIn, new AgraliteCageModel());
    }

    @Override
    public void render(GeoModel model, AgraliteCageBlockEntity animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if(animatable.getHasCapsling()){
            poseStack.scale(1.5f, 1.5F,1.5f);
        }
        else{
            poseStack.scale(0.5f, 0.5F,0.5f);

        }
        poseStack.translate(0, -0.01D, 0);
        poseStack.popPose();
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
