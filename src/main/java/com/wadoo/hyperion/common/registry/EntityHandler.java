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
import com.wadoo.hyperion.common.entities.obelisk.ObeliskEntity;
import com.wadoo.hyperion.common.entities.projectiles.VolatileGoopProjectile;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import software.bernie.geckolib.renderer.GeoObjectRenderer;

public class EntityHandler {
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Hyperion.MODID);

    public static final RegistryObject<EntityType<CapslingEntity>> CAPSLING = create("capsling", EntityType.Builder.of(CapslingEntity::new, MobCategory.CREATURE).sized(0.45f, 1.0f).fireImmune());
    public static final RegistryObject<EntityType<CrucibleEntity>> CRUCIBLE = create("crucible", EntityType.Builder.of(CrucibleEntity::new, MobCategory.MONSTER).sized(2.0f, 2.0f).fireImmune());
    public static final RegistryObject<EntityType<GruskEntity>> GRUSK = create("grusk", EntityType.Builder.of(GruskEntity::new, MobCategory.MONSTER).sized(1f, 1.6f).fireImmune());
    public static final RegistryObject<EntityType<GruskHeadEntity>> GRUSK_HEAD = create("grusk_head", EntityType.Builder.of(GruskHeadEntity::new, MobCategory.MONSTER).sized(0.8f, 0.6f).fireImmune());
    public static final RegistryObject<EntityType<FedranEntity>> FEDRAN = create("fedran", EntityType.Builder.of(FedranEntity::new, MobCategory.MONSTER).sized(1.1f, 4.8f).fireImmune());
    public static final RegistryObject<EntityType<ObeliskEntity>> OBELISK = create("obelisk", EntityType.Builder.of(ObeliskEntity::new, MobCategory.MONSTER).sized(1f, 2.5f).fireImmune());

    public static final RegistryObject<EntityType<ForgenautEntity>> FORGENAUT = create("forgenaut", EntityType.Builder.of(ForgenautEntity::new, MobCategory.MONSTER).sized(1.7f, 3.0f).fireImmune());

    //AGOLS
    public static final RegistryObject<EntityType<AbstractAgolEntity>> AGOL_BASE = create("agol_base", EntityType.Builder.<AbstractAgolEntity>of(AbstractAgolEntity::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolWalker>> AGOL_WALKER = create("agol_walker", EntityType.Builder.<AgolWalker>of(AgolWalker::new, MobCategory.MISC).sized(1f, 2f).fireImmune());
    public static final RegistryObject<EntityType<AgolConnectorT>> AGOL_CONNECTOR_T = create("agol_connector_t", EntityType.Builder.<AgolConnectorT>of(AgolConnectorT::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolHead>> AGOL_HEAD = create("agol_head", EntityType.Builder.<AgolHead>of(AgolHead::new, MobCategory.MISC).sized(1f, 1f).fireImmune());
    public static final RegistryObject<EntityType<AgolShooter>> AGOL_SHOOTER = create("agol_shooter", EntityType.Builder.<AgolShooter>of(AgolShooter::new, MobCategory.MISC).sized(1f, 1f).fireImmune());


    public static final RegistryObject<EntityType<VolatileGoopProjectile>> VOLATILE_GOOP = create("volatile_goop", EntityType.Builder.<VolatileGoopProjectile>of(VolatileGoopProjectile::new, MobCategory.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10));
    public static final RegistryObject<EntityType<CameraShakeEntity>> CAMERA = create("camera_shake", EntityType.Builder.<CameraShakeEntity>of(CameraShakeEntity::new, MobCategory.MISC).sized(1F, 1F).updateInterval(Integer.MAX_VALUE));
    public static final RegistryObject<EntityType<BasaltSpikeEntity>> BASALT_SPIKE = create("basalt_spike", EntityType.Builder.<BasaltSpikeEntity>of(BasaltSpikeEntity::new, MobCategory.MISC).sized(1F, 2.5F));
    public static final RegistryObject<EntityType<ChainTestEntity>> CHAIN_TEST = create("chain_test", EntityType.Builder.<ChainTestEntity>of(ChainTestEntity::new, MobCategory.MISC).sized(0.2F, 2.0F));

    private static <T extends Entity> RegistryObject<EntityType<T>> create(String name, EntityType.Builder<T> builder) {
        return ENTITIES.register(name, () -> builder.build(Hyperion.MODID + "." + name));
    }


}
