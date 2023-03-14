package com.wadoo.hyperion.client.renderers.item;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.VolatileGoopItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class VolatileGoopItemRenderer extends GeoItemRenderer<VolatileGoopItem> {
    public VolatileGoopItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(Hyperion.MODID, "volatile_goop")));
    }
}