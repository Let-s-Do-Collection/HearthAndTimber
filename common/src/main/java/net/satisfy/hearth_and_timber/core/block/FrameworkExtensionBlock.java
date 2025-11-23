package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class FrameworkExtensionBlock extends Block {
    private static final VoxelShape INTERACTION_SHAPE = Shapes.block();
    private static final VoxelShape BELOW_BLOCK_SHAPE = Shapes.block().move(0, -1, 0);
    private static final VoxelShape PLATFORM_SHAPE = Block.box(0, 0, 0, 16, 2, 16);

    public FrameworkExtensionBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return INTERACTION_SHAPE;
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (context.isAbove(BELOW_BLOCK_SHAPE, pos, true)) {
            return PLATFORM_SHAPE;
        }
        return Shapes.empty();
    }
}