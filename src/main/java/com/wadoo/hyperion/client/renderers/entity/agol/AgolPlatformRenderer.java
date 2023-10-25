package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.agol.AgolConnectorModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolPlatformModel;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorEntity;
import com.wadoo.hyperion.common.entities.agol.AgolPlatformEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolPlatformRenderer extends GeoEntityRenderer<AgolPlatformEntity> {
    public AgolPlatformRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolPlatformModel());
        this.shadowRadius = 0F;
    }
}