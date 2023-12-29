package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

import static com.wadoo.hyperion.common.registry.CreativeTabHandler.addToTab;

public class ItemHandler {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hyperion.MODID);

    public static final RegistryObject<Item> AGRALITE_SHEET = addToTab(ITEMS.register("agralite_sheet", () -> new Item(new Item.Properties().stacksTo(64).fireResistant())));
    public static final RegistryObject<Item> AGRALITE_CAGE = ITEMS.register("agralite_cage", () -> new BlockItem(BlockHandler.AGRALITE_CAGE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> AGRALITE_BLOCK = ITEMS.register("agralite_block", () -> new BlockItem(BlockHandler.AGRALITE_BLOCK.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> AGRALITE_SLAB = ITEMS.register("agralite_slab", () -> new BlockItem(BlockHandler.AGRALITE_SLAB.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> AGRALITE_STAIRS = ITEMS.register("agralite_stairs", () -> new BlockItem(BlockHandler.AGRALITE_STAIRS.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> CUT_AGRALITE = ITEMS.register("cut_agralite", () -> new BlockItem(BlockHandler.CUT_AGRALITE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CUT_AGRALITE_STAIRS = ITEMS.register("cut_agralite_stairs", () -> new BlockItem(BlockHandler.CUT_AGRALITE_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CUT_AGRALITE_SLAB = ITEMS.register("cut_agralite_slab", () -> new BlockItem(BlockHandler.CUT_AGRALITE_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> AGRALITE_PIPE = ITEMS.register("agralite_pipe", () -> new BlockItem(BlockHandler.AGRALITE_PIPE.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> AGRALITE_CHAIN = ITEMS.register("agralite_chain", () -> new BlockItem(BlockHandler.AGRALITE_CHAIN.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> AGRALITE_LAMP = ITEMS.register("agralite_lamp", () -> new BlockItem(BlockHandler.AGRALITE_LAMP.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> SPIRE_BRICKS = ITEMS.register("basalt_spire_bricks", () -> new BlockItem(BlockHandler.SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CRACKED_SPIRE_BRICKS = ITEMS.register("cracked_basalt_spire_bricks", () -> new BlockItem(BlockHandler.CRACKED_SPIRE_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> CUT_SPIRE_BRICKS = ITEMS.register("cut_basalt_spire_bricks", () -> new BlockItem(BlockHandler.CUT_SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CUT_SPIRE_BRICK_STAIRS = ITEMS.register("cut_spire_brick_stairs", () -> new BlockItem(BlockHandler.CUT_SPIRE_BRICK_STAIRS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CUT_SPIRE_BRICK_SLAB = ITEMS.register("cut_spire_brick_slab", () -> new BlockItem(BlockHandler.CUT_SPIRE_BRICK_SLAB.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> CHISELED_CUT_SPIRE_BRICKS = ITEMS.register("chiseled_cut_basalt_spire_bricks", () -> new BlockItem(BlockHandler.CHISELED_CUT_SPIRE_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> CHECKERED_SPIRE_BRICKS = ITEMS.register("checkered_spire_bricks", () -> new BlockItem(BlockHandler.CHECKERED_SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SPIRE_BRICK_PILLAR = ITEMS.register("basalt_spire_brick_pillar", () -> new BlockItem(BlockHandler.SPIRE_BRICK_PILLAR.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> SPIRE_BRICK_PINNACLE = ITEMS.register("basalt_spire_brick_pinnacle", () -> new BlockItem(BlockHandler.SPIRE_BRICK_PINNACLE.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> KILN = ITEMS.register("kiln", () -> new BlockItem(BlockHandler.KILN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> GRIMSPIRE_DOOR = ITEMS.register("grimspire_door", () -> new BlockItem(BlockHandler.GRIMSPIRE_DOOR.get(), new Item.Properties()));


    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new HyperionMobBucket(EntityHandler.CAPSLING, Fluids.LAVA, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties()));

    public static final RegistryObject<Item> GRUSK_SPAWN_EGG = ITEMS.register("grusk_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.GRUSK, 0x575757, 0x141210, new Item.Properties()));
    public static final RegistryObject<Item> CRUCIBLE_SPAWN_EGG = ITEMS.register("crucible_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CRUCIBLE, 0x4d494c, 0x1b2632, new Item.Properties()));
    public static final RegistryObject<Item> FORGENAUT_SPAWN_EGG = ITEMS.register("forgenaut_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.FORGENAUT, 0x3a3b48, 0x794934, new Item.Properties()));
    public static final RegistryObject<Item> FEDRAN_SPAWN_EGG = ITEMS.register("fedran_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.FEDRAN, 0x3f3f43, 0x3f303b, new Item.Properties()));

    public static final RegistryObject<VolatileGoopItem> VOLATILE_GOOP = ITEMS.register("volatile_goop",() -> new VolatileGoopItem((new Item.Properties()).stacksTo(16).fireResistant()));

    public static final RegistryObject<ModuleItem> AGOL_WALKER_MODULE = ITEMS.register("agol_walker_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_WALKER));
    public static final RegistryObject<ModuleItem> AGOL_BASE_MODULE = ITEMS.register("agol_base_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_BASE));
    public static final RegistryObject<ModuleItem> AGOL_HEAD_MODULE = ITEMS.register("agol_head_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_HEAD));
    public static final RegistryObject<ModuleItem> AGOL_SHOOTER_MODULE = ITEMS.register("agol_shooter_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_SHOOTER));

    //    public static final RegistryObject<ModuleItem> AGOL_PERFUMER_MODULE = ITEMS.register("agol_perfumer_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_PERFUMER));
//    public static final RegistryObject<ModuleItem> AGOL_PLATFORM_MODULE = ITEMS.register("agol_platform_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_PLATFORM));
    public static final RegistryObject<ModuleItem> AGOL_CONNECTOR_T = ITEMS.register("agol_connector_t",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_CONNECTOR_T));
//    public static final RegistryObject<ModuleItem> AGOL_CONNECTOR_MODULE = ITEMS.register("agol_connector_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_CONNECTOR));
//    public static final RegistryObject<ModuleItem> AGOL_SEAT_MODULE = ITEMS.register("agol_seat_module",() -> new ModuleItem((new Item.Properties()).rarity(Rarity.UNCOMMON).stacksTo(16).fireResistant(),EntityHandler.AGOL_SEAT));



    public static final List<RegistryObject<? extends Item>> HYPERION_ITEMS = List.of(
            AGRALITE_CAGE,AGRALITE_PIPE,AGRALITE_LAMP,AGRALITE_CHAIN,AGRALITE_SHEET,
            AGRALITE_BLOCK,AGRALITE_SLAB,AGRALITE_STAIRS, CUT_AGRALITE,CUT_AGRALITE_STAIRS,
            CUT_AGRALITE_SLAB,SPIRE_BRICKS,CRACKED_SPIRE_BRICKS, CUT_SPIRE_BRICKS,
            CHISELED_CUT_SPIRE_BRICKS,CUT_SPIRE_BRICK_STAIRS, CUT_SPIRE_BRICK_SLAB,
            SPIRE_BRICK_PILLAR,SPIRE_BRICK_PINNACLE, CHECKERED_SPIRE_BRICKS, KILN,
            CAPSLING_BUCKET,VOLATILE_GOOP, CAPSLING_SPAWN_EGG,GRUSK_SPAWN_EGG,
            CRUCIBLE_SPAWN_EGG,FORGENAUT_SPAWN_EGG, FEDRAN_SPAWN_EGG,
            AGOL_WALKER_MODULE, AGOL_BASE_MODULE ,AGOL_CONNECTOR_T, AGOL_HEAD_MODULE, AGOL_SHOOTER_MODULE
            //AGOL_PERFUMER_MODULE, AGOL_FLAMER_MODULE, AGOL_CONNECTOR_MODULE,
            //AGOL_PLATFORM_MODULE
    );

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeTabHandler.HYPERION_TAB.getKey()) {
            event.accept((Supplier<? extends ItemLike>) HYPERION_ITEMS.stream().map(item -> item.get().getDefaultInstance()).toList());
        }
    }
}
