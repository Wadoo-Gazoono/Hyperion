package com.wadoo.hyperion.client;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.client.network.Keybindings;
import com.wadoo.hyperion.common.network.AgolActionClientPacket;
import com.wadoo.hyperion.common.network.NetworkHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hyperion.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ForgeEventBusSubcriber {
    private static final Component EXAMPLE_KEY_PRESSED =
            Component.literal("Key pressed");


    @SubscribeEvent
    public static void clientTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();

        if(Keybindings.INSTANCE.AGOL_INPUT_1.consumeClick() && minecraft.player != null) {
            NetworkHandler.sendToServer(new AgolActionClientPacket((byte)1));
        }

        if(Keybindings.INSTANCE.AGOL_INPUT_2.consumeClick() && minecraft.player != null) {
            NetworkHandler.sendToServer(new AgolActionClientPacket((byte)2));
        }

        if(Keybindings.INSTANCE.AGOL_INPUT_3.consumeClick() && minecraft.player != null) {
            NetworkHandler.sendToServer(new AgolActionClientPacket((byte)3));
        }

        if(Keybindings.INSTANCE.AGOL_INPUT_4.consumeClick() && minecraft.player != null) {
            NetworkHandler.sendToServer(new AgolActionClientPacket((byte)4));
        }

        if(Keybindings.INSTANCE.AGOL_INPUT_5.consumeClick() && minecraft.player != null) {
            NetworkHandler.sendToServer(new AgolActionClientPacket((byte)5));
        }
    }
}
