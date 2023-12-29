package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolHeadModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolShooterModel;
import com.wadoo.hyperion.client.renderers.layers.AgolHeadEyesLayer;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.entities.agol.AgolShooter;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolShooterRenderer extends GeoEntityRenderer<AgolShooter> {
    public AgolShooterRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolShooterModel());
        this.shadowRadius = 1F;
    }
}