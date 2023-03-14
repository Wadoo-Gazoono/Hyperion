package com.wadoo.hyperion.client.renderers.entity;

import com.wadoo.hyperion.client.models.entity.GruskHeadModel;
import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class GruskHeadRenderer extends GeoEntityRenderer<GruskHeadEntity> {

    public GruskHeadRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new GruskHeadModel());
        this.shadowRadius = 0.26788F;
    }


}