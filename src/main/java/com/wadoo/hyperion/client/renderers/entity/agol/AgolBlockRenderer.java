package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolBlockModel;
import com.wadoo.hyperion.common.entities.agol.AgolBlockEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolBlockRenderer extends GeoEntityRenderer<AgolBlockEntity> {
    public AgolBlockRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolBlockModel());
        this.shadowRadius = 0F;
    }
}