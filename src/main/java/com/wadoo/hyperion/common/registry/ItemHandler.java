package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.items.HyperionMobBucket;
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
    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new HyperionMobBucket(EntityHandler.CAPSLING, Fluids.LAVA, (new Item.Properties()).stacksTo(1)));
    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties()));

    public static final List<RegistryObject<? extends Item>> HYPERION_ITEMS = List.of(
        AGRALITE_CAGE,AGRALITE_SHEET,CAPSLING_SPAWN_EGG,CAPSLING_BUCKET
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
