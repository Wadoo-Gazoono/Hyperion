package com.wadoo.hyperion.client.renderers.entity;

import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GruskRenderer extends GeoEntityRenderer<GruskEntity> {
    public GruskRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GruskModel());
        this.shadowRadius = 0.46788F;
    }
}