package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ItemHandler {

    public static CreativeModeTab HYPERION_TAB;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hyperion.MODID);

    public static final RegistryObject<Item> AGRALITE_SHEET = ITEMS.register("agralite_sheet", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
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
    public static final RegistryObject<BlockItem> CUT_SPIRE_BRICKS = ITEMS.register("cut_basalt_spire_bricks", () -> new BlockItem(BlockHandler.CUT_SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> CHECKERED_SPIRE_BRICKS = ITEMS.register("checkered_spire_bricks", () -> new BlockItem(BlockHandler.CHECKERED_SPIRE_BRICKS.get(), new Item.Properties()));

    public static final RegistryObject<BlockItem> KILN = ITEMS.register("kiln", () -> new BlockItem(BlockHandler.KILN.get(), new Item.Properties()));
    public static final RegistryObject<BlockItem> GRIMSPIRE_DOOR = ITEMS.register("grimspire_door", () -> new BlockItem(BlockHandler.GRIMSPIRE_DOOR.get(), new Item.Properties()));


    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new HyperionMobBucket(EntityHandler.CAPSLING, Fluids.LAVA, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties()));

    public static final RegistryObject<Item> GRUSK_SPAWN_EGG = ITEMS.register("grusk_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.GRUSK, 0x575757, 0x141210, new Item.Properties()));
    public static final RegistryObject<Item> CRUCIBLE_SPAWN_EGG = ITEMS.register("crucible_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CRUCIBLE, 0x4d494c, 0x1b2632, new Item.Properties()));
    public static final RegistryObject<Item> AMD_SPAWN_EGG = ITEMS.register("auto_mining_droid_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.AUTOMININGDROID, 0x3a3b48, 0x794934, new Item.Properties()));

    public static final RegistryObject<VolatileGoopItem> VOLATILE_GOOP = ITEMS.register("volatile_goop",() -> new VolatileGoopItem((new Item.Properties()).stacksTo(16).fireResistant()));
    public static final RegistryObject<ZweihanderItem> ZWEIHANDER = ITEMS.register("zweihander",() -> new ZweihanderItem(HyperionTiers.AGRALITE, 3, -2.4F, new Item.Properties()));




    public static final RegistryObject<AgraliteArmorItem> AGRALITE_HELMET = ITEMS.register("agralite_armet",
            () -> new AgraliteArmorItem(HyperionArmourMaterials.AGRALITE, EquipmentSlot.HEAD, new Item.Properties()));
    public static final RegistryObject<AgraliteArmorItem> AGRALITE_CHESTPLATE = ITEMS.register("agralite_cuirass",
            () -> new AgraliteArmorItem(HyperionArmourMaterials.AGRALITE, EquipmentSlot.CHEST, new Item.Properties()));
    public static final RegistryObject<AgraliteArmorItem> AGRALITE_LEGGINGS = ITEMS.register("agralite_greaves",
            () -> new AgraliteArmorItem(HyperionArmourMaterials.AGRALITE, EquipmentSlot.LEGS, new Item.Properties()));
    public static final RegistryObject<AgraliteArmorItem> AGRALITE_BOOTS = ITEMS.register("agralite_sabatons",
            () -> new AgraliteArmorItem(HyperionArmourMaterials.AGRALITE, EquipmentSlot.FEET, new Item.Properties()));





    public static final List<RegistryObject<? extends Item>> HYPERION_ITEMS = List.of(
        AGRALITE_CAGE,AGRALITE_PIPE,AGRALITE_LAMP,AGRALITE_CHAIN,AGRALITE_SHEET,AGRALITE_BLOCK,AGRALITE_SLAB,AGRALITE_STAIRS,
            CUT_AGRALITE,CUT_AGRALITE_STAIRS,
            CUT_AGRALITE_SLAB,SPIRE_BRICKS, CUT_SPIRE_BRICKS,CHECKERED_SPIRE_BRICKS,
            KILN, CAPSLING_BUCKET,VOLATILE_GOOP,
            CAPSLING_SPAWN_EGG,GRUSK_SPAWN_EGG,CRUCIBLE_SPAWN_EGG,AMD_SPAWN_EGG, ZWEIHANDER,
            AGRALITE_HELMET,AGRALITE_CHESTPLATE,AGRALITE_LEGGINGS,AGRALITE_BOOTS
    );

    public static void registerCreativeModeTab(CreativeModeTabEvent.Register event) {
        HYPERION_TAB = event.registerCreativeModeTab(new ResourceLocation(Hyperion.MODID, "tab"),
                builder -> builder.icon(() -> AGRALITE_SHEET.get().getDefaultInstance())
                        .title(Component.translatable("itemGroup." + Hyperion.MODID + ".tab"))
                        .withLabelColor(0x333333)
                        .displayItems((features, output, hasPermissions) ->
                                output.acceptAll(HYPERION_ITEMS.stream().map(item -> item.get().getDefaultInstance()).toList())
                        )
        );

    }
}
