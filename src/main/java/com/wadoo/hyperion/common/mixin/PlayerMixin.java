package com.wadoo.hyperion.common.mixin;


import com.wadoo.hyperion.Hyperion;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Player.class)
public abstract class PlayerMixin {
    @Inject(method = "drop(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/world/entity/item/ItemEntity;", at = @At("HEAD"))
    public void drop(ItemStack p_36177_, boolean p_36178_, CallbackInfoReturnable<ItemEntity> cir){
        Player player = (Player)(Object)this;
        Hyperion.LOGGER.info(player.getDisplayName().getString());
        System.out.println("hi");
        player.kill();

    }
}