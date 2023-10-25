package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolSeatModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolSpeakerModel;
import com.wadoo.hyperion.common.entities.agol.AgolSeatEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolSeatRenderer extends GeoEntityRenderer<AgolSeatEntity> {
    public AgolSeatRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolSeatModel());
        this.shadowRadius = 0F;
    }
}