package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolConnectorModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolSpeakerModel;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolConnectorRenderer extends GeoEntityRenderer<AgolConnectorEntity> {
    public AgolConnectorRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolConnectorModel());
        this.shadowRadius = 0F;
    }
}