package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolFlamerEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSeatEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolFlamerModel extends DefaultedEntityGeoModel<AgolFlamerEntity> {
    public AgolFlamerModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_flamer"));
    }

    @Override
    public RenderType getRenderType(AgolFlamerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
