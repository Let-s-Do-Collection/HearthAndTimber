package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class TimberBaseSkirtBlock extends TimberFoundationBlock {
    public static final EnumProperty<Half> HALF = EnumProperty.create("half", Half.class);
    protected static final VoxelShape BASE_STRAIGHT = Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 8.0);
    protected static final VoxelShape BASE_INNER = Shapes.or(Block.box(0.0, 0.0, 0.0, 8.0, 2.0, 16.0), Block.box(8.0, 0.0, 0.0, 16.0, 2.0, 8.0));
    protected static final VoxelShape BASE_OUTER = Block.box(0.0, 0.0, 0.0, 8.0, 2.0, 8.0);
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

    public TimberBaseSkirtBlock(BlockState baseState, BlockBehaviour.Properties properties) {
        super(baseState, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SHAPE, StairsShape.STRAIGHT).setValue(WATERLOGGED, false).setValue(APPLIED, false).setValue(HALF, Half.BOTTOM));
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState s = super.getStateForPlacement(context);
        Direction face = context.getClickedFace();
        boolean top = face == Direction.DOWN || (face != Direction.UP && context.getClickLocation().y - context.getClickedPos().getY() > 0.5D);
        assert s != null;
        return s.setValue(HALF, top ? Half.TOP : Half.BOTTOM);
    }

    protected @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        int idx = switch (direction) {
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> 0;
        };
        VoxelShape shape = switch (state.getValue(SHAPE)) {
            case STRAIGHT -> STRAIGHT_BY_FACING[idx];
            case INNER_LEFT -> INNER_LEFT_BY_FACING[idx];
            case INNER_RIGHT -> INNER_RIGHT_BY_FACING[idx];
            case OUTER_LEFT -> OUTER_LEFT_BY_FACING[idx];
            case OUTER_RIGHT -> OUTER_RIGHT_BY_FACING[idx];
        };
        return state.getValue(HALF) == Half.TOP ? moveShapeUp(shape) : shape;
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

    private static VoxelShape moveShapeUp(VoxelShape shape) {
        VoxelShape out = Shapes.empty();
        for (AABB a : shape.toAabbs()) {
            out = Shapes.or(out, Block.box(a.minX * 16.0, a.minY * 16.0 + 14.0, a.minZ * 16.0, a.maxX * 16.0, a.maxY * 16.0 + 14.0, a.maxZ * 16.0));
        }
        return out;
    }
}
