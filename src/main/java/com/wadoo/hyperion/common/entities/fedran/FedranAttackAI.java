package com.wadoo.hyperion.common.entities.fedran;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;
import java.util.HashMap;

public class FedranAttackAI extends Goal {
    private final FedranEntity fedran;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;



    public FedranAttackAI(FedranEntity fedran) {
        this.fedran = fedran;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.fedran.getTarget();
        return target != null && target.isAlive() && this.fedran.getAnimation() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.fedran.getTarget();
        return target != null && target.isAlive() && this.fedran.getAnimation() == 0 ;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.fedran.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.fedran.getTarget();
        if (target == null) return;
        //TODO STEAM RELEASE BUGS WITH WARDEN


        double distSqr = this.fedran.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        this.fedran.getLookControl().setLookAt(target, 30.0F, 30.0F);
        if (--this.repath <= 0 && (
                this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                        target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                this.fedran.getNavigation().isDone()
        ) {
            this.targetX = target.getX();
            this.targetY = target.getY();
            this.targetZ = target.getZ();
            this.repath = 4 + this.fedran.getRandom().nextInt(7);
            if (distSqr > 32.0D * 32.0D) {
                this.repath += 10;
            } else if (distSqr > 16.0D * 16.0D) {
                this.repath += 5;
            }
            if (!this.fedran.getNavigation().moveTo(target, 1.3D)) {
                this.repath += 15;
            }
        }
        distSqr = this.fedran.distanceToSqr(this.targetX, this.targetY, this.targetZ);
        double dist = Math.sqrt(this.fedran.distanceToSqr(this.targetX, this.targetY, this.targetZ));

        if (dist < 6f) this.fedran.getNavigation().stop();
        if (target.getY() - this.fedran.getY() >= -1 && target.getY() - this.fedran.getY() <= 3) {
            //Attack Checker
            if (testPhase(1, 450) || testPhase(2, 300) || testPhase(3, 150)) {
                this.fedran.setAnimation(6);
                this.fedran.setPhase(this.fedran.getPhase() + 1);
            }
            if (this.fedran.getAttackCooldown() <= 0){
                    //SHORT RANGE ATTACKS
                    if (dist < 3f) {
                        int rand = this.fedran.getRandom().nextInt(11);
                        if (rand < 4) this.fedran.setAnimation(3); //STOMP
                        if (rand >= 4 && rand <= 7) this.fedran.setAnimation(7); //KICK;
                        if (rand > 7) this.fedran.setAnimation(2); //FORWARD JAB
                    }
                //MID RANGE ATTACKS
                if (dist >= 3f && dist <= 10f) {
                    int rand = this.fedran.getRandom().nextInt(11);
                    if (rand < 5) this.fedran.setAnimation(4); //HORIZONTAL SWEEP
                    if (rand >= 5) this.fedran.setAnimation(2); //FORWARD JAB
                }
                //LONG RANGE ATTACKS
                if (dist > 10f && dist < 18f) {
                    this.fedran.setAnimation(5);
                }
            }
        }
    }

    public boolean testPhase(int neededPhase, int healthThreshold){
        return this.fedran.getHealth() < healthThreshold && this.fedran.getPhase() == neededPhase;
    }
}
