package com.wadoo.hyperion.client.renderers.blocks;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.blocks.AgraliteCageModel;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AgraliteCageRenderer extends GeoBlockRenderer<AgraliteCageBlockEntity> {
    public AgraliteCageRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn) {
        super(new AgraliteCageModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AgraliteCageBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        if(animatable.getHasCapsling()){
            poseStack.scale(1.5f, 1.5F,1.5f);
        }
        else{
            poseStack.scale(0.5f, 0.5F,0.5f);

        }
        poseStack.translate(0, -0.01D, 0);
        poseStack.popPose();
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}
