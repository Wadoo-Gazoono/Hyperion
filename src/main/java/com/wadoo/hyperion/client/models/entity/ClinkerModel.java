package com.wadoo.hyperion.client.models.entity;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.clinker.ClinkerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ClinkerModel extends DefaultedEntityGeoModel<ClinkerEntity> {
    public ClinkerModel() {
        super(new ResourceLocation(Hyperion.MODID, "cinder"));
    }


}
