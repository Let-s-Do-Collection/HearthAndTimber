package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WindowBlock extends IronBarsBlock {
    public static final IntegerProperty PART = IntegerProperty.create("part", 0, 3);

    public WindowBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(NORTH, false).setValue(EAST, false).setValue(SOUTH, false).setValue(WEST, false).setValue(WATERLOGGED, false));
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (!world.isClientSide()) {
            updateWindows(world, pos);
        }
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor world, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            world.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(world));
        }
        if (direction.getAxis().isVertical()) {
            updateWindows(world, pos);
        }
        return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos fromPos, boolean isMoving) {
        super.neighborChanged(state, world, pos, block, fromPos, isMoving);
        if (!world.isClientSide()) {
            updateWindows(world, pos);
        }
    }

    private void updateWindows(LevelAccessor world, BlockPos pos) {
        BlockPos lowest = getLowestWindow(world, pos);
        BlockPos highest = getHighestWindow(world, pos);
        int height = 0;
        for (BlockPos p = lowest; p.compareTo(highest) <= 0; p = p.above()) {
            height++;
        }
        if (height == 1) {
            BlockState cs = world.getBlockState(lowest);
            if (cs.getBlock() == this) world.setBlock(lowest, cs.setValue(PART, 0), 3);
            return;
        }
        BlockState c0 = world.getBlockState(lowest);
        if (c0.getBlock() == this) world.setBlock(lowest, c0.setValue(PART, 1), 3);
        if (height == 2) {
            BlockState c1 = world.getBlockState(highest);
            if (c1.getBlock() == this) world.setBlock(highest, c1.setValue(PART, 3), 3);
            return;
        }
        for (BlockPos p = lowest.above(); p.compareTo(highest) < 0; p = p.above()) {
            BlockState cs = world.getBlockState(p);
            if (cs.getBlock() == this) world.setBlock(p, cs.setValue(PART, 2), 3);
        }
        BlockState ct = world.getBlockState(highest);
        if (ct.getBlock() == this) world.setBlock(highest, ct.setValue(PART, 3), 3);
    }

    private BlockPos getLowestWindow(LevelAccessor world, BlockPos pos) {
        while (world.getBlockState(pos.below()).getBlock() == this) {
            pos = pos.below();
        }
        return pos;
    }

    private BlockPos getHighestWindow(LevelAccessor world, BlockPos pos) {
        while (world.getBlockState(pos.above()).getBlock() == this) {
            pos = pos.above();
        }
        return pos;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(PART, NORTH, EAST, WEST, SOUTH, WATERLOGGED);
    }
}
