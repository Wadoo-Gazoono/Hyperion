package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolConnectorTModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolWalkerModel;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorT;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolConnectorTRenderer extends GeoEntityRenderer<AgolConnectorT> {
    public AgolConnectorTRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolConnectorTModel());
        this.shadowRadius = 1F;
    }
}