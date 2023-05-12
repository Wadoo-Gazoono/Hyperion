package com.wadoo.hyperion.client.renderers.entity.effect;

import com.wadoo.hyperion.client.models.entity.effect.BasaltSpikeModel;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class BasaltSpikeRenderer extends GeoEntityRenderer<BasaltSpikeEntity> {

    public BasaltSpikeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BasaltSpikeModel());
        this.shadowRadius = 0.2788F;
    }


}