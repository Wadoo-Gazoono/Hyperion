package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolHeadModel extends DefaultedEntityGeoModel<AgolHead> {
    public AgolHeadModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_head"));
    }

    @Override
    public void setCustomAnimations(AgolHead animatable, long instanceId, AnimationState<AgolHead> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Entity entity = Minecraft.getInstance().getCameraEntity();

        CoreGeoBone eye = getAnimationProcessor().getBone("pupil");
        CoreGeoBone iris = getAnimationProcessor().getBone("iris");


        if (eye != null && eye.getInitialSnapshot() != null && iris != null && iris.getInitialSnapshot() != null) {


            if (entity != null) {
                Vec3 vec3 = entity.getEyePosition(0.0F);
                Vec3 vec31 = animatable.getPosition(0.0F).add(0d,0.2d,0d);
                if(vec3.y > vec31.y){
                    iris.setPosY(0.95f);
                    eye.setPosY(1f);
                }
                else{
                    iris.setPosY(-0.95f);
                    eye.setPosY(-1f);
                }

                Vec3 vec32 = animatable.getViewVector(0.0F);
                vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
                Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
                double d1 = vec32.dot(vec33);
                iris.setPosX(Mth.sqrt((float) Math.abs(d1)) * 4.0F * (float) Math.signum(d1));
                eye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 5.0F * (float) Math.signum(d1));}

        }
    }
}
