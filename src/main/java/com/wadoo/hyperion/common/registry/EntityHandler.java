package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.*;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<EntityType<CapslingEntity>> CAPSLING = create("capsling", EntityType.Builder.of(CapslingEntity::new, MobCategory.CREATURE).sized(0.45f, 1.0f).fireImmune());
    public static final RegistryObject<EntityType<GruskEntity>> GRUSK = create("grusk", EntityType.Builder.of(GruskEntity::new, MobCategory.MONSTER).sized(0.95f, 2.0f).fireImmune());
    public static final RegistryObject<EntityType<GruskHeadEntity>> GRUSK_HEAD = create("grusk_head", EntityType.Builder.of(GruskHeadEntity::new, MobCategory.MONSTER).sized(0.75f, 0.8f).fireImmune());
    public static final RegistryObject<EntityType<CrucibleEntity>> CRUCIBLE = create("crucible", EntityType.Builder.of(CrucibleEntity::new, MobCategory.MONSTER).sized(2.0f, 2.0f).fireImmune());
    public static final RegistryObject<EntityType<AutoMiningDroidEntity>> AUTOMININGDROID = create("auto_mining_droid", EntityType.Builder.of(AutoMiningDroidEntity::new, MobCategory.MONSTER).sized(1.9f, 3.0f).fireImmune());


    public static final RegistryObject<EntityType<VolatileGoopProjectile>> VOLATILE_GOOP = create("volatile_goop", EntityType.Builder.<VolatileGoopProjectile>of(VolatileGoopProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<CameraShakeEntity>> CAMERA = create("camera_shake", EntityType.Builder.<CameraShakeEntity>of(CameraShakeEntity::new, MobCategory.MISC).sized(1F, 1F).updateInterval(Integer.MAX_VALUE));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Hyperion.MODID + "." + name));
    }
}
