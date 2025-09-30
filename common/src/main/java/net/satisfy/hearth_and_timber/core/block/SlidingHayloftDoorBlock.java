package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.SlidingHayloftDoorBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlidingHayloftDoorBlock extends BaseEntityBlock {
    public static final MapCodec<SlidingHayloftDoorBlock> CODEC = simpleCodec(SlidingHayloftDoorBlock::new);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final EnumProperty<Quarter> PART = EnumProperty.create("part", Quarter.class);
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 3.0 / 16.0);
    private static final VoxelShape SOUTH_SHAPE = Shapes.box(0.0, 0.0, 13.0 / 16.0, 1.0, 1.0, 1.0);
    private static final VoxelShape WEST_SHAPE = Shapes.box(0.0, 0.0, 0.0, 3.0 / 16.0, 1.0, 1.0);
    private static final VoxelShape EAST_SHAPE = Shapes.box(13.0 / 16.0, 0.0, 0.0, 1.0, 1.0, 1.0);
    public static final EnumProperty<HingeSide> HINGE = EnumProperty.create("hinge", HingeSide.class);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");

    public SlidingHayloftDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, Quarter.BL).setValue(HINGE, HingeSide.LEFT).setValue(OPEN, false));
    }

    @Override
    public @NotNull MapCodec<SlidingHayloftDoorBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART, HINGE, OPEN);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction direction = context.getHorizontalDirection().getOpposite();
        BlockPos origin = context.getClickedPos();
        Level level = context.getLevel();
        double hitX = context.getClickLocation().x - origin.getX();
        double hitZ = context.getClickLocation().z - origin.getZ();
        HingeSide hinge;
        switch (direction) {
            case NORTH -> hinge = hitX > 0.5 ? HingeSide.RIGHT : HingeSide.LEFT;
            case SOUTH -> hinge = hitX > 0.5 ? HingeSide.LEFT : HingeSide.RIGHT;
            case WEST -> hinge = hitZ > 0.5 ? HingeSide.LEFT : HingeSide.RIGHT;
            default -> hinge = hitZ > 0.5 ? HingeSide.RIGHT : HingeSide.LEFT;
        }
        Direction lateral = lateralDirection(direction, hinge);
        BlockPos positionTopLeft = origin.above();
        BlockPos positionBottomRight = origin.relative(lateral);
        BlockPos positionTopRight = positionBottomRight.above();
        if (!isReplaceable(level, origin)) return null;
        if (!isReplaceable(level, positionTopLeft)) return null;
        if (!isReplaceable(level, positionBottomRight)) return null;
        if (!isReplaceable(level, positionTopRight)) return null;
        return defaultBlockState().setValue(FACING, direction).setValue(PART, Quarter.BL).setValue(HINGE, hinge).setValue(OPEN, false);
    }

    private boolean isReplaceable(Level level, BlockPos position) {
        return level.getBlockState(position).canBeReplaced();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos position, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(FACING);
        HingeSide hinge = state.getValue(HINGE);
        boolean open = state.getValue(OPEN);
        level.setBlock(position.above(), state.setValue(PART, Quarter.TL).setValue(HINGE, hinge).setValue(OPEN, open), 3);
        BlockPos positionBottomRight = position.relative(lateralDirection(direction, hinge));
        level.setBlock(positionBottomRight, state.setValue(PART, Quarter.BR).setValue(HINGE, hinge).setValue(OPEN, open), 3);
        level.setBlock(positionBottomRight.above(), state.setValue(PART, Quarter.TR).setValue(HINGE, hinge).setValue(OPEN, open), 3);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos position, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos origin = resolveOrigin(position, state);
            Direction direction = state.getValue(FACING);
            HingeSide hinge = state.getValue(HINGE);
            Direction lateral = lateralDirection(direction, hinge);
            destroyIfPresent(level, origin.above(), state);
            BlockPos positionBottomRight = origin.relative(lateral);
            destroyIfPresent(level, positionBottomRight, state);
            destroyIfPresent(level, positionBottomRight.above(), state);
            destroyIfPresent(level, origin, state);
        }
        super.playerWillDestroy(level, position, state, player);
        return state;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos position, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
            BlockPos origin = resolveOrigin(position, state);
            Direction direction = state.getValue(FACING);
            HingeSide hinge = state.getValue(HINGE);
            Direction lateral = lateralDirection(direction, hinge);
            destroyIfPresent(level, origin.above(), state);
            BlockPos positionBottomRight = origin.relative(lateral);
            destroyIfPresent(level, positionBottomRight, state);
            destroyIfPresent(level, positionBottomRight.above(), state);
            destroyIfPresent(level, origin, state);
        }
        super.onRemove(state, level, position, newState, isMoving);
    }

    private Direction lateralDirection(Direction facing, HingeSide hinge) {
        return hinge == HingeSide.RIGHT ? facing.getClockWise() : facing.getCounterClockWise();
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockPos blockPos = resolveOrigin(pos, state);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SlidingHayloftDoorBlockEntity slidingHayloftDoorBlockEntity) slidingHayloftDoorBlockEntity.setOpen(!slidingHayloftDoorBlockEntity.isOpen());
        return ItemInteractionResult.SUCCESS;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos blockPos = resolveOrigin(pos, state);
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof SlidingHayloftDoorBlockEntity slidingHayloftDoorBlockEntity) slidingHayloftDoorBlockEntity.setOpen(!slidingHayloftDoorBlockEntity.isOpen());
        return InteractionResult.SUCCESS;
    }

    private void destroyIfPresent(Level level, BlockPos position, BlockState referenceState) {
        BlockState at = level.getBlockState(position);
        if (at.getBlock() == this
                && at.getValue(FACING) == referenceState.getValue(FACING)
                && at.getValue(HINGE) == referenceState.getValue(HINGE)) {
            level.destroyBlock(position, true);
        }
    }

    private BlockPos resolveOrigin(BlockPos position, BlockState state) {
        Direction direction = state.getValue(FACING);
        HingeSide hinge = state.getValue(HINGE);
        Direction lateral = lateralDirection(direction, hinge);
        Quarter part = state.getValue(PART);
        if (part == Quarter.BL) return position;
        if (part == Quarter.TL) return position.below();
        if (part == Quarter.BR) return position.relative(lateral.getOpposite());
        return position.below().relative(lateral.getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos position, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        Quarter part = state.getValue(PART);
        HingeSide hinge = state.getValue(HINGE);
        boolean open = state.getValue(OPEN);
        if (!open) {
            if (facing == Direction.NORTH) return NORTH_SHAPE;
            if (facing == Direction.SOUTH) return SOUTH_SHAPE;
            if (facing == Direction.WEST) return WEST_SHAPE;
            return EAST_SHAPE;
        }
        if (hinge == HingeSide.LEFT) {
            if (part == Quarter.BR || part == Quarter.TR)
                return barShape(facing, lateralDirection(facing, HingeSide.LEFT));
            return Shapes.empty();
        } else {
            if (part == Quarter.BL || part == Quarter.TL)
                return barShape(facing, lateralDirection(facing, HingeSide.RIGHT));
            return Shapes.empty();
        }
    }

    private static VoxelShape barShape(Direction facing, Direction lateral) {
        double t = 3.0 / 16.0;
        if (facing == Direction.NORTH) {
            double z0 = 0.0;
            if (lateral == Direction.WEST) return Shapes.box(0.0, 0.0, z0, t, 1.0, t);
            return Shapes.box(1.0 - t, 0.0, z0, 1.0, 1.0, t);
        }
        if (facing == Direction.SOUTH) {
            double z0 = 1.0 - t, z1 = 1.0;
            if (lateral == Direction.WEST) return Shapes.box(0.0, 0.0, z0, t, 1.0, z1);
            return Shapes.box(1.0 - t, 0.0, z0, 1.0, 1.0, z1);
        }
        if (facing == Direction.WEST) {
            double x0 = 0.0;
            if (lateral == Direction.NORTH) return Shapes.box(x0, 0.0, 0.0, t, 1.0, t);
            return Shapes.box(x0, 0.0, 1.0 - t, t, 1.0, 1.0);
        }
        double x0 = 1.0 - t, x1 = 1.0;
        if (lateral == Direction.NORTH) return Shapes.box(x0, 0.0, 0.0, x1, 1.0, t);
        return Shapes.box(x0, 0.0, 1.0 - t, x1, 1.0, 1.0);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos position, BlockState state) {
        return new SlidingHayloftDoorBlockEntity(position, state);
    }

    private void setOpenFlag(Level level, BlockPos origin, Direction facing, HingeSide hinge, boolean open) {
        BlockState bl = level.getBlockState(origin);
        BlockPos tlPos = origin.above();
        BlockPos brPos = origin.relative(lateralDirection(facing, hinge));
        BlockPos trPos = brPos.above();
        if (bl.getBlock() == this) level.setBlock(origin, bl.setValue(OPEN, open), 3);
        BlockState tl = level.getBlockState(tlPos);
        if (tl.getBlock() == this) level.setBlock(tlPos, tl.setValue(OPEN, open), 3);
        BlockState br = level.getBlockState(brPos);
        if (br.getBlock() == this) level.setBlock(brPos, br.setValue(OPEN, open), 3);
        BlockState tr = level.getBlockState(trPos);
        if (tr.getBlock() == this) level.setBlock(trPos, tr.setValue(OPEN, open), 3);
        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof SlidingHayloftDoorBlockEntity d) d.setOpen(open);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (!level.isClientSide) return null;
        if (type != EntityTypeRegistry.SLIDING_HAYLOFT_DOOR_BLOCK_ENTITY.get()) return null;
        return (lvl, pos, st, be) -> {
            if (be instanceof SlidingHayloftDoorBlockEntity e) SlidingHayloftDoorBlockEntity.clientTick(lvl, pos, st, e);
        };
    }

    public enum Quarter implements StringRepresentable {
        TL("tl"), TR("tr"), BL("bl"), BR("br");
        private final String serialized;

        Quarter(String serialized) {
            this.serialized = serialized;
        }

        @Override
        public @NotNull String getSerializedName() {
            return serialized;
        }

        @Override
        public String toString() {
            return serialized;
        }
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    public enum HingeSide implements StringRepresentable {
        LEFT("left"), RIGHT("right");
        private final String name;

        HingeSide(String name) {
            this.name = name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return name;
        }

        @Override
        public String toString() {
            return name;
        }
    }
}