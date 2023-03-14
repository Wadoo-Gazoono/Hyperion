package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TagHandler {
    public static void registerTags() {}

    public static TagKey<Structure> REMOVE_BASALT = TagKey.create(Registries.STRUCTURE,
            new ResourceLocation(Hyperion.MODID, "remove_basalt"));
}
