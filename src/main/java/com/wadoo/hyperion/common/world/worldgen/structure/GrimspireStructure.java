package com.wadoo.hyperion.common.world.worldgen.structure;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.wadoo.hyperion.common.registry.StructureHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldGenerationContext;
import net.minecraft.world.level.levelgen.heightproviders.HeightProvider;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

import java.util.Optional;

public class GrimspireStructure extends Structure {

    // A custom codec that changes the size limit for our code_structure_sky_fan.json's config to not be capped at 7.
    // With this, we can have a structure with a size limit up to 30 if we want to have extremely long branches of pieces in the structure.
    public static final Codec<GrimspireStructure> CODEC = RecordCodecBuilder.<GrimspireStructure>mapCodec(instance ->
            instance.group(GrimspireStructure.settingsCodec(instance),
                    StructureTemplatePool.CODEC.fieldOf("start_pool").forGetter(structure -> structure.startPool),
                    ResourceLocation.CODEC.optionalFieldOf("start_jigsaw_name").forGetter(structure -> structure.startJigsawName),
                    Codec.intRange(0, 30).fieldOf("size").forGetter(structure -> structure.size),
                    HeightProvider.CODEC.fieldOf("start_height").forGetter(structure -> structure.startHeight),
                    Heightmap.Types.CODEC.optionalFieldOf("project_start_to_heightmap").forGetter(structure -> structure.projectStartToHeightmap),
                    Codec.intRange(1, 128).fieldOf("max_distance_from_center").forGetter(structure -> structure.maxDistanceFromCenter)
            ).apply(instance, GrimspireStructure::new)).codec();

    private final Holder<StructureTemplatePool> startPool;
    private final Optional<ResourceLocation> startJigsawName;
    private final int size;
    private final HeightProvider startHeight;
    private final Optional<Heightmap.Types> projectStartToHeightmap;
    private final int maxDistanceFromCenter;

    public GrimspireStructure(Structure.StructureSettings config,
                             Holder<StructureTemplatePool> startPool,
                             Optional<ResourceLocation> startJigsawName,
                             int size,
                             HeightProvider startHeight,
                             Optional<Heightmap.Types> projectStartToHeightmap,
                             int maxDistanceFromCenter)
    {
        super(config);
        this.startPool = startPool;
        this.startJigsawName = startJigsawName;
        this.size = size;
        this.startHeight = startHeight;
        this.projectStartToHeightmap = projectStartToHeightmap;
        this.maxDistanceFromCenter = maxDistanceFromCenter;
    }

    private static boolean extraSpawningChecks(GenerationContext context, BlockPos blockpos) {

        // make sure land is open enough for city
        ChunkPos chunkPos = new ChunkPos(blockpos);
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (int curChunkX = chunkPos.x - 4; curChunkX <= chunkPos.x + 4; curChunkX++) {
            for (int curChunkZ = chunkPos.z - 4; curChunkZ <= chunkPos.z + 4; curChunkZ++) {
                mutable.set(curChunkX << 4, context.chunkGenerator().getSeaLevel() + 5, curChunkZ << 4);
                NoiseColumn blockView = context.chunkGenerator().getBaseColumn(mutable.getX(), mutable.getZ(), context.heightAccessor(), context.randomState());
                int minValidSpace = 80;
                int maxHeight = 90;

                while(mutable.getY() < maxHeight) {
                    BlockState state = blockView.getBlock(mutable.getY());
                    if(!state.isAir()) {
                        return false;
                    }
                    mutable.move(Direction.UP);
                }
            }
        }

        return true;
    }

    @Override
    public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext context) {

        int offsetY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));
        BlockPos blockpos = new BlockPos(context.chunkPos().getMinBlockX(), offsetY, context.chunkPos().getMinBlockZ());


        if (!GrimspireStructure.extraSpawningChecks(context, blockpos)) {
            return Optional.empty();
        }

        int startY = this.startHeight.sample(context.random(), new WorldGenerationContext(context.chunkGenerator(), context.heightAccessor()));

        // Turns the chunk coordinates into actual coordinates we can use. (Gets corner of that chunk)
        ChunkPos chunkPos = context.chunkPos();
        BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), startY, chunkPos.getMinBlockZ());

        Optional<Structure.GenerationStub> structurePiecesGenerator =
                JigsawPlacement.addPieces(
                        context, // Used for JigsawPlacement to get all the proper behaviors done.
                        this.startPool, // The starting pool to use to create the structure layout from
                        this.startJigsawName, // Can be used to only spawn from one Jigsaw block. But we don't need to worry about this.
                        this.size, // How deep a branch of pieces can go away from center piece. (5 means branches cannot be longer than 5 pieces from center piece)
                        blockPos, // Where to spawn the structure.
                        false, // "useExpansionHack" This is for legacy villages to generate properly. You should keep this false always.
                        this.projectStartToHeightmap, // Adds the terrain height's y value to the passed in blockpos's y value. (This uses WORLD_SURFACE_WG heightmap which stops at top water too)
                        // Here, blockpos's y value is 60 which means the structure spawn 60 blocks above terrain height.
                        // Set this to false for structure to be place only at the passed in blockpos's Y value instead.
                        // Definitely keep this false when placing structures in the nether as otherwise, heightmap placing will put the structure on the Bedrock roof.
                        this.maxDistanceFromCenter); // Maximum limit for how far pieces can spawn from center. You cannot set this bigger than 128 or else pieces gets cutoff.

        return structurePiecesGenerator;
    }

    @Override
    public StructureType<?> type() {
        return StructureHandler.GRIMSPIRE.get();
    }
}