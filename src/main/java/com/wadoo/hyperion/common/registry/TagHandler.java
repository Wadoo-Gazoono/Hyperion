package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public final class TagHandler {
    public static void initTags() {}

    public static TagKey<Structure> REMOVE_BASALT = TagKey.create(Registries.STRUCTURE,
            new ResourceLocation(Hyperion.MODID, "remove_basalt"));

    public static TagKey<Block> AGRALITE_PIPE_HEAT_SOURCE = TagKey.create(Registries.BLOCK,
            new ResourceLocation(Hyperion.MODID, "mineshaft_support_replaceables"));
}