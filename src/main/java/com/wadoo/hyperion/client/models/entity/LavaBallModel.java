package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.projectiles.LavaBallProjectile;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class LavaBallModel extends DefaultedEntityGeoModel<LavaBallProjectile> {

    public LavaBallModel() {
        super(new ResourceLocation(Hyperion.MODID, "effect/lava_ball"));
    }


    @Override
    public RenderType getRenderType(LavaBallProjectile animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }

}
