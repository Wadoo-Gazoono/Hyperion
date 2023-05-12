package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.entity.AutoMiningDroidModel;
import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.client.renderers.layers.AMDEyesLayer;
import com.wadoo.hyperion.common.entities.AutoMiningDroidEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GruskRenderer extends GeoEntityRenderer<GruskEntity> {
    public GruskRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GruskModel());
        this.shadowRadius = 0.46788F;
    }
}