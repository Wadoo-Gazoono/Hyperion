package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundsRegistry {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Hyperion.MODID);

    private static RegistryObject<SoundEvent> register(String path) {
        return SOUNDS.register(path, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hyperion.MODID, path)));
    }

    public static final RegistryObject<SoundEvent> CAPSLING_IDLE = register("entity.capsling.idle");
    public static final RegistryObject<SoundEvent> CAPSLING_IDLE_PLEAD = register("entity.capsling.idle.plead");
    public static final RegistryObject<SoundEvent> CAPSLING_HURT = register("entity.capsling.hurt");
    public static final RegistryObject<SoundEvent> CAPSLING_DEATH = register("entity.capsling.death");
    public static final RegistryObject<SoundEvent> CAPSLING_BITE = register("entity.capsling.bite");
    public static final RegistryObject<SoundEvent> CAPSLING_CHEW = register("entity.capsling.chew");
    public static final RegistryObject<SoundEvent> CAPSLING_SPIT = register("entity.capsling.spit");
    public static final RegistryObject<SoundEvent> CAPSLING_INTO_BUCKET = register("entity.capsling.into_bucket");

    public static final RegistryObject<SoundEvent> GRUSK_IDLE = register("entity.grusk.idle");
    public static final RegistryObject<SoundEvent> GRUSK_HURT = register("entity.grusk.hurt");
    public static final RegistryObject<SoundEvent> GRUSK_DEATH = register("entity.grusk.death");
    public static final RegistryObject<SoundEvent> GRUSK_ROAR = register("entity.grusk.roar");
    public static final RegistryObject<SoundEvent> GRUSK_EAT = register("entity.grusk.eat");
    public static final RegistryObject<SoundEvent> GRUSK_DECAPITATE = register("entity.grusk.decapitate");

    public static final RegistryObject<SoundEvent> CRUCIBLE_IDLE = register("entity.crucible.idle");
    public static final RegistryObject<SoundEvent> CRUCIBLE_HURT = register("entity.crucible.hurt");
    public static final RegistryObject<SoundEvent> CRUCIBLE_DEATH = register("entity.crucible.death");
    public static final RegistryObject<SoundEvent> CRUCIBLE_THROW = register("entity.crucible.throw");
    public static final RegistryObject<SoundEvent> CRUCIBLE_SLAM = register("entity.crucible.slam");

    public static final RegistryObject<SoundEvent> FORGENAUT_IDLE = register("entity.forgenaut.idle");
    public static final RegistryObject<SoundEvent> FORGENAUT_HURT = register("entity.forgenaut.hurt");
    public static final RegistryObject<SoundEvent> FORGENAUT_DEATH = register("entity.forgenaut.death");
    public static final RegistryObject<SoundEvent> FORGENAUT_STARTUP = register("entity.forgenaut.startup");
    public static final RegistryObject<SoundEvent> FORGENAUT_STUN = register("entity.forgenaut.stun");
    public static final RegistryObject<SoundEvent> FORGENAUT_ATTACK_PUNCH = register("entity.forgenaut.attack.punch");
    public static final RegistryObject<SoundEvent> FORGENAUT_ATTACK_SLAM = register("entity.forgenaut.attack.slam");
    public static final RegistryObject<SoundEvent> FORGENAUT_ATTACK_ROCKET = register("entity.forgenaut.attack.rocket");

    public static final RegistryObject<SoundEvent> FEDRAN_IDLE = register("entity.fedran.idle");
    public static final RegistryObject<SoundEvent> FEDRAN_IDLE_PIPES = register("entity.fedran.idle.pipes");
    public static final RegistryObject<SoundEvent> FEDRAN_IDLE_CORE = register("entity.fedran.idle.core");
    public static final RegistryObject<SoundEvent> FEDRAN_HURT = register("entity.fedran.hurt");
    public static final RegistryObject<SoundEvent> FEDRAN_DEATH = register("entity.fedran.death");
    public static final RegistryObject<SoundEvent> FEDRAN_ATTACK_SLAM = register("entity.fedran.attack.slam");
    public static final RegistryObject<SoundEvent> FEDRAN_ATTACK_SWIPE = register("entity.fedran.attack.swipe");
    public static final RegistryObject<SoundEvent> FEDRAN_ATTACK_CHOOCHOO = register("entity.fedran.attack.choochoo");
    public static final RegistryObject<SoundEvent> FEDRAN_STOMP = register("entity.fedran.stomp");
    public static final RegistryObject<SoundEvent> FEDRAN_PHASE_CHANGE = register("entity.fedran.phase_change");

    public static final RegistryObject<SoundEvent> PARRY = SOUNDS.register("weapon.parry",
            () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Hyperion.MODID, "weapon.parry")));
}