package com.wadoo.hyperion.common.registry;

import com.mojang.serialization.Codec;
import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.world.worldgen.structure.GrimspireStructure;
import com.wadoo.hyperion.common.world.worldgen.structure.RefineryStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class StructureHandler {
    public static final DeferredRegister<StructureType<?>> STRUCTURES = DeferredRegister.create(Registries.STRUCTURE_TYPE, Hyperion.MODID);

    public static final RegistryObject<StructureType<RefineryStructure>> REFINERY = STRUCTURES.register("refinery", () -> explicitStructureTypeTyping(RefineryStructure.CODEC));
    public static final RegistryObject<StructureType<GrimspireStructure>> GRIMSPIRE = STRUCTURES.register("grimspire_main_floor", () -> explicitStructureTypeTyping(GrimspireStructure.CODEC));

    private static <T extends Structure> StructureType<T> explicitStructureTypeTyping(Codec<T> structureCodec) {
        return () -> structureCodec;
    }
}
