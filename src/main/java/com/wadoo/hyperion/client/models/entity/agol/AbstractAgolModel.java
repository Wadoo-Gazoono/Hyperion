package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AbstractAgolModel extends DefaultedEntityGeoModel<AbstractAgolEntity> {
    public AbstractAgolModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_base"));
    }
}
