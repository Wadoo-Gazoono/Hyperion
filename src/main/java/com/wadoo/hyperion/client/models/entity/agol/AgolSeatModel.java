package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolSeatEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolSeatModel extends DefaultedEntityGeoModel<AgolSeatEntity> {
    public AgolSeatModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_seat"));
    }

    @Override
    public RenderType getRenderType(AgolSeatEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
