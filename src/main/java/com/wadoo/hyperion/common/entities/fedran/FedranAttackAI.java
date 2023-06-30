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

    public int stompCoolDown = 0;
    public int horizontalStrikeCoolDown = 0;
    public int forwardJabCoolDown = 0;
    public int smithingSlamCoolDown = 0;
    public int kickCoolDown = 0;

    public FedranAttackAI(FedranEntity fedran) {
        this.fedran = fedran;
        this.stompCoolDown = this.fedran.stompCoolDown;
        this.horizontalStrikeCoolDown = this.fedran.horizontalStrikeCoolDown;
        this.forwardJabCoolDown = this.fedran.forwardJabCoolDown;
        this.smithingSlamCoolDown = this.fedran.smithingSlamCoolDown;
        this.kickCoolDown = this.fedran.kickCoolDown;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.fedran.getTarget();
        return target != null && target.isAlive() && this.fedran.getAnimation() == 0 ;
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.fedran.getTarget();
        return target != null && target.isAlive() && this.fedran.getAnimation() == 0;
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
        this.stompCoolDown = this.fedran.stompCoolDown;
        this.horizontalStrikeCoolDown = this.fedran.horizontalStrikeCoolDown;
        this.forwardJabCoolDown = this.fedran.forwardJabCoolDown;
        this.smithingSlamCoolDown = this.fedran.smithingSlamCoolDown;
        this.kickCoolDown = this.fedran.kickCoolDown;

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
            if (this.fedran.getHealth() < this.fedran.getHealthThreshold() && !this.fedran.hasTransitioned()) this.fedran.setAnimation(6);

            //if (this.fedran.isReadyToJump() && this.fedran.jumpCoolDown <= 0 && this.fedran.getHealth() < this.fedran.getHealthThreshold() && !this.fedran.hasTransitioned()) this.fedran.setAnimation(8);

            if (dist > 2.8f && dist < 7.5f && this.fedran.forwardJabCoolDown <= 0) handleAttack(2);
            //FORWARD JAB
            if (dist < 2f && this.fedran.kickCoolDown <= 0) handleAttack(7);
            //KICK
            if (dist < 5f && dist > 2.5f&&  this.fedran.stompCoolDown <= 0) handleAttack(3);
            //STOMP
            if (dist > 3f && dist < 7f &&  this.fedran.horizontalStrikeCoolDown <= 0) handleAttack(4);
            //HORIZONTAL STRIKE
            if (dist > 5.5f && dist < 18.5f && this.fedran.smithingSlamCoolDown <= 0) handleAttack(5);
            //SMITHING SLAM


        }
    }

    private void handleAttack(int attackID){
        this.fedran.setAnimation(attackID);
        switch (attackID){
            case 2:
                this.fedran.forwardJabCoolDown = 50;
                break;
            case 3:
                this.fedran.stompCoolDown = 40;
                break;
            case 4:
                this.fedran.horizontalStrikeCoolDown = 90;
                break;
            case 5:
                this.fedran.smithingSlamCoolDown = 110;
                break;
            case 7:
                this.fedran.kickCoolDown = 55;
                break;
        }
    }
}
