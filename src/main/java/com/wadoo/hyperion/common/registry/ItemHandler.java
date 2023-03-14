package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.GigaHammerItem;
import com.wadoo.hyperion.common.items.HyperionMobBucket;
import com.wadoo.hyperion.common.items.VolatileGoopItem;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class ItemHandler {

    public static CreativeModeTab HYPERION_TAB;

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hyperion.MODID);

    public static final RegistryObject<Item> AGRALITE_SHEET = ITEMS.register("agralite_sheet", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final RegistryObject<Item> AGRALITE_CAGE = ITEMS.register("agralite_cage", () -> new BlockItem(BlockHandler.AGRALITE_CAGE.get(), new Item.Properties()));
    public static final RegistryObject<Item> AGRALITE_BLOCK = ITEMS.register("agralite_block", () -> new BlockItem(BlockHandler.AGRALITE_BLOCK.get(), new Item.Properties()));
    public static final RegistryObject<Item> CUT_AGRALITE = ITEMS.register("cut_agralite", () -> new BlockItem(BlockHandler.CUT_AGRALITE.get(), new Item.Properties()));
    public static final RegistryObject<Item> AGRALITE_PIPE = ITEMS.register("agralite_pipe", () -> new BlockItem(BlockHandler.AGRALITE_PIPE.get(), new Item.Properties()));

    public static final RegistryObject<Item> SPIRE_BRICKS = ITEMS.register("basalt_spire_bricks", () -> new BlockItem(BlockHandler.SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CUT_SPIRE_BRICKS = ITEMS.register("cut_basalt_spire_bricks", () -> new BlockItem(BlockHandler.CUT_SPIRE_BRICKS.get(), new Item.Properties()));
    public static final RegistryObject<Item> CHECKERED_SPIRE_BRICKS = ITEMS.register("checkered_spire_bricks", () -> new BlockItem(BlockHandler.CHECKERED_SPIRE_BRICKS.get(), new Item.Properties()));



    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new HyperionMobBucket(EntityHandler.CAPSLING, Fluids.LAVA, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties()));

    public static final RegistryObject<Item> GRUSK_SPAWN_EGG = ITEMS.register("grusk_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.GRUSK, 0x575757, 0x141210, new Item.Properties()));

    public static final RegistryObject<Item> CRUCIBLE_SPAWN_EGG = ITEMS.register("crucible_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CRUCIBLE, 0x4d494c, 0x1b2632, new Item.Properties()));

    public static final RegistryObject<VolatileGoopItem> VOLATILE_GOOP = ITEMS.register("volatile_goop",() -> new VolatileGoopItem((new Item.Properties()).stacksTo(16).fireResistant()));
    public static final RegistryObject<GigaHammerItem> GIGA_HAMMER = ITEMS.register("giga_hammer",() -> new GigaHammerItem((new Item.Properties()).defaultDurability(1300).fireResistant().rarity(Rarity.RARE)));
    public static final List<RegistryObject<? extends Item>> HYPERION_ITEMS = List.of(
        AGRALITE_CAGE,AGRALITE_SHEET,AGRALITE_BLOCK,CUT_AGRALITE,SPIRE_BRICKS,CUT_SPIRE_BRICKS,CHECKERED_SPIRE_BRICKS,CAPSLING_BUCKET,VOLATILE_GOOP,CAPSLING_SPAWN_EGG,GRUSK_SPAWN_EGG,CRUCIBLE_SPAWN_EGG, GIGA_HAMMER
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
