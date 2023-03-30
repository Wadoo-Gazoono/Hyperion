package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.particles.AgraliteFlameParticle;
import com.wadoo.hyperion.client.particles.KilnFlameParticle;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Hyperion.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleHandler {

    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister
            .create(ForgeRegistries.PARTICLE_TYPES, Hyperion.MODID);

    public static final RegistryObject<SimpleParticleType> AGRALITE_FLAME = PARTICLES.register("agralite_flame",
            () -> new SimpleParticleType(true));

    public static final RegistryObject<SimpleParticleType> KILN_FLAME = PARTICLES.register("kiln_flame",
            () -> new SimpleParticleType(true));

    @SubscribeEvent
    public static void registry(RegisterParticleProvidersEvent event) {
        event.register(AGRALITE_FLAME.get(), AgraliteFlameParticle.Factory::new);
        event.register(KILN_FLAME.get(), KilnFlameParticle.Factory::new);
    }

}