package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.common.entities.AutoMiningDroidEntity;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskEntity;
import com.wadoo.hyperion.common.entities.grusk.GruskHeadEntity;
import com.wadoo.hyperion.common.registry.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Set;
import java.util.UUID;

@Mod(Hyperion.MODID)
public class Hyperion {
    public static final String MODID = "hyperion";
    public static final Logger LOGGER = LogUtils.getLogger();
    private static final UUID AGRALITE_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier AGRALITE_ARMOUR_MODIFIER = new AttributeModifier(AGRALITE_ARMOR_MODIFIER_UUID, "Weapon modifier", 3.0D, AttributeModifier.Operation.ADDITION);

    public Hyperion() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        EntityHandler.ENTITIES.register(bus);
        ItemHandler.ITEMS.register(bus);
        BlockHandler.BLOCKS.register(bus);
        BlockEntityHandler.BLOCK_ENTITIES.register(bus);
        SoundsRegistry.SOUNDS.register(bus);
        StructureHandler.STRUCTURES.register(bus);
        ParticleHandler.PARTICLES.register(bus);
        TagHandler.initTags();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> HyperionClient::clientEvents);

        bus.addListener(EventPriority.NORMAL, ItemHandler::registerCreativeModeTab);
        bus.addListener(this::registerEntityAttributes);
        bus.addListener(this::entitySpawnRestriction);


    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK.get(), GruskEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK_HEAD.get(), GruskHeadEntity.createAttributes().build());
        event.put(EntityHandler.CRUCIBLE.get(), CrucibleEntity.createAttributes().build());
        event.put(EntityHandler.AUTOMININGDROID.get(), AutoMiningDroidEntity.createAttributes().build());

    }


    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        //TODO Move to Common Events class
        Player player = event.player;
        Set<Item> armor = new ObjectOpenHashSet<>();
        for (ItemStack stack : player.getArmorSlots()) {
            armor.add(stack.getItem());
        }
        boolean wearingAgralite = armor.containsAll(ObjectArrayList.of(
                ItemHandler.AGRALITE_BOOTS.get(),
                ItemHandler.AGRALITE_CHESTPLATE.get(),
                ItemHandler.AGRALITE_HELMET.get(),
                ItemHandler.AGRALITE_LEGGINGS.get()));

        if (wearingAgralite) {
            if (player.getAttribute(Attributes.ATTACK_SPEED).getModifier(AGRALITE_ARMOR_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(AGRALITE_ARMOR_MODIFIER_UUID, "Weapon modifier", 0.08d, AttributeModifier.Operation.ADDITION));
            }
        }
        else{
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(AGRALITE_ARMOR_MODIFIER_UUID);
        }
    }


    public void entitySpawnRestriction(SpawnPlacementRegisterEvent event) {
        event.register(EntityHandler.CAPSLING.get(), SpawnPlacements.Type.IN_LAVA, Heightmap.Types.MOTION_BLOCKING,
                CapslingEntity::checkMonsterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }


}