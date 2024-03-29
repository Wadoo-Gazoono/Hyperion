package com.wadoo.hyperion.common.entities.forgenaut;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.item.BowItem;

import java.util.EnumSet;

public class ForgenautAttackAI extends Goal{
    private final ForgenautEntity forgenaut;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;



    public ForgenautAttackAI(ForgenautEntity forgenaut) {
        this.forgenaut = forgenaut;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.forgenaut.getTarget();
        return target != null && target.isAlive() && this.forgenaut.getAnimation() == 0 && this.forgenaut.getTransition() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.forgenaut.getTarget();
        return target != null && target.isAlive() && this.forgenaut.getAnimation() == 0  && this.forgenaut.getTransition() == 0;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.forgenaut.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.forgenaut.getTarget();
        if (target == null) return;

        double distSqr = this.forgenaut.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        double dist = Math.sqrt(this.forgenaut.distanceToSqr(this.targetX, this.targetY, this.targetZ));

        if(forgenaut.getPhase() == 0){
            if (forgenaut.distanceTo(forgenaut.getTarget()  ) < 13f || forgenaut.getHealth() < forgenaut.getMaxHealth()) {
                forgenaut.setAnimation(1);
            }
            return;
        }



        this.forgenaut.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.forgenaut.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.forgenaut.getRandom().nextInt(7);
            if (distSqr > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (distSqr > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.forgenaut.getNavigation().moveTo(target, 0.5D)) {
                this.repath += 15;
            }
        }

        dist = forgenaut.distanceTo(forgenaut.getTarget());

        if (dist < 3f) this.forgenaut.getNavigation().stop();
        if (target.getY() - this.forgenaut.getY() >= -1 && target.getY() - this.forgenaut.getY() <= 3) {
            if (this.forgenaut.getAttackCooldown() > 0){
                this.forgenaut.setAttackCooldown(this.forgenaut.getAttackCooldown() - 1);
                return;
            }
            //Attack Checker
            if (this.forgenaut.getPhase() != 0) {
                if (dist < 3f){
                    float rand = forgenaut.getRandom().nextFloat();
                    if (rand < 0.8){
                        this.forgenaut.setAnimation(this.forgenaut.getRandom().nextFloat() < 0.5f ? 2 : 4);
                    }
                    else {
                        this.forgenaut.setAnimation(9);
                    }
                }
                if (dist > 15f && dist < 25f){this.forgenaut.setAnimation(this.forgenaut.getRandom().nextFloat() < 0.6f ? 7 : 8);}
                if (dist > 25f){this.forgenaut.setAnimation(8);}
            }
        }
    }
}

