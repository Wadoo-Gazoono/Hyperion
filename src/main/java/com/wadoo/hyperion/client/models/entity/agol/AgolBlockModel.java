package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolBlockEntity;
import com.wadoo.hyperion.common.entities.agol.AgolFlamerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolBlockModel extends DefaultedEntityGeoModel<AgolBlockEntity> {
    public AgolBlockModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_block"));
    }

    @Override
    public RenderType getRenderType(AgolBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
