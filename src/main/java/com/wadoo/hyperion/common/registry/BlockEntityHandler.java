package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import com.wadoo.hyperion.common.blocks.entities.KilnBlockEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlastFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityHandler {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<BlockEntityType<AgraliteCageBlockEntity>> AGRALITE_CAGE = BLOCK_ENTITIES.register("agralite_cage", () -> BlockEntityType.Builder.of(AgraliteCageBlockEntity::new, BlockHandler.AGRALITE_CAGE.get()).build(null));
    public static final RegistryObject<BlockEntityType<KilnBlockEntity>> KILN = BLOCK_ENTITIES.register("kiln", () -> BlockEntityType.Builder.of(KilnBlockEntity::new, BlockHandler.KILN.get()).build(null));

}
