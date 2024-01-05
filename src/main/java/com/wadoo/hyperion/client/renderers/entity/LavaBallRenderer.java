package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.LavaBallModel;
import com.wadoo.hyperion.client.models.entity.VolatileGoopModel;
import com.wadoo.hyperion.common.entities.projectiles.LavaBallProjectile;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;


public class LavaBallRenderer extends GeoEntityRenderer<LavaBallProjectile> {

    public LavaBallRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new LavaBallModel());
    }

}