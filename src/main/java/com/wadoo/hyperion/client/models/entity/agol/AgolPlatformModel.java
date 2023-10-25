package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorEntity;
import com.wadoo.hyperion.common.entities.agol.AgolPlatformEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolPlatformModel extends DefaultedEntityGeoModel<AgolPlatformEntity> {
    public AgolPlatformModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_platform"));
    }

    @Override
    public RenderType getRenderType(AgolPlatformEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(AgolPlatformEntity animatable, long instanceId, AnimationState<AgolPlatformEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Entity entity = Minecraft.getInstance().getCameraEntity();

        CoreGeoBone leftEye = getAnimationProcessor().getBone("leftPupil");
        CoreGeoBone rightEye = getAnimationProcessor().getBone("rightPupil");


        if (leftEye != null && leftEye.getInitialSnapshot() != null && rightEye != null && rightEye.getInitialSnapshot() != null) {


            if (entity != null) {
                Vec3 vec3 = entity.getEyePosition(0.0F);
                Vec3 vec31 = animatable.getPosition(0.0F).add(0d,0.2d,0d);
                if(vec3.y > vec31.y){
                    rightEye.setPosY(0.95f);
                    leftEye.setPosY(0.95f);
                }
                else{
                    rightEye.setPosY(0.05f);
                    leftEye.setPosY(0.05f);
                }

                Vec3 vec32 = animatable.getViewVector(0.0F);
                vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
                Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
                double d1 = vec32.dot(vec33);
                rightEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));
                leftEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));}
        }
    }
}
