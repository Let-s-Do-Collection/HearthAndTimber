package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class PillarBlock extends Block {
    public static final BooleanProperty NORTH_CONNECTED = BooleanProperty.create("north_connected");
    public static final BooleanProperty SOUTH_CONNECTED = BooleanProperty.create("south_connected");
    public static final BooleanProperty WEST_CONNECTED = BooleanProperty.create("west_connected");
    public static final BooleanProperty EAST_CONNECTED = BooleanProperty.create("east_connected");

    protected static final VoxelShape SHAPE;
    protected static final VoxelShape CONNECT_N;
    protected static final VoxelShape CONNECT_S;
    protected static final VoxelShape CONNECT_W;
    protected static final VoxelShape CONNECT_E;

    static {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(5.0 / 16.0, 0.0, 5.0 / 16.0, 11.0 / 16.0, 1.0, 11.0 / 16.0), BooleanOp.OR);
        SHAPE = shape;

        CONNECT_N = Shapes.box(5.0 / 16.0, 10.0 / 16.0, 0.0, 11.0 / 16.0, 1.0, 5.0 / 16.0);
        CONNECT_S = Shapes.box(5.0 / 16.0, 10.0 / 16.0, 11.0 / 16.0, 11.0 / 16.0, 1.0, 1.0);
        CONNECT_W = Shapes.box(0.0, 10.0 / 16.0, 5.0 / 16.0, 5.0 / 16.0, 1.0, 11.0 / 16.0);
        CONNECT_E = Shapes.box(10.0 / 16.0, 10.0 / 16.0, 5.0 / 16.0, 1.0, 1.0, 11.0 / 16.0);
    }

    public PillarBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(NORTH_CONNECTED, false)
                .setValue(SOUTH_CONNECTED, false)
                .setValue(WEST_CONNECTED, false)
                .setValue(EAST_CONNECTED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(NORTH_CONNECTED, SOUTH_CONNECTED, WEST_CONNECTED, EAST_CONNECTED);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos p = ctx.getClickedPos();
        BlockGetter level = ctx.getLevel();
        boolean n = isSupport(level.getBlockState(p.relative(Direction.NORTH)));
        boolean s = isSupport(level.getBlockState(p.relative(Direction.SOUTH)));
        boolean w = isSupport(level.getBlockState(p.relative(Direction.WEST)));
        boolean e = isSupport(level.getBlockState(p.relative(Direction.EAST)));
        return this.defaultBlockState()
                .setValue(NORTH_CONNECTED, n)
                .setValue(SOUTH_CONNECTED, s)
                .setValue(WEST_CONNECTED, w)
                .setValue(EAST_CONNECTED, e);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState adj, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (dir == Direction.NORTH) return state.setValue(NORTH_CONNECTED, isSupport(adj));
        if (dir == Direction.SOUTH) return state.setValue(SOUTH_CONNECTED, isSupport(adj));
        if (dir == Direction.WEST) return state.setValue(WEST_CONNECTED, isSupport(adj));
        if (dir == Direction.EAST) return state.setValue(EAST_CONNECTED, isSupport(adj));
        return state;
    }

    private static boolean isSupport(BlockState s) {
        return s.getBlock() instanceof SupportBlock;
    }

    @Override
    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        VoxelShape shape = SHAPE;
        if (state.getValue(NORTH_CONNECTED)) shape = Shapes.join(shape, CONNECT_N, BooleanOp.OR);
        if (state.getValue(SOUTH_CONNECTED)) shape = Shapes.join(shape, CONNECT_S, BooleanOp.OR);
        if (state.getValue(WEST_CONNECTED)) shape = Shapes.join(shape, CONNECT_W, BooleanOp.OR);
        if (state.getValue(EAST_CONNECTED)) shape = Shapes.join(shape, CONNECT_E, BooleanOp.OR);
        return shape;
    }
}