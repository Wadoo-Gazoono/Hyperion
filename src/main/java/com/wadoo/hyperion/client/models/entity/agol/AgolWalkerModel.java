package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolWalkerModel extends DefaultedEntityGeoModel<AgolWalker> {
    public AgolWalkerModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_walker"));
    }
}
