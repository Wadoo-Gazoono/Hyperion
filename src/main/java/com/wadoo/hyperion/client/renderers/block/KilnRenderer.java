package com.wadoo.hyperion.client.renderers.block;



import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.wadoo.hyperion.client.models.block.KilnModel;
import com.wadoo.hyperion.common.blocks.entities.KilnBlockEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class KilnRenderer extends GeoBlockRenderer<KilnBlockEntity> {


    public KilnRenderer() {
        super(new KilnModel());
    }

    @Override
    public boolean shouldRender(KilnBlockEntity p_173568_, Vec3 p_173569_) {
        return true;
    }
}
