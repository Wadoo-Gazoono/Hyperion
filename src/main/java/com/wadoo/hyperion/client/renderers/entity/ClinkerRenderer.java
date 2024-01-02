package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.ClinkerModel;
import com.wadoo.hyperion.client.renderers.layers.ClinkerGlowLayer;
import com.wadoo.hyperion.common.entities.clinker.ClinkerEntity;
import com.wadoo.hyperion.common.registry.ParticleHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class ClinkerRenderer extends GeoEntityRenderer<ClinkerEntity> {
    private int currentTick = -1;
    public ClinkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ClinkerModel());
        addRenderLayer(new ClinkerGlowLayer<>(this));
    }

    @Override
    protected float getDeathMaxRotation(ClinkerEntity animatable) {
        return 0f;
    }

    @Override
    public void renderRecursively(PoseStack poseStack, ClinkerEntity entity, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderRecursively(poseStack, animatable, bone, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (bone.getName().equals("root")){


            if (this.currentTick < 0 || this.currentTick != entity.tickCount && !Minecraft.getInstance().isPaused()) {
                currentTick = entity.tickCount;
                Vec3 ringPos = entity.getSpawnPos();
                Vec3 pos = entity.position();
                Vec3 directionToMob = ringPos.vectorTo(entity.position());
                ringPos = ringPos.add(directionToMob.normalize().x, 0d, directionToMob.normalize().z);
                directionToMob = pos.vectorTo(ringPos).normalize();
                float randSpeed =  entity.getRandom().nextInt(5,10);
                if (entity.getRandom().nextFloat() < 0.2f){
                    entity.getCommandSenderWorld().addParticle(
                            ParticleTypes.SMALL_FLAME,
                            pos.x + entity.getRandom().nextInt(-5,5)/10f,
                            pos.y + 0.1d + entity.getRandom().nextInt(-5,5)/10f,
                            pos.z + entity.getRandom().nextInt(-5,5)/10f,
                            directionToMob.x / randSpeed,
                            directionToMob.y  / randSpeed,
                            directionToMob.z  / randSpeed

                    );
                }
                if (entity.getRandom().nextFloat() < 0.3f) {
                    entity.getCommandSenderWorld().addParticle(
                            ParticleHandler.AGRALITE_FLAME.get(),
                            pos.x + entity.getRandom().nextInt(-30, 30) / 40f,
                            pos.y + 0.25d,
                            pos.z + entity.getRandom().nextInt(-30, 30) / 40f,
                            0d,
                            0.1d + entity.getRandom().nextInt(0, 5) / 40f,
                            0d
                    );
                }
                if (entity.getRandom().nextFloat() < 0.1f) {
                    entity.getCommandSenderWorld().addParticle(
                            ParticleTypes.SMOKE,
                            pos.x + entity.getRandom().nextInt(-10, 10) / 40f,
                            pos.y + 0.25d,
                            pos.z + entity.getRandom().nextInt(-10, 10) / 40f,
                            0d,
                            0.05d + entity.getRandom().nextInt(0, 3) / 80f,
                            0d
                    );
                }
            }
        }
    }

    @Override
    public void render(ClinkerEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        //if (entity.getOwner() == null) {


        //
    }
}