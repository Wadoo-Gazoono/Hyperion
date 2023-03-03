package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.AgraliteCageBlock;
import com.wadoo.hyperion.common.blocks.AgralitePipeBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockHandler {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hyperion.MODID);
    public static final RegistryObject<Block> AGRALITE_CAGE = BLOCKS.register("agralite_cage", () -> new AgraliteCageBlock(BlockBehaviour.Properties.of(Material.METAL).strength(30.0F, 15.0F).randomTicks().sound(SoundType.METAL).noOcclusion()));
    public static final RegistryObject<Block> CUT_AGRALITE = BLOCKS.register("cut_agralite", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(50.0f, 15.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_BLOCK = BLOCKS.register("agralite_block", () -> new Block(BlockBehaviour.Properties.of(Material.METAL).strength(50.0f, 15.0f).randomTicks().sound(SoundType.METAL)));
    public static final RegistryObject<Block> AGRALITE_PIPE = BLOCKS.register("agralite_pipe", () -> new AgralitePipeBlock(BlockBehaviour.Properties.of(Material.METAL).strength(50.0f, 15.0f).randomTicks().sound(SoundType.METAL).noOcclusion()));

}
