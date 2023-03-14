package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.client.ClientEvents;
import com.wadoo.hyperion.common.entities.CapslingEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.entities.GruskEntity;
import com.wadoo.hyperion.common.entities.GruskHeadEntity;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.registry.*;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        SoundRegistry.SOUNDS.register(bus);
        TagHandler.registerTags();
        bus.addListener(EventPriority.NORMAL, ItemHandler::registerCreativeModeTab);
        bus.addListener(this::registerEntityAttributes);
        MinecraftForge.EVENT_BUS.addListener(this::onSetupCamera);


    }

    private void registerEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(EntityHandler.CAPSLING.get(), CapslingEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK.get(), GruskEntity.createAttributes().build());
        event.put(EntityHandler.GRUSK_HEAD.get(), GruskHeadEntity.createAttributes().build());
        event.put(EntityHandler.CRUCIBLE.get(), CrucibleEntity.createAttributes().build());

    }

    @OnlyIn(Dist.CLIENT)
    public void onSetupCamera(ViewportEvent.ComputeCameraAngles event) {
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

}