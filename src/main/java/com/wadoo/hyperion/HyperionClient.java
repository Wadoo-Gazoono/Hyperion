package com.wadoo.hyperion;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.registry.ItemHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class HyperionClient {
    public static final ResourceLocation BOSS_BAR_OVERLAY_LOCATION = new ResourceLocation(Hyperion.MODID, "textures/gui/bossbar_overlay.png");
    public static final ResourceLocation BOSS_BAR_LOCATION = new ResourceLocation(Hyperion.MODID, "textures/gui/bossbar.png");
    public static GuiGraphics graphics;

    public static void clientEvents() {
            IEventBus forgeBus = MinecraftForge.EVENT_BUS;
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

            forgeBus.addListener(HyperionClient::onSetupCamera);
        forgeBus.addListener(HyperionClient::setGraphics);
        forgeBus.addListener(HyperionClient::bossBar);
        }

    //Credit to Bob Mowzie
    public static void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
        Player player = Minecraft.getInstance().player;
        float delta = Minecraft.getInstance().getFrameTime();
        float ticksExistedDelta = player.tickCount + delta;
        if (player != null) {
            if (!Minecraft.getInstance().isPaused()) {
                float shakeAmplitude = 0;
                for (CameraShakeEntity cameraShake : player.level().getEntitiesOfClass(CameraShakeEntity.class, player.getBoundingBox().inflate(20, 20, 20))) {
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



    private static void setGraphics(RenderGuiEvent.Pre event){
        graphics = event.getGuiGraphics();
    }

    private static void bossBar(CustomizeGuiOverlayEvent.BossEventProgress event){
        if (event.getBossEvent().getName().getContents().toString().contains("entity.hyperion.fedran") && graphics != null){
            event.setCanceled(true);
            int y = event.getY() + 2;
            int i = Minecraft.getInstance().getWindow().getGuiScaledWidth();
            int j = 12;
            int k = i / 2 - 91;
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShaderTexture(0,BOSS_BAR_LOCATION);
            drawBar(graphics, event.getX() - 4, y - 1, event.getBossEvent());
            Component component = event.getBossEvent().getName().copy().withStyle(ChatFormatting.GOLD);
            int l = Minecraft.getInstance().font.width(component);
            int i1 = i / 2 - l / 2;
            int j1 = y - 12;

            Minecraft.getInstance().getProfiler().push("coolerBossBar");
            RenderSystem.setShaderTexture(0,BOSS_BAR_OVERLAY_LOCATION);
            graphics.blit(BOSS_BAR_LOCATION,event.getX()-14,y-14,0,0,195,26,195,26);
            Minecraft.getInstance().getProfiler().pop();

            graphics.drawString(Minecraft.getInstance().font, component, i1, j1, 16777215);
        }
    }


    private static void drawBar(GuiGraphics p_283672_, int p_283570_, int p_283306_, BossEvent p_283156_) {
        drawBar(p_283672_, p_283570_, p_283306_, p_283156_, 182, 0);
        int i = (int)(p_283156_.getProgress() * 183.0F);
        if (i > 0) {
            drawBar(p_283672_, p_283570_, p_283306_, p_283156_, i, 5);
        }

    }

    private static void drawBar(GuiGraphics p_281657_, int p_283675_, int p_282498_, BossEvent p_281288_, int p_283619_, int p_281636_) {
        p_281657_.blit(BOSS_BAR_LOCATION, p_283675_, p_282498_, 0, p_281288_.getColor().ordinal() * 5 * 2 + p_281636_, p_283619_, 5);
        if (p_281288_.getOverlay() != BossEvent.BossBarOverlay.PROGRESS) {
            RenderSystem.enableBlend();
            p_281657_.blit(BOSS_BAR_LOCATION, p_283675_, p_282498_, 0, 80 + (p_281288_.getOverlay().ordinal() - 1) * 5 * 2 + p_281636_, p_283619_, 5);
            RenderSystem.disableBlend();
        }

    }
}
