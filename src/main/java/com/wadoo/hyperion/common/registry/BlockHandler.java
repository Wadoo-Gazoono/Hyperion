package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.AgraliteCageBlock;
import com.wadoo.hyperion.common.blocks.AgralitePipeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockHandler {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hyperion.MODID);
    public static final RegistryObject<Block> AGRALITE_CAGE = BLOCKS.register("agralite_cage", () -> new AgraliteCageBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> CUT_AGRALITE = BLOCKS.register("cut_agralite", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_BLOCK = BLOCKS.register("agralite_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_PIPE = BLOCKS.register("agralite_pipe", () -> new AgralitePipeBlock(BlockBehaviour.Properties.of(Material.METAL).requiresCorrectToolForDrops().strength(8.0f, 5.0f).randomTicks().sound(SoundType.METAL).noOcclusion()));

    public static final RegistryObject<Block> SPIRE_BRICKS = BLOCKS.register("basalt_spire_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistryObject<Block> CUT_SPIRE_BRICKS = BLOCKS.register("cut_basalt_spire_bricks", () -> new Block(BlockBehaviour.Properties.copy(Blocks.BASALT)));
    public static final RegistryObject<Block> CHECKERED_SPIRE_BRICKS = BLOCKS.register("checkered_spire_bricks", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(2.0f, 3.0f).randomTicks().sound(SoundType.METAL)));

}
