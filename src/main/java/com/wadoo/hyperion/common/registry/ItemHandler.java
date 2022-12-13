package com.wadoo.hyperion.common.registry;

import com.wadoo.hyperion.Hyperion;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemHandler {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Hyperion.MODID);

    public static final RegistryObject<Item> AGRALITE_SHEET = ITEMS.register("agralite_sheet", () -> new Item(new Item.Properties().tab(Hyperion.GROUP).stacksTo(64).fireResistant()));
    public static final RegistryObject<Item> AGRALITE_CAGE = ITEMS.register("agralite_cage", () -> new BlockItem(BlockHandler.AGRALITE_CAGE.get(), new Item.Properties().tab(Hyperion.GROUP)));
    public static final RegistryObject<Item> CAPSLING_BUCKET = ITEMS.register("capsling_bucket",() -> new MobBucketItem(EntityHandler.CAPSLING.get(), Fluids.LAVA,SoundEvents.BUCKET_EMPTY_FISH, (new Item.Properties()).stacksTo(1).tab(CreativeModeTab.TAB_MISC)));

    public static final RegistryObject<Item> CAPSLING_SPAWN_EGG = ITEMS.register("capsling_spawn_egg", () -> new ForgeSpawnEggItem(EntityHandler.CAPSLING, 0x737170, 0x515054, new Item.Properties().tab(Hyperion.GROUP)));

}
