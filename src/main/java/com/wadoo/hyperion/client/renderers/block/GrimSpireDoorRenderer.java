package com.wadoo.hyperion.client.renderers.block;



import com.wadoo.hyperion.client.models.block.GrimSpireDoorModel;
import com.wadoo.hyperion.client.models.block.KilnModel;
import com.wadoo.hyperion.common.blocks.entities.GrimSpireDoorBlockEntity;
import com.wadoo.hyperion.common.blocks.entities.KilnBlockEntity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GrimSpireDoorRenderer extends GeoBlockRenderer<GrimSpireDoorBlockEntity> {


    public GrimSpireDoorRenderer() {
        super(new GrimSpireDoorModel());
    }

    @Override
    public boolean shouldRender(GrimSpireDoorBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }

    @Override
    public boolean shouldRenderOffScreen(GrimSpireDoorBlockEntity p_112306_) {
        return true;
    }
}
