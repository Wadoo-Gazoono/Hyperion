package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolConnectorTModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolHeadModel;
import com.wadoo.hyperion.client.renderers.layers.AgolHeadEyesLayer;
import com.wadoo.hyperion.client.renderers.layers.CrucibleLavaLayer;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorT;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolHeadRenderer extends GeoEntityRenderer<AgolHead> {
    public AgolHeadRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolHeadModel());
        addRenderLayer(new AgolHeadEyesLayer<>(this));

        this.shadowRadius = 1F;
    }
}