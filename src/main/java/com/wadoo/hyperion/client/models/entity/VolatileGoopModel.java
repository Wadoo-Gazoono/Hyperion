package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class VolatileGoopModel extends DefaultedEntityGeoModel<VolatileGoopProjectile> {

    public VolatileGoopModel() {
        super(new ResourceLocation(Hyperion.MODID, "volatile_goop"));
    }

    @Override
    public ResourceLocation getModelResource(VolatileGoopProjectile entity) {
        return new ResourceLocation(Hyperion.MODID, "geo/entity/volatile_goop.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(VolatileGoopProjectile entity) {
        return new ResourceLocation(Hyperion.MODID, "textures/entity/projectile/volatile_goop.png");
    }

    @Override
    public ResourceLocation getAnimationResource(VolatileGoopProjectile entity) {
        return new ResourceLocation(Hyperion.MODID, "animations/entity/volatile_goop.animation.json");
    }

    @Override
    public RenderType getRenderType(VolatileGoopProjectile animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
