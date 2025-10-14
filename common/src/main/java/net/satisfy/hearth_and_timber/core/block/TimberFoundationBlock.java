package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFoundationBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimberFoundationBlock extends Block implements EntityBlock, SimpleWaterloggedBlock {
    public static final MapCodec<TimberFoundationBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(BlockState.CODEC.fieldOf("base_state").forGetter(b -> b.baseState), propertiesCodec()).apply(instance, TimberFoundationBlock::new));
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final EnumProperty<StairsShape> SHAPE = BlockStateProperties.STAIRS_SHAPE;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");
    protected static final VoxelShape BASE_STRAIGHT = Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape BASE_INNER = Shapes.or(Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 16.0), Block.box(8.0, 0.0, 0.0, 16.0, 16.0, 8.0));
    protected static final VoxelShape BASE_OUTER = Block.box(0.0, 0.0, 0.0, 8.0, 16.0, 8.0);
    protected static final VoxelShape[] STRAIGHT_BY_FACING = new VoxelShape[]{
            rotateY(BASE_STRAIGHT, Direction.NORTH),
            rotateY(BASE_STRAIGHT, Direction.EAST),
            rotateY(BASE_STRAIGHT, Direction.SOUTH),
            rotateY(BASE_STRAIGHT, Direction.WEST)
    };
    protected static final VoxelShape[] INNER_LEFT_BY_FACING = new VoxelShape[]{
            rotateY(BASE_INNER, Direction.NORTH),
            rotateY(BASE_INNER, Direction.EAST),
            rotateY(BASE_INNER, Direction.SOUTH),
            rotateY(BASE_INNER, Direction.WEST)
    };
    protected static final VoxelShape[] INNER_RIGHT_BY_FACING = new VoxelShape[]{
            rotateY(mirrorX(BASE_INNER), Direction.NORTH),
            rotateY(mirrorX(BASE_INNER), Direction.EAST),
            rotateY(mirrorX(BASE_INNER), Direction.SOUTH),
            rotateY(mirrorX(BASE_INNER), Direction.WEST)
    };
    protected static final VoxelShape[] OUTER_LEFT_BY_FACING = new VoxelShape[]{
            rotateY(BASE_OUTER, Direction.NORTH),
            rotateY(BASE_OUTER, Direction.EAST),
            rotateY(BASE_OUTER, Direction.SOUTH),
            rotateY(BASE_OUTER, Direction.WEST)
    };
    protected static final VoxelShape[] OUTER_RIGHT_BY_FACING = new VoxelShape[]{
            rotateY(mirrorX(BASE_OUTER), Direction.NORTH),
            rotateY(mirrorX(BASE_OUTER), Direction.EAST),
            rotateY(mirrorX(BASE_OUTER), Direction.SOUTH),
            rotateY(mirrorX(BASE_OUTER), Direction.WEST)
    };
    private final Block base;
    protected final BlockState baseState;

    public TimberFoundationBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, false).setValue(APPLIED, false));
        this.base = baseState.getBlock();
        this.baseState = baseState;
    }

    public @NotNull MapCodec<? extends TimberFoundationBlock> codec() {
        return CODEC;
    }

    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        int idx = switch (direction) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        return switch (state.getValue(SHAPE)) {
            case STRAIGHT -> STRAIGHT_BY_FACING[idx];
            case INNER_LEFT -> INNER_LEFT_BY_FACING[idx];
            case INNER_RIGHT -> INNER_RIGHT_BY_FACING[idx];
            case OUTER_LEFT -> OUTER_LEFT_BY_FACING[idx];
            case OUTER_RIGHT -> OUTER_RIGHT_BY_FACING[idx];
        };
    }

    private static VoxelShape rotateY(VoxelShape shape, Direction direction) {
        int r = switch (direction) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        VoxelShape s = shape;
        for (int i = 0; i < r; i++) s = rotateOnceY(s);
        return s;
    }

    private static VoxelShape rotateOnceY(VoxelShape shape) {
        VoxelShape out = Shapes.empty();
        for (AABB a : shape.toAabbs()) {
            out = Shapes.or(out, Block.box((1.0 - a.maxZ) * 16.0, a.minY * 16.0, a.minX * 16.0, (1.0 - a.minZ) * 16.0, a.maxY * 16.0, a.maxX * 16.0));
        }
        return out;
    }

    private static VoxelShape mirrorX(VoxelShape shape) {
        VoxelShape out = Shapes.empty();
        for (AABB a : shape.toAabbs()) {
            out = Shapes.or(out, Block.box((1.0 - a.maxX) * 16.0, a.minY * 16.0, a.minZ * 16.0, (1.0 - a.minX) * 16.0, a.maxY * 16.0, a.maxZ * 16.0));
        }
        return out;
    }

    public float getExplosionResistance() {
        return this.base.getExplosionResistance();
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos pos = context.getClickedPos();
        FluidState fluid = context.getLevel().getFluidState(pos);
        BlockState state = this.defaultBlockState().setValue(FACING, context.getHorizontalDirection()).setValue(WATERLOGGED, fluid.getType() == Fluids.WATER).setValue(APPLIED, false);
        return state.setValue(SHAPE, getShapeFor(state, context.getLevel(), pos));
    }

    protected @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState neighbor, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return dir.getAxis().isHorizontal() ? state.setValue(SHAPE, getShapeFor(state, level, pos)) : super.updateShape(state, dir, neighbor, level, pos, neighborPos);
    }

    private static StairsShape getShapeFor(BlockState state, BlockGetter level, BlockPos pos) {
        Direction facing = state.getValue(FACING);
        BlockState front = level.getBlockState(pos.relative(facing));
        if (isFoundation(front)) {
            Direction direction2 = front.getValue(FACING);
            if (direction2.getAxis() != facing.getAxis() && canTakeShape(state, level, pos, direction2.getOpposite())) {
                return direction2 == facing.getCounterClockWise() ? StairsShape.OUTER_LEFT : StairsShape.OUTER_RIGHT;
            }
        }
        BlockState back = level.getBlockState(pos.relative(facing.getOpposite()));
        if (isFoundation(back)) {
            Direction direction3 = back.getValue(FACING);
            if (direction3.getAxis() != facing.getAxis() && canTakeShape(state, level, pos, direction3)) {
                return direction3 == facing.getCounterClockWise() ? StairsShape.INNER_LEFT : StairsShape.INNER_RIGHT;
            }
        }
        return StairsShape.STRAIGHT;
    }

    private static boolean canTakeShape(BlockState state, BlockGetter level, BlockPos pos, Direction dir) {
        BlockState other = level.getBlockState(pos.relative(dir));
        return !isFoundation(other) || other.getValue(FACING) != state.getValue(FACING);
    }

    public static boolean isFoundation(BlockState state) {
        return state.getBlock() instanceof TimberFoundationBlock;
    }

    protected @NotNull BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    protected @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        Direction direction = state.getValue(FACING);
        StairsShape shape = state.getValue(SHAPE);
        switch (mirror) {
            case LEFT_RIGHT -> {
                if (direction.getAxis() == Axis.Z) {
                    return switch (shape) {
                        case INNER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case INNER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case OUTER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        default -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
            }
            case FRONT_BACK -> {
                if (direction.getAxis() == Axis.X) {
                    return switch (shape) {
                        case INNER_LEFT -> state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_LEFT);
                        case INNER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.INNER_RIGHT);
                        case OUTER_LEFT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_RIGHT);
                        case OUTER_RIGHT ->
                                state.rotate(Rotation.CLOCKWISE_180).setValue(SHAPE, StairsShape.OUTER_LEFT);
                        case STRAIGHT -> state.rotate(Rotation.CLOCKWISE_180);
                    };
                }
            }
        }
        return super.mirror(state, mirror);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SHAPE, WATERLOGGED, APPLIED);
    }

    protected @NotNull FluidState getFluidState(BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimberFoundationBlockEntity(pos, state);
    }

    private static boolean canAccept(BlockGetter level, BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) return false;
        if (state.getBlock() instanceof EntityBlock) return false;
        if (state.getBlock() instanceof TimberFrameBlock) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getShape(level, pos));
    }

    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof PickaxeItem) {
            if (!state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            if (level.isClientSide) return ItemInteractionResult.SUCCESS;
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof TimberFoundationBlockEntity fbe) {
                BlockState mimic = fbe.getMimicState();
                if (mimic != null && !mimic.isAir()) {
                    Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
                    fbe.setMimicState(Blocks.AIR.defaultBlockState());
                    level.setBlock(pos, state.setValue(APPLIED, false), 3);
                    if (level instanceof ServerLevel server) {
                        server.levelEvent(2001, pos, Block.getId(mimic));
                    }
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if (state.getValue(APPLIED)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (!(stack.getItem() instanceof BlockItem blockItem))
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        BlockState mimic = blockItem.getBlock().defaultBlockState();
        if (!canAccept(level, pos, mimic)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TimberFoundationBlockEntity fbe) {
            fbe.setMimicState(mimic);
            level.setBlock(pos, state.setValue(APPLIED, true), 3);
            if (level instanceof ServerLevel server) {
                server.levelEvent(2001, pos, Block.getId(mimic));
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TimberFoundationBlockEntity fbe) {
            BlockState mimic = fbe.getMimicState();
            if (mimic != null && !mimic.isAir() && !player.isCreative()) {
                Block.popResource(level, pos, new ItemStack(mimic.getBlock()));
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> list, TooltipFlag tooltipFlag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            list.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame_full.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}
