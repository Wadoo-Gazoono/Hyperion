package com.wadoo.hyperion.client.renderers.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.util.RenderUtils;

public class CapslingItemLayer<T extends CapslingEntity> extends GeoRenderLayer<T> {
    protected Matrix4f entityRenderTranslations = new Matrix4f();
    protected Matrix4f modelRenderTranslations = new Matrix4f();
    private CapslingEntity entity;


    public CapslingItemLayer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.preRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        this.entityRenderTranslations = new Matrix4f(poseStack.last().pose());
        this.modelRenderTranslations = new Matrix4f(poseStack.last().pose());

    }

    @Override
    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        entity = animatable;
        renderRecursively(entity,bakedModel.topLevelBones().get(0), poseStack, bufferSource, packedLight, OverlayTexture.NO_OVERLAY);

    }

    public void renderRecursively(CapslingEntity entity, GeoBone bone, PoseStack poseStack, MultiBufferSource buffer, int packedLight,
                                  int packedOverlay) {
        poseStack.pushPose();
        //RenderUtils.translateMatrixToBone(poseStack, bone);
        //RenderUtils.translateToPivotPoint(poseStack, bone);
        RenderUtils.translateAndRotateMatrixForBone(poseStack, bone);
        RenderUtils.scaleMatrixForBone(poseStack, bone);

        if (!bone.isHidden()) {
            for (GeoBone childBone : bone.getChildBones()) {
                renderRecursively(entity,childBone, poseStack, buffer, packedLight, packedOverlay);
            }
        }

        if (bone.isTrackingMatrices()) {
            Matrix4f poseState = new Matrix4f(poseStack.last().pose());
            Matrix4f localMatrix = RenderUtils.invertAndMultiplyMatrices(poseState, this.entityRenderTranslations);

            bone.setModelSpaceMatrix(RenderUtils.invertAndMultiplyMatrices(poseState, this.modelRenderTranslations));
            bone.setLocalSpaceMatrix(localMatrix.translation(new Vector3f(getRenderOffset(this.entity, 1).toVector3f())));
            bone.setWorldSpaceMatrix(new Matrix4f(localMatrix).translation(new Vector3f(this.entity.position().toVector3f())));
        }

        RenderUtils.translateAwayFromPivotPoint(poseStack, bone);

        if(bone.getName().equals("item") && !bone.isHidden()){
            //poseStack.scale(1.5f,1.5f,1.5f);
            Minecraft.getInstance().getItemRenderer().renderStatic(entity.getItemBySlot(EquipmentSlot.MAINHAND), ItemTransforms.TransformType.GROUND,
                    packedLight, packedOverlay, poseStack, buffer, entity.getId());
        }
        poseStack.popPose();
    }

    public Vec3 getRenderOffset(CapslingEntity p_114483_, float p_114484_) {
        return Vec3.ZERO;
    }

}