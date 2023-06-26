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

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}
