package com.wadoo.hyperion.client.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.wadoo.hyperion.client.models.entity.CapslingModel;
import com.wadoo.hyperion.client.models.entity.FedranModel;
import com.wadoo.hyperion.client.models.entity.GruskModel;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.BlockAndItemGeoLayer;


public class FedranRenderer extends GeoEntityRenderer<FedranEntity> {

    public FedranRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FedranModel());
        this.shadowRadius = 1.6F;
    }
}