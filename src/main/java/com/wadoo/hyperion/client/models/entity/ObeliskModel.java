package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.entities.obelisk.ObeliskEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ObeliskModel extends DefaultedEntityGeoModel<ObeliskEntity> {
    public ObeliskModel() {
        super(new ResourceLocation(Hyperion.MODID, "obelisk"));
    }

    @Override
    public void setCustomAnimations(ObeliskEntity animatable, long instanceId, AnimationState<ObeliskEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Entity entity = Minecraft.getInstance().getCameraEntity();

        CoreGeoBone eyeFront = getAnimationProcessor().getBone("frontPupil");
        CoreGeoBone eyeBack = getAnimationProcessor().getBone("backPupil");
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (entity == null) return;

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
        if (eyeFront != null && eyeFront.getInitialSnapshot() != null) {
            Vec3 vec3 = entity.getEyePosition(0.0F);
            Vec3 vec31 = animatable.getPosition(0.0F).add(0d,0.2d,0d);


            Vec3 vec32 = animatable.getViewVector(0.0F);
            vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
            Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
            double d1 = vec32.dot(vec33);
            eyeFront.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));}

        if (eyeBack != null && eyeBack.getInitialSnapshot() != null) {
            Vec3 vec3 = entity.getEyePosition(0.0F);
            Vec3 vec31 = animatable.getPosition(0.0F).add(0d,0.2d,0d);

            Vec3 vec32 = animatable.getViewVector(0.0F);
            vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
            Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
            double d1 = vec32.dot(vec33);
            eyeBack.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));}
    }
}
