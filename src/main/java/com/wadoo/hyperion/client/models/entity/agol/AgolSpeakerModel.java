package com.wadoo.hyperion.client.models.entity.agol;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.entities.agol.AgolModuleEntity;
import com.wadoo.hyperion.common.entities.agol.AgolSpeakerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class AgolSpeakerModel extends DefaultedEntityGeoModel<AgolSpeakerEntity> {
    public AgolSpeakerModel() {
        super(new ResourceLocation(Hyperion.MODID, "agol/agol_speaker"));
    }

    @Override
    public RenderType getRenderType(AgolSpeakerEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(getTextureResource(animatable));
    }
}
