package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.AgraliteCageBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockHandler {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Hyperion.MODID);
    public static final RegistryObject<Block> AGRALITE_CAGE = BLOCKS.register("agralite_cage", () -> new AgraliteCageBlock(BlockBehaviour.Properties.of(Material.METAL).strength(30.0F, 15.0F).randomTicks().sound(SoundType.METAL)));

}
