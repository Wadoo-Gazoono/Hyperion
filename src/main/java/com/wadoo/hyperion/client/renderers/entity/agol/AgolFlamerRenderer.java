package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolFlamerModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolSeatModel;
import com.wadoo.hyperion.common.entities.agol.AgolFlamerEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSeatEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolFlamerRenderer extends GeoEntityRenderer<AgolFlamerEntity> {
    public AgolFlamerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolFlamerModel());
        this.shadowRadius = 0F;
    }
}