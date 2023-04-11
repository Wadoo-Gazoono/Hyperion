package com.wadoo.hyperion.common.util;

import net.minecraft.client.animation.Keyframe;

import java.util.HashMap;
import java.util.List;

public class GrabAnimation {
    protected HashMap<Integer, GrabKeyframe> keyframes = new HashMap<Integer, GrabKeyframe>();

    public GrabAnimation(){
    }

    public GrabAnimation addKeyFrame(int timeStamp, GrabKeyframe frame){

        this.keyframes.put(timeStamp,frame);
        return this;
    }



    public GrabKeyframe isOnKeyframe(int entityTick){
        for(Integer i : keyframes.keySet()){
            if(entityTick == i) return keyframes.get(i);
        }
        return null;
    }

    public HashMap<Integer, GrabKeyframe> getKeyframes(){
        return keyframes;
    }
}
