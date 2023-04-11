package com.arathain.vigorem.test;

import com.arathain.vigorem.api.Keyframe;
import com.arathain.vigorem.api.anim.MirrorableAnimation;
import com.wadoo.hyperion.Hyperion;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Map;

public class SmashAnimation extends MirrorableAnimation {
    public SmashAnimation(int length, Map<String, List<Keyframe>> keyframes, boolean right) {
        super(length, keyframes, right);
    }
    @Override
    public ResourceLocation getId() {
        return Hyperion.id(mirrored ? "smash_left" : "smash_right");
    }

    @Override
    public float getMovementMultiplier() {
        if(this.frame > 9 && this.frame < 24) {
            return 0f;
        }
        return 0.02f;
    }

    @Override
    public boolean lockHeldItem() {
        return true;
    }

    @Override
    public boolean isBlockingMovement() {
        return true;
    }

    @Override
    public void clientTick(Player player) {
        super.clientTick(player);
        if(this.frame > 10 && this.frame < 15) {
            player.setYBodyRot(player.yHeadRot);
        }
        if(this.frame == 15) {
            Vec3 vec = new Vec3(player.getForward().x, 0, player.getForward().z).multiply(2,2,2);
            player.getLevel().playSound(player, player.getX() + vec.x, player.getY(), player.getZ() + vec.z,SoundEvents.ANVIL_FALL,SoundSource.PLAYERS,0.4f,0.8f);
        }
    }

    @Override
    public void serverTick(Player player) {
        super.serverTick(player);
        if(this.frame > 10 && this.frame < 15) {
            player.setYBodyRot(player.yHeadRot);
        }
        if(this.frame == 15) {
            Vec3 vec = new Vec3(player.getLookAngle().x, 0, player.getLookAngle().z).normalize().multiply(2,2,2);
            player.getLevel().getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().move(vec).inflate(0.5, 0.4, 0.5), livingEntity -> livingEntity.isAlive() && !livingEntity.isInvulnerable()).forEach(entity -> {
                entity.hurt(DamageSource.playerAttack(player), (float) (9.0f + player.getAttributeValue(Attributes.ATTACK_DAMAGE)));
                entity.setDeltaMovement((player.getRandom().nextFloat() - 0.5f) * 0.2f, 0.3, (player.getRandom().nextFloat() - 0.5f) * 0.2f);
                if(player.getLevel().getBlockState(player.blockPosition().offset(Mth.floor(vec.x), Mth.floor(vec.y), Mth.floor(vec.z))).getBlock().defaultDestroyTime() == 0) {
                    player.getLevel().destroyBlock(player.blockPosition().offset(Mth.floor(vec.x), Mth.floor(vec.y), Mth.floor(vec.z)), true, player);
                }
                if(player.getLevel().getBlockState(player.blockPosition().offset(Mth.floor(vec.x), Mth.floor(vec.y - 1), Mth.floor(vec.z))).getBlock().defaultDestroyTime() == 0) {
                    player.getLevel().destroyBlock(player.blockPosition().offset(Mth.floor(vec.x), Mth.floor(vec.y - 1), Mth.floor(vec.z)), true, player);
                }
            });
        }
    }
}