package com.wadoo.hyperion.common.entities.obelisk;


import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;


public class ObeliskAttackAI extends Goal {
    private final ObeliskEntity obelisk;

    private int repath;
    private double targetX;
    private double targetY;
    private double targetZ;

    private int sitCooldown = 0;

    public ObeliskAttackAI(ObeliskEntity obelisk) {
        this.obelisk = obelisk;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.JUMP, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.obelisk.getTarget();
        return target != null && target.isAlive() && this.obelisk.getAnimation() == 0;
    }

    @Override
    public void start() {
        this.repath = 0;
    }

    @Override
    public void stop() {
        this.obelisk.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity target = this.obelisk.getTarget();
        sitCooldown--;
        if (target == null) return;

        double dist = this.obelisk.distanceToSqr(this.targetX, this.targetY, this.targetZ);

        if (!obelisk.getSitting()){
            this.obelisk.getLookControl().setLookAt(target, 30.0F, 30.0F);
            if (--this.repath <= 0 && (
                    this.targetX == 0.0D && this.targetY == 0.0D && this.targetZ == 0.0D ||
                            target.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0D) ||
                    this.obelisk.getNavigation().isDone()
            ) {
                this.targetX = target.getX();
                this.targetY = target.getY();
                this.targetZ = target.getZ();
                this.repath = 4 + this.obelisk.getRandom().nextInt(7);
                if (dist > 32.0D * 32.0D) {
                    this.repath += 10;
                } else if (dist > 16.0D * 16.0D) {
                    this.repath += 5;
                }
                if (!this.obelisk.getNavigation().moveTo(target, 1.3D)) {
                    this.repath += 15;
                }
            }
        }
        dist = this.obelisk.distanceToSqr(target);
        if (target.getY() - this.obelisk.getY() >= -1 && target.getY() - this.obelisk.getY() <= 3) {
            //Attack Checker
            if(Math.sqrt(dist) < 6.5F && !this.obelisk.getSitting()){
                if(sitCooldown < 0) {
                    this.obelisk.setAnimation(1);
                    sitCooldown = 20;
                }
            }
            if (Math.sqrt(dist) > 9.5f && this.obelisk.getSitting()){
                if(sitCooldown < 0) {
                    this.obelisk.setAnimation(2);
                }
            }
        }
    }
}
