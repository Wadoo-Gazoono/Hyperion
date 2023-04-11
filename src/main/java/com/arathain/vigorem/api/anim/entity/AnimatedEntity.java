package com.arathain.vigorem.api.anim.entity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

import java.util.Map;
import java.util.function.Supplier;

public interface AnimatedEntity {
    <T extends Entity & AnimatedEntity> Map<ResourceLocation, Supplier<EntityAnimation<T>>> getAnimations();
    <T extends Entity & AnimatedEntity> ModelPart getPart(String string, EntityModel<T> model);

    CompoundTag writeAnimNbt(CompoundTag nbt);
    void readAnimNbt(CompoundTag nbt);
}