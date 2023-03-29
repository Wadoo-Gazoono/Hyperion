package com.wadoo.hyperion.client.renderers.block;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.block.AgraliteCageModel;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AgraliteCageRenderer extends GeoBlockRenderer<AgraliteCageBlockEntity> {
    public AgraliteCageRenderer() {
        super(new AgraliteCageModel());
    }

    @Override
    public void preRender(PoseStack poseStack, AgraliteCageBlockEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        poseStack.pushPose();
        poseStack.translate(0, -0.01D, 0);
        poseStack.popPose();
        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public boolean shouldRender(AgraliteCageBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }
}
