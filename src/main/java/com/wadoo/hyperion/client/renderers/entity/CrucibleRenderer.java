package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.models.entity.CrucibleModel;
import com.wadoo.hyperion.client.renderers.layers.CrucibleEyesLayer;
import com.wadoo.hyperion.client.renderers.layers.CrucibleLavaLayer;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.example.client.renderer.entity.layer.CoolKidGlassesLayer;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;


public class CrucibleRenderer extends GeoEntityRenderer<CrucibleEntity> {
    private int currentTick = -1;
    public CrucibleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrucibleModel());
        addRenderLayer(new CrucibleLavaLayer<>(this));
        addRenderLayer(new CrucibleEyesLayer<>(this));

        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, CrucibleEntity animatable) {
                return bone.getName().equals("rightItem") ? animatable.getItemBySlot(EquipmentSlot.MAINHAND) : null;
            }

            @Override
            protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack, CrucibleEntity animatable) {
                return ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CrucibleEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-70));
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.scale(1.3f,1.3f,1.3f);
                poseStack.translate(0.05D, 0.01D, -0.1D);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }

        });
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, CrucibleEntity animatable) {
                return bone.getName().equals("leftItem") ? animatable.getItemBySlot(EquipmentSlot.OFFHAND) : null;
            }

            @Override
            protected ItemTransforms.TransformType getTransformTypeForStack(GeoBone bone, ItemStack stack, CrucibleEntity animatable) {
                return ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, CrucibleEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-70));
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.scale(1.3f,1.3f,1.3f);
                poseStack.translate(-0.05D, 0.01D, -0.05D);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }

        });
        this.shadowRadius = 1.56788F;
    }

    @Override
    protected float getDeathMaxRotation(CrucibleEntity animatable) {
        return 2f;
    }

    @Override
    public void postRender(PoseStack poseStack, CrucibleEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.postRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }
}