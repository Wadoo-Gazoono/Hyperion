package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolHead;
import com.wadoo.hyperion.common.entities.agol.AgolShooter;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolShooterModel extends DefaultedEntityGeoModel<AgolShooter> {
    public AgolShooterModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_shooter"));
    }
}
