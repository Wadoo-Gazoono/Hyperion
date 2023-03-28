package com.wadoo.hyperion.client.models.block;

import com.wadoo.hyperion.Hyperion;
import com.wadoo.hyperion.common.blocks.entities.AgraliteCageBlockEntity;
import com.wadoo.hyperion.common.blocks.entities.KilnBlockEntity;
import com.wadoo.hyperion.common.entities.CrucibleEntity;
import com.wadoo.hyperion.common.registry.ParticleHandler;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.molang.MolangParser;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

import static net.minecraft.world.level.block.AbstractFurnaceBlock.FACING;
import static net.minecraft.world.level.block.AbstractFurnaceBlock.LIT;


public class KilnModel extends DefaultedBlockGeoModel<KilnBlockEntity> {
    public Vec3 faceVector = new Vec3(0d,0d,0d);
    public KilnModel() {
        super(new ResourceLocation(Hyperion.MODID, "kiln"));    }

    @Override
    public ResourceLocation getModelResource(KilnBlockEntity object) {
        return buildFormattedModelPath(new ResourceLocation(Hyperion.MODID, "kiln"));
    }

    @Override
    public ResourceLocation getTextureResource(KilnBlockEntity object) {
        return buildFormattedTexturePath(new ResourceLocation(Hyperion.MODID, "kiln"));
    }

    @Override
    public ResourceLocation getAnimationResource(KilnBlockEntity animatable) {
        return buildFormattedAnimationPath(new ResourceLocation(Hyperion.MODID, "kiln"));
    }



    @Override
    public void applyMolangQueries(KilnBlockEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        BlockPos pos = animatable.getBlockPos();
        double d0 = (double)pos.getX() + 0.5D;
        double d1 = (double)pos.getY() + 0.3D;
        double d2 = (double)pos.getZ() + 0.5D;

        Direction direction = animatable.getBlockState().getValue(FACING);
        Direction.Axis direction$axis = direction.getAxis();
        double d3 = 0.52D;
        double d4 = 0;
        double d5 = direction$axis == Direction.Axis.X ? (double)direction.getStepX() * 0.52D : d4;
        double d6 = 0;
        double d7 = direction$axis == Direction.Axis.Z ? (double)direction.getStepZ() * 0.52D : d4;
        faceVector = new Vec3(d0+d5,d1+d6,d2+d7);
        d4 = animatable.getLevel().random.nextDouble() * 0.9D - 0.3D;
        d6 = animatable.getLevel().random.nextDouble() * 5.0D / 16.0D;
        Vec3 shootVec = new Vec3(direction.getStepX(),0,direction.getStepZ()).multiply(0.1f + animatable.getLevel().random.nextDouble()/10,0.0f,0.1f + animatable.getLevel().random.nextDouble()/10);
        if(animatable.getBlockState().getValue(LIT) && animatable.getLevel().random.nextFloat() < 0.25 && !mc.isPaused()) {
            animatable.getLevel().addParticle(ParticleTypes.SMOKE, d0 + d5, d1 + d6, d2 + d7, shootVec.x, 0.0D, shootVec.z);
            animatable.getLevel().addParticle(ParticleTypes.FLAME, d0 + d5, d1 + d6, d2 + d7, shootVec.x, 0.0D, shootVec.z);
        }

    }

    @Override
    public void setCustomAnimations(KilnBlockEntity animatable, long instanceId, AnimationState<KilnBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        Entity entity = Minecraft.getInstance().getCameraEntity();

        CoreGeoBone leftEye = getAnimationProcessor().getBone("leftPupil");
        CoreGeoBone rightEye = getAnimationProcessor().getBone("rightPupil");


        if (leftEye != null && leftEye.getInitialSnapshot() != null && rightEye != null && rightEye.getInitialSnapshot() != null) {


            if (entity != null) {
                Vec3 vec3 = entity.getEyePosition(0.0F);
                Vec3 vec31 = animatable.getBlockPos().getCenter().add(0d,0.5d,0d);
                if(vec3.y > vec31.y){
                    rightEye.setPosY(0.65f);
                    leftEye.setPosY(0.65f);
                }
                else{
                    rightEye.setPosY(-0.05f);
                    leftEye.setPosY(-0.05f);
                }

                BlockPos pos = animatable.getBlockPos();
                Vec3 blockPositionVec = new Vec3(pos.getX() + 0.5D, pos.getY(), pos.getZ() + 0.5d);
                Vec3 viewVec = faceVector.subtract(blockPositionVec);


                Vec3 vec32 = viewVec;
                vec32 = new Vec3(vec32.x, 0.0D, vec32.z);
                Vec3 vec33 = (new Vec3(vec31.x - vec3.x, 0.0D, vec31.z - vec3.z)).normalize().yRot(((float) Math.PI / 2F));
                double d1 = vec32.dot(vec33);
                rightEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));
                leftEye.setPosX(Mth.sqrt((float) Math.abs(d1)) * 2.0F * (float) Math.signum(d1));}
        }
    }
}
