package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.models.entity.GruskHeadModel;
import com.wadoo.hyperion.client.models.entity.ObeliskModel;
import com.wadoo.hyperion.client.renderers.layers.CrucibleLavaLayer;
import com.wadoo.hyperion.client.renderers.layers.ObeliskEyesLayer;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import com.wadoo.hyperion.common.entities.obelisk.ObeliskEntity;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ParticleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import java.util.function.Function;


public class ObeliskRenderer extends GeoEntityRenderer<ObeliskEntity> {
    private final EntityRenderDispatcher entityRenderer;


    public ObeliskRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ObeliskModel());
        addRenderLayer(new ObeliskEyesLayer<>(this));
        this.shadowRadius = 1f;
        this.entityRenderer = renderManager.getEntityRenderDispatcher();
    }

    @Override
    public void render(ObeliskEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public void renderRecursively(PoseStack poseStack, ObeliskEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("head")){
            Vec3 pos = animatable.position();
            if (!Minecraft.getInstance().isPaused()) {
                Vec3 delta = animatable.getDeltaMovement();
                if (animatable.getRandom().nextFloat() < 0.1f)
                    animatable.level().addParticle(ParticleHandler.AGRALITE_FLAME.get(), pos.x, pos.y + 1.7d + Math.sin(animatable.tickCount / 5f) * 0.05d, pos.z, delta.x, delta.y, delta.z);
            }
            Entity e = EntityHandler.CLINKER.get().create(animatable.level());

            if (e != null) {

                poseStack.pushPose();
                poseStack.scale(0.7f,0.7f,0.7f);
                poseStack.translate(0d,2.25d + Math.sin(animatable.tickCount / 5f) * 0.05d,0d);
                poseStack.mulPose(Axis.YP.rotationDegrees( animatable.tickCount * 5));
                this.entityRenderer.render(e, 0.0D, 0.0D, 0.0D, 0f, partialTick, poseStack, bufferSource, packedLight);
                poseStack.popPose();
            }
        }

    }
}