package com.wadoo.hyperion.client.renderers.entity.agol;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.models.entity.agol.AgolModuleModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolWalkerModel;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.agol.AgolModuleEntity;
import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;


public class AgolModuleRenderer extends GeoEntityRenderer<AgolModuleEntity> {
    public AgolModuleRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolModuleModel());
        this.shadowRadius = 0F;
        this.addRenderLayer(new BlockAndItemGeoLayer<>(this) {
            @Nullable
            @Override
            protected ItemStack getStackForBone(GeoBone bone, AgolModuleEntity animatable) {
                return bone.getName().equals("rightItem") ? new ItemStack(Items.NETHERITE_AXE,1) : null;
            }

            @Override
            protected ItemDisplayContext getTransformTypeForStack(GeoBone bone, ItemStack stack, AgolModuleEntity animatable) {
                return ItemDisplayContext.THIRD_PERSON_RIGHT_HAND;
            }

            @Override
            protected void renderStackForBone(PoseStack poseStack, GeoBone bone, ItemStack stack, AgolModuleEntity animatable, MultiBufferSource bufferSource, float partialTick, int packedLight, int packedOverlay) {
                poseStack.mulPose(Axis.XP.rotationDegrees(-0));
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                poseStack.mulPose(Axis.ZP.rotationDegrees(0));
                poseStack.scale(1.3f,1.3f,1.3f);
                poseStack.translate(0.05D, 0.01D, -0.1D);
                super.renderStackForBone(poseStack, bone, stack, animatable, bufferSource, partialTick, packedLight,
                        packedOverlay);
            }

        });
    }
}