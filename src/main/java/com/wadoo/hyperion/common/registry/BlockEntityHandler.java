package com.wadoo.hyperion.common.registry;

import com.mojang.datafixers.types.Type;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityHandler {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<BlockEntityType<AgraliteCageBlockEntity>> AGRALITE_CAGE = BLOCK_ENTITIES.register("agralite_cage", () -> BlockEntityType.Builder.of(AgraliteCageBlockEntity::new, BlockHandler.AGRALITE_CAGE.get()).build(null));



}
