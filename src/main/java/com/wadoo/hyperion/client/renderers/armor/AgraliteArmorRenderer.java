package com.wadoo.hyperion.client.renderers.armor;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.AgraliteArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class AgraliteArmorRenderer extends GeoArmorRenderer<AgraliteArmorItem> {
    public AgraliteArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Hyperion.MODID, "armor/agralite_armor")));
    }
}