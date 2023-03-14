package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Hyperion.MODID);

    public static final RegistryObject<SoundEvent> GRUSK_AMBIENCE = SOUNDS.register("grusk.ambient",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hyperion.MODID, "grusk.ambient")));
    public static final RegistryObject<SoundEvent> GRUSK_HURT = SOUNDS.register("grusk.damage",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hyperion.MODID, "grusk.damage")));
    public static final RegistryObject<SoundEvent> GRUSK_DEATH = SOUNDS.register("grusk.death",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hyperion.MODID, "grusk.death")));

}