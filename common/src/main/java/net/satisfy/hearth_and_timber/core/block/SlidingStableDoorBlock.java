package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PickaxeItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
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
import net.satisfy.hearth_and_timber.core.block.entity.SlidingDoorBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SlidingStableDoorBlock extends BaseEntityBlock {
    public static final MapCodec<SlidingStableDoorBlock> CODEC = simpleCodec(SlidingStableDoorBlock::new);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final EnumProperty<HingeSide> HINGE = EnumProperty.create("hinge", HingeSide.class);
    public static final EnumProperty<Half> PART = EnumProperty.create("part", Half.class);
    public static final BooleanProperty OPEN = BooleanProperty.create("open");
    private static final VoxelShape NORTH_SHAPE = Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 3.0 / 16.0);
    private static final VoxelShape SOUTH_SHAPE = Shapes.box(0.0, 0.0, 13.0 / 16.0, 1.0, 1.0, 1.0);
    private static final VoxelShape WEST_SHAPE = Shapes.box(0.0, 0.0, 0.0, 3.0 / 16.0, 1.0, 1.0);
    private static final VoxelShape EAST_SHAPE = Shapes.box(13.0 / 16.0, 0.0, 0.0, 1.0, 1.0, 1.0);

    public SlidingStableDoorBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(HINGE, HingeSide.LEFT).setValue(PART, Half.BOTTOM).setValue(OPEN, false));
    }

    @Override
    public @NotNull MapCodec<SlidingStableDoorBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, HINGE, PART, OPEN);
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
        if (!isReplaceable(level, origin)) return null;
        if (!isReplaceable(level, origin.above())) return null;
        return defaultBlockState().setValue(FACING, direction).setValue(HINGE, hinge).setValue(PART, Half.BOTTOM).setValue(OPEN, false);
    }

    private boolean isReplaceable(Level level, BlockPos position) {
        return level.getBlockState(position).canBeReplaced();
    }

    @Override
    public void setPlacedBy(Level level, BlockPos position, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction direction = state.getValue(FACING);
        HingeSide hinge = state.getValue(HINGE);
        boolean open = state.getValue(OPEN);
        level.setBlock(position.above(), state.setValue(PART, Half.TOP).setValue(HINGE, hinge).setValue(OPEN, open), 3);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos position, BlockState state, Player player) {
        if (!level.isClientSide) {
            BlockPos origin = resolveOrigin(position, state);
            destroyIfPresent(level, origin.above(), state);
            destroyIfPresent(level, origin, state);
        }
        super.playerWillDestroy(level, position, state, player);
        return state;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos position, BlockState newState, boolean isMoving) {
        if (!level.isClientSide && state.getBlock() != newState.getBlock()) {
            BlockPos origin = resolveOrigin(position, state);
            destroyIfPresent(level, origin.above(), state);
            destroyIfPresent(level, origin, state);
        }
        super.onRemove(state, level, position, newState, isMoving);
    }

    private Direction lateralDirection(Direction facing, HingeSide hinge) {
        return hinge == HingeSide.RIGHT ? facing.getClockWise() : facing.getCounterClockWise();
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockPos origin = resolveOrigin(pos, state);
        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof SlidingDoorBlockEntity door) {
            if (door.isReinforced() && stack.getItem() instanceof PickaxeItem) {
                if (!level.isClientSide) {
                    door.setReinforced(false);
                    popResource(level, origin, new ItemStack(Blocks.IRON_BLOCK));
                    if (level instanceof ServerLevel server) {
                        double x = origin.getX() + 0.5;
                        double y = origin.getY() + 0.5;
                        double z = origin.getZ() + 0.5;
                        server.playSound(null, origin, SoundEvents.ANVIL_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                        server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.defaultBlockState()), x, y, z, 20, 0.35, 0.3, 0.35, 0.02);
                    }
                }
                return level.isClientSide ? ItemInteractionResult.SUCCESS : ItemInteractionResult.CONSUME;
            }
            if (stack.is(Blocks.IRON_BLOCK.asItem())) {
                if (!level.isClientSide) {
                    if (!door.isReinforced()) {
                        door.setReinforced(true);
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        if (level instanceof ServerLevel server) {
                            double x = origin.getX() + 0.5;
                            double y = origin.getY() + 0.5;
                            double z = origin.getZ() + 0.5;
                            server.playSound(null, origin, SoundEvents.ANVIL_BREAK, SoundSource.BLOCKS, 1.0f, 1.0f);
                            server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.IRON_BLOCK.defaultBlockState()), x, y, z, 16, 0.35, 0.3, 0.35, 0.02);
                        }
                    }
                }
                return level.isClientSide ? ItemInteractionResult.SUCCESS : ItemInteractionResult.CONSUME;
            }
            if (door.isReinforced()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean changed = tryRecolorWithPlanks(level, pos, player, hand, stack);
        if (changed) return level.isClientSide ? ItemInteractionResult.SUCCESS : ItemInteractionResult.CONSUME;
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isShiftKeyDown()) return InteractionResult.PASS;
        if (level.isClientSide) return InteractionResult.SUCCESS;
        BlockPos origin = resolveOrigin(pos, state);
        BlockPos other = findOppositeOrigin(level, origin, state);
        boolean open = !level.getBlockState(origin).getValue(OPEN);
        if (other != null) {
            Direction f1 = state.getValue(FACING);
            HingeSide h1 = state.getValue(HINGE);
            setOpenFlag(level, origin, f1, h1, open);
            BlockState s2 = level.getBlockState(other);
            if (s2.getBlock() == this) {
                Direction f2 = s2.getValue(FACING);
                HingeSide h2 = s2.getValue(HINGE);
                setOpenFlag(level, other, f2, h2, open);
            }
            return InteractionResult.SUCCESS;
        } else {
            Direction f = state.getValue(FACING);
            HingeSide h = state.getValue(HINGE);
            setOpenFlag(level, origin, f, h, open);
            return InteractionResult.SUCCESS;
        }
    }

    private BlockPos findOppositeOrigin(Level level, BlockPos origin, BlockState state) {
        Direction direction = state.getValue(FACING);
        for (int d = 1; d <= 3; d++) {
            BlockPos p = origin.relative(direction, d);
            BlockState s = level.getBlockState(p);
            if (s.getBlock() == this && s.getValue(FACING) == direction.getOpposite()) return resolveOrigin(p, s);
            BlockPos p2 = origin.relative(direction.getOpposite(), d);
            BlockState s2 = level.getBlockState(p2);
            if (s2.getBlock() == this && s2.getValue(FACING) == direction.getOpposite()) return resolveOrigin(p2, s2);
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Half part = state.getValue(PART);
        if (part == Half.TOP) return level.getBlockState(pos.below()).getBlock() == this;
        return Block.canSupportCenter(level, pos.below(), Direction.UP);
    }

    private void setOpenFlag(Level level, BlockPos origin, Direction facing, HingeSide hinge, boolean open) {
        BlockPos topPos = origin.above();
        BlockState bl = level.getBlockState(origin);
        if (bl.getBlock() == this) level.setBlock(origin, bl.setValue(OPEN, open), 3);
        BlockState tl = level.getBlockState(topPos);
        if (tl.getBlock() == this) level.setBlock(topPos, tl.setValue(OPEN, open), 3);
        BlockEntity be = level.getBlockEntity(origin);
        if (be instanceof SlidingDoorBlockEntity d) d.setOpen(open, true);
    }

    private void destroyIfPresent(Level level, BlockPos position, BlockState referenceState) {
        BlockState at = level.getBlockState(position);
        if (at.getBlock() == this && at.getValue(FACING) == referenceState.getValue(FACING) && at.getValue(HINGE) == referenceState.getValue(HINGE)) {
            level.destroyBlock(position, true);
        }
    }

    private BlockPos resolveOrigin(BlockPos position, BlockState state) {
        Half part = state.getValue(PART);
        if (part == Half.BOTTOM) return position;
        return position.below();
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos position, CollisionContext context) {
        Direction facing = state.getValue(FACING);
        HingeSide hinge = state.getValue(HINGE);
        boolean open = state.getValue(OPEN);
        if (!open) {
            if (facing == Direction.NORTH) return NORTH_SHAPE;
            if (facing == Direction.SOUTH) return SOUTH_SHAPE;
            if (facing == Direction.WEST) return WEST_SHAPE;
            return EAST_SHAPE;
        }
        Direction lateral = lateralDirection(facing, hinge);
        double t = 3.0 / 16.0;
        double w = 6.0 / 16.0;
        double d = 1.0 / 16.0;
        if (facing == Direction.NORTH) {
            if (lateral == Direction.WEST) return Shapes.box(0.0, 0.0, d, w, 1.0, d + t);
            return Shapes.box(1.0 - w, 0.0, d, 1.0, 1.0, d + t);
        }
        if (facing == Direction.SOUTH) {
            if (lateral == Direction.WEST) return Shapes.box(0.0, 0.0, 1.0 - d - t, w, 1.0, 1.0 - d);
            return Shapes.box(1.0 - w, 0.0, 1.0 - d - t, 1.0, 1.0, 1.0 - d);
        }
        if (facing == Direction.WEST) {
            if (lateral == Direction.NORTH) return Shapes.box(d, 0.0, 0.0, d + t, 1.0, w);
            return Shapes.box(d, 0.0, 1.0 - w, d + t, 1.0, 1.0);
        }
        if (lateral == Direction.NORTH) return Shapes.box(1.0 - d - t, 0.0, 0.0, 1.0 - d, 1.0, w);
        return Shapes.box(1.0 - d - t, 0.0, 1.0 - w, 1.0 - d, 1.0, 1.0);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos position, BlockState state) {
        return new SlidingDoorBlockEntity(position, state);
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (type != EntityTypeRegistry.SLIDING_DOOR_BLOCK_ENTITY.get()) return null;
        if (level.isClientSide) return null;
        return (srv, pos, st, be) -> {
            if (be instanceof SlidingDoorBlockEntity e) SlidingDoorBlockEntity.serverTick(srv, pos, st, e);
        };
    }

    public enum HingeSide implements StringRepresentable {
        LEFT("left"), RIGHT("right");
        private final String name;
        HingeSide(String name) { this.name = name; }
        @Override public @NotNull String getSerializedName() { return name; }
        @Override public String toString() { return name; }
    }

    public enum Half implements StringRepresentable {
        TOP("top"), BOTTOM("bottom");
        private final String name;
        Half(String name) { this.name = name; }
        @Override public @NotNull String getSerializedName() { return name; }
        @Override public String toString() { return name; }
    }

    private static String woodKeyFromPlanks(BlockState plankState) {
        if (plankState.is(Blocks.OAK_PLANKS)) return "oak";
        if (plankState.is(Blocks.SPRUCE_PLANKS)) return "spruce";
        if (plankState.is(Blocks.BIRCH_PLANKS)) return "birch";
        if (plankState.is(Blocks.JUNGLE_PLANKS)) return "jungle";
        if (plankState.is(Blocks.ACACIA_PLANKS)) return "acacia";
        if (plankState.is(Blocks.DARK_OAK_PLANKS)) return "dark_oak";
        if (plankState.is(Blocks.CHERRY_PLANKS)) return "cherry";
        if (plankState.is(Blocks.MANGROVE_PLANKS)) return "mangrove";
        if (plankState.is(Blocks.BAMBOO_PLANKS)) return "bamboo";
        if (plankState.is(Blocks.CRIMSON_PLANKS)) return "crimson";
        if (plankState.is(Blocks.WARPED_PLANKS)) return "warped";
        return null;
    }

    private static BlockState planksFromWoodKey(String key) {
        return switch (key) {
            case "oak" -> Blocks.OAK_PLANKS.defaultBlockState();
            case "birch" -> Blocks.BIRCH_PLANKS.defaultBlockState();
            case "jungle" -> Blocks.JUNGLE_PLANKS.defaultBlockState();
            case "acacia" -> Blocks.ACACIA_PLANKS.defaultBlockState();
            case "dark_oak" -> Blocks.DARK_OAK_PLANKS.defaultBlockState();
            case "cherry" -> Blocks.CHERRY_PLANKS.defaultBlockState();
            case "mangrove" -> Blocks.MANGROVE_PLANKS.defaultBlockState();
            case "bamboo" -> Blocks.BAMBOO_PLANKS.defaultBlockState();
            case "crimson" -> Blocks.CRIMSON_PLANKS.defaultBlockState();
            case "warped" -> Blocks.WARPED_PLANKS.defaultBlockState();
            default -> Blocks.SPRUCE_PLANKS.defaultBlockState();
        };
    }

    private boolean tryRecolorWithPlanks(Level level, BlockPos pos, Player player, InteractionHand hand, ItemStack stack) {
        ItemStack held = stack;
        BlockItem blockItem = held.getItem() instanceof BlockItem b ? b : null;
        if (blockItem == null) {
            ItemStack other = hand == InteractionHand.MAIN_HAND ? player.getOffhandItem() : player.getMainHandItem();
            blockItem = other.getItem() instanceof BlockItem b2 ? b2 : null;
            if (blockItem == null) return false;
            held = other;
        }
        BlockPos origin = resolveOrigin(pos, level.getBlockState(pos));
        BlockEntity be = level.getBlockEntity(origin);
        if (!(be instanceof SlidingDoorBlockEntity door)) return false;
        if (door.isReinforced()) return false;
        BlockState plankState = blockItem.getBlock().defaultBlockState();
        String newKey = woodKeyFromPlanks(plankState);
        if (newKey == null) return false;
        String oldKey = door.getWood();
        if (newKey.equals(oldKey)) return true;
        if (!level.isClientSide) {
            if (level instanceof ServerLevel server) {
                BlockState oldPlanks = planksFromWoodKey(oldKey);
                BlockPos[] points = new BlockPos[] { origin, origin.above() };
                for (BlockPos p : points) {
                    double cx = p.getX() + 0.5;
                    double cy = p.getY() + 0.1;
                    double cz = p.getZ() + 0.5;
                    server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, oldPlanks), cx, cy, cz, 18, 0.35, 0.25, 0.35, 0.03);
                    server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, plankState), cx, cy, cz, 18, 0.35, 0.25, 0.35, 0.03);
                }
                server.playSound(null, origin, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0f, 0.95f + server.random.nextFloat() * 0.1f);
            }
            door.setWood(newKey);
            if (!player.getAbilities().instabuild) held.shrink(1);
        }
        return true;
    }
}