package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.NotNull;

public class TimberDiagonalFrameBlock extends TimberFrameBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty LEFT = BooleanProperty.create("left");

    public TimberDiagonalFrameBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(baseState, properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(WATERLOGGED, false)
                .setValue(APPLIED, false)
                .setValue(LEFT, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LEFT);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.getLevel().getFluidState(pos);
        Direction face = context.getClickedFace();
        boolean left = true;
        if (face == Direction.EAST) {
            left = true;
        } else if (face == Direction.WEST) {
            left = false;
        } else if (face == Direction.NORTH) {
            left = context.getClickLocation().x - pos.getX() < 0.5d;
        } else if (face == Direction.SOUTH) {
            left = context.getClickLocation().x - pos.getX() > 0.5d;
        } else if (face == Direction.UP || face == Direction.DOWN) {
            Direction look = context.getHorizontalDirection();
            double cx = context.getClickLocation().x - pos.getX() - 0.5d;
            double cz = context.getClickLocation().z - pos.getZ() - 0.5d;
            if (look == Direction.NORTH) left = cx < 0d;
            else if (look == Direction.SOUTH) left = cx > 0d;
            else if (look == Direction.WEST) left = cz > 0d;
            else left = cz < 0d;
        }
        return this.defaultBlockState()
                .setValue(WATERLOGGED, fluid.getType() == Fluids.WATER)
                .setValue(APPLIED, false)
                .setValue(LEFT, left);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return super.getShape(state, level, pos, context);
    }
}