package com.wadoo.hyperion.client.renderers.entity;

import com.wadoo.hyperion.client.models.entity.ForgenautModel;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class ForgenautRenderer extends GeoEntityRenderer<ForgenautEntity> {

    public ForgenautRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ForgenautModel());
        this.shadowRadius = 1.86788F;
    }

}