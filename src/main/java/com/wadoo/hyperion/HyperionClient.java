package com.wadoo.hyperion;

import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class HyperionClient {
        public static void clientEvents() {
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            forgeBus.addListener(HyperionClient::onSetupCamera);
            modEventBus.addListener(HyperionClient::onClientSetup);
        }

    //Credit to Bob Mowzie
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if (player != null) {
            if (!Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (CameraShakeEntity cameraShake : player.level.getEntitiesOfClass(CameraShakeEntity.class, player.getBoundingBox().inflate(20, 20, 20))) {
                    if (cameraShake.distanceTo(player) < cameraShake.getRadius()) {
                        shakeAmplitude += cameraShake.getShakeAmount(player, delta);
                    }
                }
                if (shakeAmplitude > 1.0f) shakeAmplitude = 1.0f;
                event.setPitch((float) (event.getPitch() + shakeAmplitude * Math.cos(ticksExistedDelta * 3 + 2) * 25));
                event.setYaw((float) (event.getYaw() + shakeAmplitude * Math.cos(ticksExistedDelta * 5 + 1) * 25));
                event.setRoll((float) (event.getRoll() + shakeAmplitude * Math.cos(ticksExistedDelta * 4) * 25));
            }
        }
    }

    public static void onClientSetup(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            itemProperties();
        });
    }

    private static void itemProperties() {
        //Credit to Telepathic Grunt
        ItemProperties.register(
                ItemHandler.BURROWING_SLICER.get(),
                new ResourceLocation("blocking"),
                (itemStack, world, livingEntity, integer) ->
                        livingEntity != null &&
                                livingEntity.isUsingItem() &&
                                livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F
        );
    }
}
