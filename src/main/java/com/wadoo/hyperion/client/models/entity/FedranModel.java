package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FedranModel extends DefaultedEntityGeoModel<FedranEntity> {
    public FedranModel() {
        super(new ResourceLocation(Hyperion.MODID, "fedran"));
    }

    @Override
    public ResourceLocation getModelResource(FedranEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/fedran.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FedranEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/fedran.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FedranEntity entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/fedran.animation.json");
    }

    @Override
    public RenderType getRenderType(FedranEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

    @Override
    public void setCustomAnimations(FedranEntity animatable, long instanceId, AnimationState<FedranEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone("head");
//        CoreGeoBone leftChain = getAnimationProcessor().getBone("leftChain");
//        CoreGeoBone leftChain1 = getAnimationProcessor().getBone("leftChain1");
//        CoreGeoBone leftChain2 = getAnimationProcessor().getBone("leftChain2");
//        CoreGeoBone leftChain3 = getAnimationProcessor().getBone("leftChain3");
//        CoreGeoBone leftChain4 = getAnimationProcessor().getBone("leftChain4");
//        CoreGeoBone leftChain5 = getAnimationProcessor().getBone("leftChain5");
//        CoreGeoBone leftChain6 = getAnimationProcessor().getBone("leftChain6");
//        CoreGeoBone leftChain7 = getAnimationProcessor().getBone("leftChain7");
//        CoreGeoBone leftChain8 = getAnimationProcessor().getBone("leftChain8");
//        CoreGeoBone leftChain9 = getAnimationProcessor().getBone("leftChain9");
//        CoreGeoBone leftChain10 = getAnimationProcessor().getBone("leftChain10");

        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);


        if (head != null) {
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }

//        float z1 = (float) Math.atan2(animatable.leftP2.y - animatable.leftP1.y,animatable.leftP2.z-animatable.leftP1.z);
//        float x1 = (float) Math.atan2(animatable.leftP2.y - animatable.leftP1.y,animatable.leftP2.x-animatable.leftP1.x);
//
//        leftChain1.setRotX((z1 - (270*Mth.DEG_TO_RAD)));
//        leftChain1.setRotY((x1 - (270*Mth.DEG_TO_RAD)));
//
//        float z2 = (float) Math.atan2(animatable.leftP3.y - animatable.leftP2.y,animatable.leftP3.z-animatable.leftP2.z);
//        leftChain2.setRotX((z2 - (270*Mth.DEG_TO_RAD)));
//
//        float z3 = (float) Math.atan2(animatable.leftP4.y - animatable.leftP3.y,animatable.leftP4.z-animatable.leftP3.z);
//        leftChain3.setRotX((z3 - (270*Mth.DEG_TO_RAD)));
//
//        float z4 = (float) Math.atan2(animatable.leftP5.y - animatable.leftP4.y,animatable.leftP5.z-animatable.leftP4.z);
//        leftChain4.setRotX((z4 - (270*Mth.DEG_TO_RAD)));
//
//        float z5 = (float) Math.atan2(animatable.leftP6.y - animatable.leftP5.y,animatable.leftP6.z-animatable.leftP5.z);
//        leftChain5.setRotX((z5 - (270*Mth.DEG_TO_RAD)));
//
//        float z6 = (float) Math.atan2(animatable.leftP7.y - animatable.leftP6.y,animatable.leftP7.z-animatable.leftP6.z);
//        leftChain6.setRotX((z6 - (270*Mth.DEG_TO_RAD)));
//
//        float z7 = (float) Math.atan2(animatable.leftP8.y - animatable.leftP7.y,animatable.leftP8.z-animatable.leftP7.z);
//        leftChain7.setRotX((z7 - (270*Mth.DEG_TO_RAD)));
//
//        float z8 = (float) Math.atan2(animatable.leftP9.y - animatable.leftP8.y,animatable.leftP9.z-animatable.leftP8.z);
//        leftChain8.setRotX((z8 - (270*Mth.DEG_TO_RAD)));
//
//        float z9 = (float) Math.atan2(animatable.leftP10.y - animatable.leftP9.y,animatable.leftP10.z-animatable.leftP9.z);
//        leftChain9.setRotX((z9 - (270*Mth.DEG_TO_RAD)));
//
//        float z10 = (float) Math.atan2(animatable.leftP11.y - animatable.leftP10.y,animatable.leftP11.z-animatable.leftP10.z);
//        leftChain10.setRotX((z10 - (270*Mth.DEG_TO_RAD)));
    }
}
