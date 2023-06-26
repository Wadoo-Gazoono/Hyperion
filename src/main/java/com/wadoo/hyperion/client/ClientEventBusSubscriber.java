package com.wadoo.hyperion.client;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.renderers.block.AgraliteCageRenderer;
import com.wadoo.hyperion.client.renderers.block.GrimSpireDoorRenderer;
import com.wadoo.hyperion.client.renderers.block.KilnRenderer;
import com.wadoo.hyperion.client.renderers.entity.*;
import com.wadoo.hyperion.client.renderers.entity.effect.BasaltSpikeRenderer;
//import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.BlockEntityHandler;
import com.wadoo.hyperion.common.registry.EntityHandler;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = Hyperion.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventBusSubscriber {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityHandler.CAPSLING.get(), CapslingRenderer::new);
        event.registerEntityRenderer(EntityHandler.GRUSK.get(), GruskRenderer::new);
        event.registerEntityRenderer(EntityHandler.GRUSK_HEAD.get(), GruskHeadRenderer::new);
        event.registerEntityRenderer(EntityHandler.CRUCIBLE.get(), CrucibleRenderer::new);
        event.registerEntityRenderer(EntityHandler.AUTOMININGDROID.get(), AutoMiningDroidRenderer::new);
        event.registerEntityRenderer(EntityHandler.FEDRAN.get(), FedranRenderer::new);


        event.registerEntityRenderer(EntityHandler.VOLATILE_GOOP.get(), VolatileGoopRenderer::new);
        event.registerEntityRenderer(EntityHandler.BASALT_SPIKE.get(), BasaltSpikeRenderer::new);

        event.registerEntityRenderer(EntityHandler.CAMERA.get(), RenderNothing::new);

        event.registerBlockEntityRenderer(BlockEntityHandler.AGRALITE_CAGE.get(), context -> new AgraliteCageRenderer());
        event.registerBlockEntityRenderer(BlockEntityHandler.KILN.get(), context -> new KilnRenderer());
        event.registerBlockEntityRenderer(BlockEntityHandler.GRIMSPIRE_DOOR.get(), context -> new GrimSpireDoorRenderer());

    }




}
