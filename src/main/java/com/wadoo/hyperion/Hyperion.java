package com.wadoo.hyperion;

import com.mojang.logging.LogUtils;
import com.wadoo.hyperion.common.entities.*;
import com.wadoo.hyperion.common.entities.effects.CameraShakeEntity;
import com.wadoo.hyperion.common.registry.*;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.living.ShieldBlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Set;
import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Hyperion.MODID)
public class Hyperion {
    public static final String MODID = "hyperion";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final UUID AGRALITE_ARMOR_MODIFIER_UUID = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF27F");
    private static final AttributeModifier AGRALITE_ARMOUR_MODIFIER = new AttributeModifier(AGRALITE_ARMOR_MODIFIER_UUID, "Weapon modifier", 3.0D, AttributeModifier.Operation.ADDITION);

    public Hyperion() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);

        EntityHandler.ENTITIES.register(bus);
        ItemHandler.ITEMS.register(bus);
        BlockHandler.BLOCKS.register(bus);
        BlockEntityHandler.BLOCK_ENTITIES.register(bus);
        SoundsRegistry.SOUNDS.register(bus);
        StructureHandler.STRUCTURES.register(bus);



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
        event.put(EntityHandler.AUTOMININGDROID.get(), AutoMiningDroidEntity.createAttributes().build());

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

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {

        //Agralite armor buff
        Player player = event.player;
        Set<Item> armor = new ObjectOpenHashSet<>();
        for (ItemStack stack : player.getArmorSlots()) {
            armor.add(stack.getItem());
        }
        boolean wearingAgralite = armor.containsAll(ObjectArrayList.of(
                ItemHandler.AGRALITE_BOOTS.get(),
                ItemHandler.AGRALITE_CHESTPLATE.get(),
                ItemHandler.AGRALITE_HELMET.get(),
                ItemHandler.AGRALITE_LEGGINGS.get()));

        if (wearingAgralite) {
            if (player.getAttribute(Attributes.ATTACK_SPEED).getModifier(AGRALITE_ARMOR_MODIFIER_UUID) == null) {
                player.getAttribute(Attributes.ATTACK_SPEED).addTransientModifier(new AttributeModifier(AGRALITE_ARMOR_MODIFIER_UUID, "Weapon modifier", 0.08d, AttributeModifier.Operation.ADDITION));
            }
        }
        else{
            player.getAttribute(Attributes.ATTACK_SPEED).removeModifier(AGRALITE_ARMOR_MODIFIER_UUID);
        }
        if(player.getMainHandItem().is(ItemHandler.ZWEIHANDER.get())){
            if(player.isBlocking() && player.hurtTime > 0 && 72000 - player.getUseItemRemainingTicks() < 30){
                player.playSound(SoundsRegistry.PARRY.get());
                player.addEffect(new MobEffectInstance(MobEffects.GLOWING, 50, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 40, 1));

            }
        }


    }
    @SubscribeEvent
    public void onShieldBlock(ShieldBlockEvent event){
        System.out.println("blocked");
        if(event.getEntity().getOffhandItem().is(ItemHandler.ZWEIHANDER.get())||event.getEntity().getMainHandItem().is(ItemHandler.ZWEIHANDER.get())){
            event.setCanceled(true);
        }
    }

}