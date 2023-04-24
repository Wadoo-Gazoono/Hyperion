package com.wadoo.hyperion.common.entities.ai.util;

import java.util.LinkedList;

public class Attack {

    private String name;
    private float range;
    private float reach;
    private int attackLength;
    private int attackTime;
    private int coolDown;

    public Attack(String name, float range, float reach, int attackLength, int attackTime, int coolDown){
        this.name = name;
        this.range = range;
        this.reach = reach;
        this.attackLength = attackLength;
        this.attackTime = attackTime;
        this.coolDown = coolDown;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRange() {
        return range;
    }

    public void setRange(float range) {
        this.range = range;
    }

    public float getReach() {
        return reach;
    }

    public void setReach(float reach) {
        this.reach = reach;
    }

    public int getAttackLength() {
        return attackLength;
    }

    public void setAttackLength(int attackLength) {
        this.attackLength = attackLength;
    }


    public int getCoolDown() {
        return coolDown;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }


}
