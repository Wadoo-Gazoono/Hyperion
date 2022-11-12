package com.wadoo.hyperion.client;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.renderers.CapslingRenderer;
import com.wadoo.hyperion.client.renderers.GruskRenderer;
import com.wadoo.hyperion.client.renderers.blocks.AgraliteCageRenderer;
import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.BlockHandler;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.example.registry.TileRegistry;


@Mod.EventBusSubscriber(modid = Hyperion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEvents {



    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityHandler.CAPSLING.get(), CapslingRenderer::new);
        event.registerEntityRenderer(EntityHandler.GRUSK.get(), GruskRenderer::new);
        event.registerBlockEntityRenderer(BlockEntityHandler.AGRALITE_CAGE.get(), AgraliteCageRenderer::new);

    }
}
