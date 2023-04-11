package com.wadoo.hyperion.common.entities.ai;

import net.minecraft.world.entity.ai.goal.Goal;

public class HyperionFollowTarget extends Goal {
    @Override
    public boolean canUse() {
        return false;
    }
}
