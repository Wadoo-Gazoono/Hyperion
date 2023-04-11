package com.arathain.vigorem.api.init;



import com.arathain.vigorem.api.Easing;
import com.arathain.vigorem.api.Keyframe;
import com.arathain.vigorem.api.anim.Animation;
import com.arathain.vigorem.test.SmashAnimation;
import com.wadoo.hyperion.Hyperion;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class Animations {
    private static final Map<ResourceLocation, Supplier<Animation>> ANIMATIONS = new HashMap<>();
    public static final Vector3f ZERO = new Vector3f(0,0,0);
    private static final Map<String, List<Keyframe>> TPOSE_START = new HashMap<>(), TPOSE_END = new HashMap<>();
    private static final Map<String, List<Keyframe>> SMASH_R = new HashMap<>();
    public static final Map<String, List<Keyframe>> SNEAK = new HashMap<>();
    public static void init() {
        List<Keyframe> cache = new ArrayList<>();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, 1.57079632679f), ZERO, ZERO, 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, 1.57079632679f), ZERO, ZERO, 30));
        TPOSE_START.put("right_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, 1.57079632679f), ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, 0), ZERO, ZERO, 20));
        TPOSE_END.put("right_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, -1.57079632679f), ZERO, ZERO, 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, -1.57079632679f), ZERO, ZERO, 30));
        TPOSE_START.put("left_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, -1.57079632679f), ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0, 0, 0), ZERO, ZERO, 20));
        TPOSE_END.put("left_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO,  new Vector3f(0, -12, 0), 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.1f, 0, 0), ZERO, new Vector3f(0, -12, 0), 10));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.1f, 0, 0), ZERO, new Vector3f(0, -12, 0), 30));
        TPOSE_START.put("body", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.1f, 0, 0), ZERO, new Vector3f(0, -12, 0), 0));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, new Vector3f(0, -12, 0), 20));
        TPOSE_END.put("body", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO,  new Vector3f(0, -12, 0), 0));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(-0.15585f, 0, 0), ZERO,  new Vector3f(0, -12, 0), 3));
        cache.add(new Keyframe(Easing.QUINTIC_IN, ZERO, new Vector3f(-0.305f, 0, 0), ZERO,  new Vector3f(0, -12, 0), 6));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, new Vector3f(1.09f, 0, 0.1f), ZERO,  new Vector3f(0, -12, 0), 15));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(0.9354f, 0, 0.1f), ZERO,  new Vector3f(0, -12, 0), 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0.9354f, 0, 0.1f), ZERO,  new Vector3f(0, -12, 0), 25));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0f, 0, 0), ZERO,  new Vector3f(0, -12, 0), 33));
        SMASH_R.put("body", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO,  ZERO, 0, false));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(0.15585f, 0, 0), ZERO,  ZERO, 3, false));
        cache.add(new Keyframe(Easing.QUINTIC_IN, ZERO, new Vector3f(0.305f, 0, 0), ZERO,  ZERO, 6, false));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, new Vector3f(-1.09f, 0, 0), ZERO,  ZERO, 15, false));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(-0.9354f, 0, 0), ZERO,  ZERO, 20, false));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.9354f, 0, 0), ZERO,  ZERO, 25, false));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0f, 0, 0), ZERO,  ZERO, 33, false));
        SMASH_R.put("head", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(0, 0.15585f, 0), ZERO, ZERO, 4));
        cache.add(new Keyframe(Easing.EXPO_IN, ZERO, new Vector3f(0, 0.305f, 0), ZERO, ZERO, 8));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, new Vector3f(0.6545f, 0.3927f, 0.5236f), ZERO,  ZERO, 15));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(0.5817f, 0.383f, 0.465f), ZERO,  ZERO, 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(0.5817f, 0.383f, 0.465f), ZERO, ZERO, 25));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 33));
        SMASH_R.put("left_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(-1.204f, 0.09f, 0.09f), ZERO, ZERO, 4));
        cache.add(new Keyframe(Easing.EXPO_IN, ZERO, new Vector3f(-2.456f, 0.175f, 0.175f), ZERO, ZERO, 8));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, new Vector3f(-1.702f, 0.09f, 0.47f), ZERO,  ZERO, 15));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(-1.7745f, -0.06f, 0.40f), ZERO,  ZERO, 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-1.7745f, -0.06f, 0.40f), ZERO, ZERO, 25));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 33));
        SMASH_R.put("right_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.EXPO_IN, ZERO, ZERO, ZERO, ZERO, 8));
        cache.add(new Keyframe(Easing.SINE_OUT, new Vector3f(0, 0, -10), new Vector3f(0, 0, 0), ZERO,  ZERO, 15));
        cache.add(new Keyframe(Easing.LINEAR, new Vector3f(0, 0, -10), new Vector3f(0, 0, 0), ZERO,  ZERO, 20));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, new Vector3f(0, 0, -10), new Vector3f(0, 0, 0), ZERO, ZERO, 25));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 33));
        SMASH_R.put("right_hand", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(0f, -0.0445f, 0f), ZERO, ZERO, 3));
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO,new Vector3f(0f, -0.087266f, 0f), ZERO, ZERO, 7));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, new Vector3f(-0.087266f, 0.087266f, 0f), ZERO,  ZERO, 16));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(-0.0775f, 0.06789f, 0f), ZERO,  ZERO, 21));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.0775f, 0.06789f, 0f), ZERO, ZERO, 26));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 33));
        SMASH_R.put("right_leg", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO, ZERO, ZERO, ZERO, 0));
        cache.add(new Keyframe(Easing.EXPO_OUT, ZERO, new Vector3f(0.178f, 0f, 0.0669f), ZERO, new Vector3f(0, 1, 0), 3));
        cache.add(new Keyframe(Easing.CUBIC_IN, ZERO,new Vector3f(0.349066f, 0f, 0.1309f), ZERO, ZERO, 6));
        cache.add(new Keyframe(Easing.QUAD_IN, ZERO, new Vector3f(0.349066f, 0f, 0.1309f), ZERO,  ZERO, 9));
        cache.add(new Keyframe(Easing.CUBIC_OUT, ZERO, new Vector3f(0.05693f, 0f, 0.102f), ZERO,  new Vector3f(0, 1.5f, -0.75f), 12));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, new Vector3f(-0.3103f, 0f, 0.0534f), ZERO,  new Vector3f(0, 0.067f, -2f), 15));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, new Vector3f(-0.3103f, 0f, 0.0534f), ZERO,  new Vector3f(0, 0.067f, -2f), 25));
        cache.add(new Keyframe(Easing.QUAD_IN_OUT, ZERO, ZERO, ZERO, ZERO, 33));
        SMASH_R.put("left_leg", new ArrayList<>(cache));
        cache.clear();

        put(Hyperion.id("smash_left"), () -> new SmashAnimation(33, SMASH_R, true));
        put(Hyperion.id("smash_right"), () -> new SmashAnimation(33, SMASH_R, false));
        //put(Hyperion.id("t_pose"), () -> new TPoseAnimation(30, TPOSE_START, 20, TPOSE_END));

        cache.add(new Keyframe(Easing.QUAD_IN, new Vector3f(0f, 2f, 4f), deg(30f, 0f, 0f), ZERO, new Vector3f(0, -12, 0), 0.0f));
        cache.add(new Keyframe(Easing.QUAD_OUT, new Vector3f(0f, 1.5f, 4f), deg(29f, 0f, 0f), ZERO, new Vector3f(0, -12, 0), 5.0f));
        cache.add(new Keyframe(Easing.QUAD_IN, new Vector3f(0f, 2f, 4f), deg(30f, 0f, 0f), ZERO, new Vector3f(0, -12, 0), 10.0f));
        cache.add(new Keyframe(Easing.QUAD_OUT, new Vector3f(0f, 1.5f, 4f), deg(29f, 0f, 0f), ZERO, new Vector3f(0, -12, 0), 15.0f));
        cache.add(new Keyframe(Easing.LINEAR, new Vector3f(0f, 2f, 4f), deg(30f, 0f, 0f), ZERO, new Vector3f(0, -12, 0), 20));
        SNEAK.put("body", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, 10f), ZERO, ZERO, 0.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, 8f), ZERO, ZERO, 2.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, 10f), ZERO, ZERO, 5.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, 12f), ZERO, ZERO, 7.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, 10f), ZERO, ZERO, 10.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, 8f), ZERO, ZERO, 12.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, 10f), ZERO, ZERO, 15.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, 12f), ZERO, ZERO, 17.5f));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, deg(-20f, 0f, 10f), ZERO, ZERO, 20));
        SNEAK.put("right_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, -10f), ZERO, ZERO, 0.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, -8f), ZERO, ZERO, 2.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, -10f), ZERO, ZERO, 5.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, -12f), ZERO, ZERO, 7.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, -10f), ZERO, ZERO, 10.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, -8f), ZERO, ZERO, 12.5f));
        cache.add(new Keyframe(Easing.SINE_IN, ZERO, deg(-20f, 0f, -10f), ZERO, ZERO, 15.0f));
        cache.add(new Keyframe(Easing.SINE_OUT, ZERO, deg(-20f, 0f, -12f), ZERO, ZERO, 17.5f));
        cache.add(new Keyframe(Easing.LINEAR, ZERO, deg(-20f, 0f, -10f), ZERO, ZERO, 20));
        SNEAK.put("left_arm", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.CUBIC_IN, new Vector3f(0f, 0f, 1f), deg(30f, 0f, -2f), ZERO, ZERO, 0.0f));
        cache.add(new Keyframe(Easing.CUBIC_OUT, new Vector3f(0f, 1f, 0.5f), deg(15f, 0f, -2f), ZERO, ZERO, 5.0f));
        cache.add(new Keyframe(Easing.SINE_IN, new Vector3f(0f, 0f, -2f), deg(0f, 0f, -2f), ZERO, ZERO, 10.0f));
        cache.add(new Keyframe(Easing.LINEAR, new Vector3f(0f, 0f, 1f), deg(30f, 0f, -2f), ZERO, ZERO, 20));
        SNEAK.put("right_leg", new ArrayList<>(cache));
        cache.clear();
        cache.add(new Keyframe(Easing.SINE_IN, new Vector3f(0f, 0f, -2f), deg(0f, 0f, 2f), ZERO, ZERO, 0.0f));
        cache.add(new Keyframe(Easing.CUBIC_IN, new Vector3f(0f, 0f, 1f), deg(30f, 0f, 2f), ZERO, ZERO, 10.0f));
        cache.add(new Keyframe(Easing.CUBIC_OUT, new Vector3f(0f, 1f, 0.5f), deg(15f, 0f, 2f), ZERO, ZERO, 15.0f));
        cache.add(new Keyframe(Easing.LINEAR, new Vector3f(0f, 0f, -2f), deg(0f, 0f, 2f), ZERO, ZERO, 20));
        SNEAK.put("left_leg", new ArrayList<>(cache));
        cache.clear();
    }
    public static void put(ResourceLocation id, Supplier<Animation> anim) {
        ANIMATIONS.put(id, anim);
    }
    public static Animation getAnimation(ResourceLocation id) {
        return ANIMATIONS.get(id) != null ? ANIMATIONS.get(id).get() : null;
    }
    public static Vector3f deg(float x, float y, float z) {
        return new Vector3f((float) (x * Math.PI/180), (float) (y * Math.PI/180), (float) (z * Math.PI/180));
    }
}