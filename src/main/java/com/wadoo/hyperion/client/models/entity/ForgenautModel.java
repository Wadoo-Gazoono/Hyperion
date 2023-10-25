package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ForgenautModel extends DefaultedEntityGeoModel<ForgenautEntity> {
    public ForgenautModel() {
        super(new ResourceLocation(Hyperion.MODID, "forgenaut"));
    }
}
