package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolSpeakerModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolWalkerModel;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolSpeakerRenderer extends GeoEntityRenderer<AgolSpeakerEntity> {
    public AgolSpeakerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolSpeakerModel());
        this.shadowRadius = 0F;
    }
}