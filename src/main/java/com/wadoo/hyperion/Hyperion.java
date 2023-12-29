package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.agol.*;
import com.wadoo.hyperion.common.entities.fedran.FedranEntity;
import com.wadoo.hyperion.common.entities.forgenaut.ForgenautEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import com.wadoo.hyperion.common.entities.obelisk.ObeliskEntity;
import com.wadoo.hyperion.common.network.NetworkHandler;
import com.wadoo.hyperion.common.registry.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Strider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import javax.annotation.Syntax;
import java.util.Set;
import java.util.UUID;
//TODO FIX KILNS DATA SAVING
//TODO FIX GRIMSPIRE LOG SPAM
@Mod(Hyperion.MODID)
public class Hyperion {
    public static final String MODID = "hyperion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Hyperion() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(this::commonSetup);
        EntityHandler.ENTITIES.register(bus);
        ContainerHandler.MENU_TYPES.register(bus);
        ItemHandler.ITEMS.register(bus);
        BlockHandler.BLOCKS.register(bus);
        CreativeTabHandler.TABS.register(bus);
        BlockEntityHandler.BLOCK_ENTITIES.register(bus);
        SoundsRegistry.SOUNDS.register(bus);
        StructureHandler.STRUCTURES.register(bus);
        ParticleHandler.PARTICLES.register(bus);
        TagHandler.initTags();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HyperionClient::clientEvents);

        bus.addListener(this::registerEntityAttributes);
    }


    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK.get(), GruskEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK_HEAD.get(), GruskHeadEntity.createAttributes().build());
        event.put(EntityHandler.CRUCIBLE.get(), CrucibleEntity.createAttributes().build());
        event.put(EntityHandler.OBELISK.get(), ObeliskEntity.createAttributes().build());

        event.put(EntityHandler.FORGENAUT.get(), ForgenautEntity.createAttributes().build());
        event.put(EntityHandler.FEDRAN.get(), FedranEntity.createAttributes().build());


        event.put(EntityHandler.AGOL_BASE.get(), AbstractAgolEntity.createAttributes().build());
        event.put(EntityHandler.AGOL_CONNECTOR_T.get(), AgolConnectorT.createAttributes().build());
        event.put(EntityHandler.AGOL_HEAD.get(), AgolHead.createAttributes().build());
        event.put(EntityHandler.AGOL_SHOOTER.get(), AgolHead.createAttributes().build());

        event.put(EntityHandler.AGOL_WALKER.get(), AgolWalker.createAttributes().build());

    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            NetworkHandler.init();
        });
    }

    @SubscribeEvent
    public void spawns(SpawnPlacementRegisterEvent event){
        event.register(EntityHandler.CAPSLING.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.OR);
    }
}