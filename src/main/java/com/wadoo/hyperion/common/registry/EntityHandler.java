package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<EntityType<CapslingEntity>> CAPSLING = create("capsling", EntityType.Builder.of(CapslingEntity::new, MobCategory.CREATURE).sized(0.45f, 1.0f).fireImmune());

    public static final RegistryObject<EntityType<GruskEntity>> GRUSK = create("grusk", EntityType.Builder.of(GruskEntity::new, MobCategory.MONSTER).sized(1.0f, 1.7f).fireImmune());
    public static final RegistryObject<EntityType<GruskHeadEntity>> GRUSK_HEAD = create("grusk_head", EntityType.Builder.of(GruskHeadEntity::new, MobCategory.MONSTER).sized(0.5f, 0.6f).fireImmune());

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Hyperion.MODID + "." + name));
    }
}
