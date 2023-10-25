package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.*;
import com.wadoo.hyperion.common.entities.agol.*;
import com.wadoo.hyperion.common.entities.effects.BasaltSpikeEntity;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.entities.effects.ChainTestEntity;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
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
    public static final RegistryObject<EntityType<CrucibleEntity>> CRUCIBLE = create("crucible", EntityType.Builder.of(CrucibleEntity::new, MobCategory.MONSTER).sized(2.0f, 2.0f).fireImmune());
    public static final RegistryObject<EntityType<GruskEntity>> GRUSK = create("grusk", EntityType.Builder.of(GruskEntity::new, MobCategory.MONSTER).sized(1f, 1.6f).fireImmune());
    public static final RegistryObject<EntityType<GruskHeadEntity>> GRUSK_HEAD = create("grusk_head", EntityType.Builder.of(GruskHeadEntity::new, MobCategory.MONSTER).sized(0.8f, 0.6f).fireImmune());
    public static final RegistryObject<EntityType<FedranEntity>> FEDRAN = create("fedran", EntityType.Builder.of(FedranEntity::new, MobCategory.MONSTER).sized(1.1f, 4.8f).fireImmune());

    public static final RegistryObject<EntityType<ForgenautEntity>> FORGENAUT = create("forgenaut", EntityType.Builder.of(ForgenautEntity::new, MobCategory.MONSTER).sized(1.7f, 3.0f).fireImmune());

    //AGOLS
    public static final RegistryObject<EntityType<AgolWalkerEntity>> AGOL_CORE_WALKER = create("agol_core_walker", EntityType.Builder.of(AgolWalkerEntity::new, MobCategory.MISC).sized(1f, 1.1f).fireImmune());
    public static final RegistryObject<EntityType<AgolModuleEntity>> AGOL_MODULE = create("agol_module", EntityType.Builder.of(AgolModuleEntity::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolSpeakerEntity>> AGOL_SPEAKER = create("agol_speaker", EntityType.Builder.of(AgolSpeakerEntity::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolPerfumerEntity>> AGOL_PERFUMER = create("agol_perfumer", EntityType.Builder.of(AgolPerfumerEntity::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolConnectorEntity>> AGOL_CONNECTOR = create("agol_connector", EntityType.Builder.of(AgolConnectorEntity::new, MobCategory.MISC).sized(1.6f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolSeatEntity>> AGOL_SEAT = create("agol_seat", EntityType.Builder.of(AgolSeatEntity::new, MobCategory.MISC).sized(1.6f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolFlamerEntity>> AGOL_FLAMER = create("agol_flamer", EntityType.Builder.of(AgolFlamerEntity::new, MobCategory.MISC).sized(1.6f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolPlatformEntity>> AGOL_PLATFORM = create("agol_platform", EntityType.Builder.of(AgolPlatformEntity::new, MobCategory.MISC).sized(3f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolBlockEntity>> AGOL_BLOCK = create("agol_block", EntityType.Builder.of(AgolBlockEntity::new, MobCategory.MISC).sized(1f, 1f).fireImmune());


    public static final RegistryObject<EntityType<VolatileGoopProjectile>> VOLATILE_GOOP = create("volatile_goop", EntityType.Builder.<VolatileGoopProjectile>of(VolatileGoopProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<CameraShakeEntity>> CAMERA = create("camera_shake", EntityType.Builder.<CameraShakeEntity>of(CameraShakeEntity::new, MobCategory.MISC).sized(1F, 1F).updateInterval(Integer.MAX_VALUE));
    public static final RegistryObject<EntityType<BasaltSpikeEntity>> BASALT_SPIKE = create("basalt_spike", EntityType.Builder.<BasaltSpikeEntity>of(BasaltSpikeEntity::new, MobCategory.MISC).sized(1F, 2.5F));
    public static final RegistryObject<EntityType<ChainTestEntity>> CHAIN_TEST = create("chain_test", EntityType.Builder.<ChainTestEntity>of(ChainTestEntity::new, MobCategory.MISC).sized(0.2F, 2.0F));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Hyperion.MODID + "." + name));
    }
}
