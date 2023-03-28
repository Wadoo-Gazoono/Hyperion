package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockHandler {

    //TODO, make agralite minable properly
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hyperion.MODID);
    public static final RegistryObject<Block> AGRALITE_CAGE = BLOCKS.register("agralite_cage", () -> new AgraliteCageBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL).noOcclusion()));



    public static final RegistryObject<Block> CUT_AGRALITE = BLOCKS.register("cut_agralite", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> CUT_AGRALITE_SLAB = BLOCKS.register("cut_agralite_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> CUT_AGRALITE_STAIRS = BLOCKS.register("cut_agralite_stairs", () -> new StairBlock(CUT_AGRALITE.get().defaultBlockState(), BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));

    public static final RegistryObject<Block> AGRALITE_BLOCK = BLOCKS.register("agralite_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_STAIRS = BLOCKS.register("agralite_stairs", () -> new StairBlock(AGRALITE_BLOCK.get().defaultBlockState(),BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_SLAB = BLOCKS.register("agralite_slab", () -> new SlabBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));

    public static final RegistryObject<Block> BASALT_WALL = BLOCKS.register("basalt_wall", () -> new WallBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> POLISHED_BASALT_WALL = BLOCKS.register("polished_basalt_wall", () -> new WallBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));



    public static final RegistryObject<Block> AGRALITE_PIPE = BLOCKS.register("agralite_pipe", () -> new AgralitePipeBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> AGRALITE_LAMP = BLOCKS.register("agralite_lamp", () -> new AgraliteLamp(BlockBehaviour.Properties.copy(Blocks.LANTERN).lightLevel(level -> {
        return 12;
    }).noOcclusion()));
    public static final RegistryObject<Block> AGRALITE_CHAIN = BLOCKS.register("agralite_chain", () -> new ChainBlock(BlockBehaviour.Properties.copy(Blocks.CHAIN).randomTicks().noOcclusion()));


    public static final RegistryObject<RotatedPillarBlock> SPIRE_BRICKS = BLOCKS.register("basalt_spire_bricks", () -> new RotatedPillarBlock(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistryObject<Block> CUT_SPIRE_BRICKS = BLOCKS.register("cut_basalt_spire_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.POLISHED_BASALT)));
    public static final RegistryObject<Block> CUT_SPIRE_BRICK_STAIRS = BLOCKS.register("cut_spire_brick_stairs", () -> new StairBlock(CUT_SPIRE_BRICKS.get().defaultBlockState(),BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistryObject<Block> CUT_SPIRE_BRICK_SLAB = BLOCKS.register("cut_spire_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistryObject<Block> CUT_SPIRE_BRICK_WALL = BLOCKS.register("cut_spire_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    //Fix UVS
    public static final RegistryObject<Block> CHECKERED_SPIRE_BRICKS = BLOCKS.register("checkered_spire_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.POLISHED_BASALT)));

    public static final RegistryObject<Block> KILN = BLOCKS.register("kiln", () -> new KilnBlock(BlockBehaviour.Properties.copy(Blocks.BLAST_FURNACE).randomTicks().noOcclusion()));

}
