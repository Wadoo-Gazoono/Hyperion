package com.arathain.vigorem.api;


import com.arathain.vigorem.api.anim.Animation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Tuple;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class MoveSet {
    private final List<ResourceLocation> anims;
    private final Predicate<Tuple<Player, Boolean>> predicate;
    public MoveSet(Predicate<Tuple<Player, Boolean>> applicationPredicate, ResourceLocation... animationIds) {
        this.anims = Arrays.stream(animationIds).toList();
        this.predicate = applicationPredicate;
    }
    public boolean shouldApply(Player player, boolean rightClick) {
        return predicate.test(new Tuple<>(player, rightClick));
    }

    public ResourceLocation getAnimId(Animation current) {
        if(current == null || !anims.contains(current.getId()) || anims.size() == 1 || (anims.contains(current.getId()) && anims.indexOf(current.getId()) == anims.size() - 1)) {
            return anims.get(0);
        } else {
            return anims.get(anims.indexOf(current.getId()) + 1);
        }
    }
}