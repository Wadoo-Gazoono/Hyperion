package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.AutoMiningDroidEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AutoMiningDroidModel extends DefaultedEntityGeoModel<AutoMiningDroidEntity> {

    public AutoMiningDroidModel() {
        super(new ResourceLocation(Hyperion.MODID, "auto_mining_droid"));
    }

    @Override
    public ResourceLocation getModelResource(AutoMiningDroidEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/auto_mining_droid.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(AutoMiningDroidEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/auto_mining_droid.png");
    }

    @Override
    public ResourceLocation getAnimationResource(AutoMiningDroidEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/auto_mining_droid.animation.json");
    }

    @Override
    public void setCustomAnimations(AutoMiningDroidEntity animatable, long instanceId, AnimationState<AutoMiningDroidEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Entity entity = Minecraft.getInstance().getCameraEntity();

        CoreGeoBone leftTopEye = getAnimationProcessor().getBone("leftTopPupil");
        CoreGeoBone rightTopEye = getAnimationProcessor().getBone("rightTopPupil");
        CoreGeoBone leftBottomEye = getAnimationProcessor().getBone("leftBottomPupil");
        CoreGeoBone rightBottomEye = getAnimationProcessor().getBone("rightBottomPupil");

        CoreGeoBone root = getAnimationProcessor().getBone("root");
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        if (root != null) {
            root.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }
        if(head != null){
            head.setRotX((entityData.headPitch() -25) * ((float) Math.PI / 360F));
           // head.setRotY(entityData.netHeadYaw() * ((float) Math.PI / 180F));
        }

        if (leftTopEye != null && leftTopEye.getInitialSnapshot() != null && rightTopEye != null && rightTopEye.getInitialSnapshot() != null) {


            if (entity != null) {
                Vec3 vec3 = entity.getEyePosition(0.0F);
                Vec3 vec31 = animatable.getEyePosition(0.0f);
                if(vec3.y > vec31.y){
                    rightTopEye.setPosY(0.95f);
                    leftTopEye.setPosY(0.95f);
                    rightBottomEye.setPosY(0.95f);
                    leftBottomEye.setPosY(0.95f);
                }
                else{
                    rightTopEye.setPosY(-0.95f);
                    leftTopEye.setPosY(-0.95f);
                    rightBottomEye.setPosY(-0.95f);
                    leftBottomEye.setPosY(-0.95f);
                }

                Vec3 vec32 = animatable.getViewVector(0.0F);
                vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
                Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
                double d1 = vec32.dot(vec33);
                rightTopEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 1.3F * (float) Math.signum(d1));
                leftTopEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 1.3F * (float) Math.signum(d1));
                rightBottomEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 1.3F * (float) Math.signum(d1));
                leftBottomEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 1.3F * (float) Math.signum(d1));}
        }

    }

}
