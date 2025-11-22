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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import org.jetbrains.annotations.NotNull;

public class PillarBlock extends Block {
    public static final BooleanProperty NORTH_CONNECTED = BooleanProperty.create("north_connected");
    public static final BooleanProperty SOUTH_CONNECTED = BooleanProperty.create("south_connected");
    public static final BooleanProperty WEST_CONNECTED = BooleanProperty.create("west_connected");
    public static final BooleanProperty EAST_CONNECTED = BooleanProperty.create("east_connected");
    public static final BooleanProperty EXTENDED_TOP = BooleanProperty.create("extended_top");

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
                .setValue(EAST_CONNECTED, false)
                .setValue(EXTENDED_TOP, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(NORTH_CONNECTED, SOUTH_CONNECTED, WEST_CONNECTED, EAST_CONNECTED, EXTENDED_TOP);
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!(stack.getItem() instanceof AxeItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!level.isClientSide) {
            boolean ext = state.getValue(EXTENDED_TOP);
            if (level instanceof ServerLevel server) {
                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 1.02, pos.getZ() + 0.5, 12, 0.2, 0.0, 0.2, 0.08);
            }
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            level.setBlock(pos, state.setValue(EXTENDED_TOP, !ext), 3);
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos p = ctx.getClickedPos();
        BlockGetter level = ctx.getLevel();
        boolean n = isSupport(level.getBlockState(p.relative(Direction.NORTH)), Direction.NORTH);
        boolean s = isSupport(level.getBlockState(p.relative(Direction.SOUTH)), Direction.SOUTH);
        boolean w = isSupport(level.getBlockState(p.relative(Direction.WEST)), Direction.WEST);
        boolean e = isSupport(level.getBlockState(p.relative(Direction.EAST)), Direction.EAST);
        return this.defaultBlockState()
                .setValue(NORTH_CONNECTED, n)
                .setValue(SOUTH_CONNECTED, s)
                .setValue(WEST_CONNECTED, w)
                .setValue(EAST_CONNECTED, e);
    }

    @Override
    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState adj, LevelAccessor level, BlockPos pos, BlockPos pos2) {
        if (dir == Direction.NORTH) return state.setValue(NORTH_CONNECTED, isSupport(adj, Direction.NORTH));
        if (dir == Direction.SOUTH) return state.setValue(SOUTH_CONNECTED, isSupport(adj, Direction.SOUTH));
        if (dir == Direction.WEST) return state.setValue(WEST_CONNECTED, isSupport(adj, Direction.WEST));
        if (dir == Direction.EAST) return state.setValue(EAST_CONNECTED, isSupport(adj, Direction.EAST));
        return state;
    }

    private static boolean isSupport(BlockState state, Direction fromPillarDirection) {
        if (!(state.getBlock() instanceof SupportBlock)) return false;
        if (!state.hasProperty(SupportBlock.FACING)) return false;
        if (!state.hasProperty(SupportBlock.CONNECTED)) return false;

        Direction supportFacing = state.getValue(SupportBlock.FACING);
        boolean supportConnected = state.getValue(SupportBlock.CONNECTED);

        if (!supportConnected) return false;
        return supportFacing == fromPillarDirection.getOpposite();
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