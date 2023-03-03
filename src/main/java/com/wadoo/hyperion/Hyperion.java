package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.BlockHandler;
import com.wadoo.hyperion.common.registry.EntityHandler;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hyperion.MODID)
public class Hyperion {
    public static final String MODID = "hyperion";
    private static final Logger LOGGER = LogUtils.getLogger();


    public Hyperion() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        MinecraftForge.EVENT_BUS.register(this);

        EntityHandler.ENTITIES.register(bus);
        ItemHandler.ITEMS.register(bus);
        BlockHandler.BLOCKS.register(bus);
        BlockEntityHandler.BLOCK_ENTITIES.register(bus);

        bus.addListener(EventPriority.NORMAL, ItemHandler::registerCreativeModeTab);
        bus.addListener(this::registerEntityAttributes);
    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK.get(), GruskEntity.createAttributes().build());

    }

}