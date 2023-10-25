package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolPerfumerModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolSpeakerModel;
import com.wadoo.hyperion.common.entities.agol.AgolPerfumerEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolPerfumerRenderer extends GeoEntityRenderer<AgolPerfumerEntity> {
    public AgolPerfumerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolPerfumerModel());
        this.shadowRadius = 0F;
    }
}