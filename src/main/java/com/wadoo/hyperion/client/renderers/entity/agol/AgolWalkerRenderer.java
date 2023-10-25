package com.wadoo.hyperion.client.renderers.entity.agol;

import com.wadoo.hyperion.client.models.entity.GruskHeadModel;
import com.wadoo.hyperion.client.models.entity.agol.AgolWalkerModel;
import com.wadoo.hyperion.common.entities.agol.AgolWalkerEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class AgolWalkerRenderer extends GeoEntityRenderer<AgolWalkerEntity> {
    public AgolWalkerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AgolWalkerModel());
        this.shadowRadius = 1F;
    }
}