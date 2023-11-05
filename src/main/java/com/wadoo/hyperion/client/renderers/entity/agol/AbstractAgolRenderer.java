package com.wadoo.hyperion.client.renderers.entity.agol;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.models.entity.CapslingModel;
import com.wadoo.hyperion.client.models.entity.agol.AbstractAgolModel;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.agol.AbstractAgolEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;


public class AbstractAgolRenderer extends GeoEntityRenderer<AbstractAgolEntity> {
    public AbstractAgolRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AbstractAgolModel());
        this.shadowRadius = 1F;
    }
}