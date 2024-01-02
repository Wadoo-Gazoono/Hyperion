package com.wadoo.hyperion.common.entities.clinker;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class ClinkerAttackAI extends Goal{
    private final ClinkerEntity clinker;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;



    public ClinkerAttackAI(ClinkerEntity clinker) {
        this.clinker = clinker;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.clinker.getTarget();
        return target != null && target.isAlive() && this.clinker.getAnimation() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.clinker.getTarget();
        return target != null && target.isAlive() && this.clinker.getAnimation() == 0 ;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.clinker.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.clinker.getTarget();
        if (target == null) return;
        double distSqr = this.clinker.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        this.clinker.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.clinker.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.clinker.getRandom().nextInt(7);
            if (distSqr > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (distSqr > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.clinker.getNavigation().moveTo(target, 1.0D)) {
                this.repath += 15;
            }
        }
        distSqr = this.clinker.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        double dist = Math.sqrt(this.clinker.distanceToSqr(this.targetX, this.targetY, this.targetZ));

        if (target.getY() - this.clinker.getY() >= -1 && target.getY() - this.clinker.getY() <= 3) {
            //Attack Checker
            if(dist < 2f){
                this.clinker.setAnimation(1);
            }


        }
    }
}

