package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolConnectorT;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolConnectorTModel extends DefaultedEntityGeoModel<AgolConnectorT> {
    public AgolConnectorTModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_connector_t"));
    }
}
