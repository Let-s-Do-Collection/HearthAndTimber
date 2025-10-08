package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class SupportBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");

    private static final Map<Direction, VoxelShape> SHAPE_SINGLE = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.empty();
        base = Shapes.join(base, Shapes.box(5.0 / 16.0, 0.0, 14.0 / 16.0, 11.0 / 16.0, 10.0 / 16.0, 1.0), BooleanOp.OR);
        base = Shapes.join(base, Shapes.box(5.0 / 16.0, 10.0 / 16.0, 0.0, 11.0 / 16.0, 1.0, 1.0), BooleanOp.OR);
        for (Direction d : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(d, GeneralUtil.rotateShape(Direction.NORTH, d, base));
        }
    });

    private static final Map<Direction, VoxelShape> SHAPE_CONNECTED = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.box(5.0 / 16.0, 10.0 / 16.0, 0.0, 11.0 / 16.0, 1.0, 1.0);
        for (Direction d : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(d, GeneralUtil.rotateShape(Direction.NORTH, d, base));
        }
    });

    public SupportBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(CONNECTED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction facing = context.getHorizontalDirection().getOpposite();
        BlockPos pos = context.getClickedPos();
        Level level = context.getLevel();
        boolean connect = canConnectBehind(level.getBlockState(pos.relative(facing.getOpposite())), facing);
        return this.defaultBlockState().setValue(FACING, facing).setValue(CONNECTED, connect);
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean moved) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos behind = pos.relative(facing.getOpposite());
            BlockState nb = level.getBlockState(behind);
            if (nb.getBlock() == this && nb.getValue(FACING) == facing && !nb.getValue(CONNECTED)) {
                level.setBlock(behind, nb.setValue(CONNECTED, true), 3);
            }
        }
        super.onPlace(state, level, pos, oldState, moved);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            boolean connect = canConnectBehind(level.getBlockState(pos.relative(facing.getOpposite())), facing);
            if (state.getValue(CONNECTED) != connect) {
                level.setBlock(pos, state.setValue(CONNECTED, connect), 3);
            }
        }
        super.neighborChanged(state, level, pos, sourceBlock, sourcePos, notify);
    }

    private boolean canConnectBehind(BlockState other, Direction facing) {
        return other.getBlock() == this && other.hasProperty(FACING) && other.getValue(FACING) == facing;
    }


    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return (state.getValue(CONNECTED) ? SHAPE_CONNECTED : SHAPE_SINGLE).get(state.getValue(FACING));
    }
}
