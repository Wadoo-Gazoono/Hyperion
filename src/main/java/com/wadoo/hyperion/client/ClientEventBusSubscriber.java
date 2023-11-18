package com.wadoo.hyperion.client;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.renderers.block.AgraliteCageRenderer;
import com.wadoo.hyperion.client.renderers.block.GrimSpireDoorRenderer;
import com.wadoo.hyperion.client.renderers.block.KilnRenderer;
import com.wadoo.hyperion.client.renderers.entity.*;
import com.wadoo.hyperion.client.renderers.entity.agol.*;
import com.wadoo.hyperion.client.renderers.entity.effect.BasaltSpikeRenderer;
//import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.client.renderers.entity.effect.ChainTestRenderer;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.entities.agol.AgolWalker;
import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolMenu;
import com.wadoo.hyperion.common.inventory.menu.agol.AbstractAgolScreen;
import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.ContainerHandler;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Hyperion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityHandler.CAPSLING.get(), CapslingRenderer::new);
        event.registerEntityRenderer(EntityHandler.GRUSK.get(), GruskRenderer::new);
        event.registerEntityRenderer(EntityHandler.GRUSK_HEAD.get(), GruskHeadRenderer::new);
        event.registerEntityRenderer(EntityHandler.CRUCIBLE.get(), CrucibleRenderer::new);
        event.registerEntityRenderer(EntityHandler.FORGENAUT.get(), ForgenautRenderer::new);
        event.registerEntityRenderer(EntityHandler.FEDRAN.get(), FedranRenderer::new);

        event.registerEntityRenderer(EntityHandler.AGOL_BASE.get(), AbstractAgolRenderer::new);
        event.registerEntityRenderer(EntityHandler.AGOL_WALKER.get(), AgolWalkerRenderer::new);
        event.registerEntityRenderer(EntityHandler.AGOL_CONNECTOR_T.get(), AgolConnectorTRenderer::new);
        event.registerEntityRenderer(EntityHandler.AGOL_HEAD.get(), AgolHeadRenderer::new);
        event.registerEntityRenderer(EntityHandler.AGOL_PLACEHOLDER.get(), RenderNothing::new);


        event.registerEntityRenderer(EntityHandler.VOLATILE_GOOP.get(), VolatileGoopRenderer::new);
        event.registerEntityRenderer(EntityHandler.BASALT_SPIKE.get(), BasaltSpikeRenderer::new);
        event.registerEntityRenderer(EntityHandler.CHAIN_TEST.get(), ChainTestRenderer::new);

        event.registerEntityRenderer(EntityHandler.CAMERA.get(), RenderNothing::new);

        event.registerBlockEntityRenderer(BlockEntityHandler.AGRALITE_CAGE.get(), context -> new AgraliteCageRenderer());
        event.registerBlockEntityRenderer(BlockEntityHandler.KILN.get(), context -> new KilnRenderer());
        event.registerBlockEntityRenderer(BlockEntityHandler.GRIMSPIRE_DOOR.get(), context -> new GrimSpireDoorRenderer());

    }



}
