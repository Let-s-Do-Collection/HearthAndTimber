package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.BathtubBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BathtubBlock extends Block implements EntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final net.minecraft.world.level.block.state.properties.EnumProperty<BedPart> PART = BlockStateProperties.BED_PART;
    public static final BooleanProperty FILLED = BooleanProperty.create("filled");
    public static final BooleanProperty FILLING = BooleanProperty.create("filling");
    private static final VoxelShape HEAD_N = Shapes.or(Shapes.box(0, 0, 0, 1, 0.5, 1), Shapes.box(0.0625, 0.5, 0.0625, 0.9375, 0.625, 0.9375));
    private static final VoxelShape FOOT_N = Shapes.or(Shapes.box(0, 0, 0, 1, 0.5, 1));

    public BathtubBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(PART, BedPart.HEAD).setValue(FILLED, false).setValue(FILLING, false));
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction dir = ctx.getHorizontalDirection();
        BlockPos pos = ctx.getClickedPos();
        BlockPos other = pos.relative(dir.getOpposite());
        if (!ctx.getLevel().getBlockState(other).canBeReplaced(ctx)) return null;
        return defaultBlockState().setValue(FACING, dir).setValue(PART, BedPart.HEAD).setValue(FILLED, false).setValue(FILLING, false);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        Direction dir = state.getValue(FACING);
        BlockPos footPos = pos.relative(dir.getOpposite());
        level.setBlock(footPos, state.setValue(PART, BedPart.FOOT), 3);
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide && player.isCreative()) {
            BedPart part = state.getValue(PART);
            if (part == BedPart.HEAD) {
                BlockPos foot = pos.relative(state.getValue(FACING).getOpposite());
                BlockState fs = level.getBlockState(foot);
                if (fs.is(this) && fs.getValue(PART) == BedPart.FOOT) {
                    level.setBlock(foot, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, foot, Block.getId(fs));
                }
            } else {
                BlockPos head = pos.relative(state.getValue(FACING));
                BlockState hs = level.getBlockState(head);
                if (hs.is(this) && hs.getValue(PART) == BedPart.HEAD) {
                    level.setBlock(head, Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(player, 2001, head, Block.getId(hs));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
        return state;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return true;
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, Direction dir, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction facing = state.getValue(FACING);
        Direction needed = state.getValue(PART) == BedPart.HEAD ? facing.getOpposite() : facing;
        if (dir == needed) {
            if (!neighborState.is(this) || neighborState.getValue(PART) == state.getValue(PART)) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return rotate(state, mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> b) {
        b.add(FACING, PART, FILLED, FILLING);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        Direction f = state.getValue(FACING);
        VoxelShape base = state.getValue(PART) == BedPart.HEAD ? HEAD_N : FOOT_N;
        return switch (f) {
            case SOUTH -> rotateY(base, 2);
            case WEST -> rotateY(base, 1);
            case EAST -> rotateY(base, 3);
            default -> base;
        };
    }

    private VoxelShape rotateY(VoxelShape shape, int steps) {
        VoxelShape out = shape;
        for (int i = 0; i < steps; i++) {
            VoxelShape tmp = Shapes.empty();
            for (var a : out.toAabbs()) {
                double x1 = a.minX, y1 = a.minY, z1 = a.minZ, x2 = a.maxX, y2 = a.maxY, z2 = a.maxZ;
                tmp = Shapes.or(tmp, Shapes.box(1 - z2, y1, x1, 1 - z1, y2, x2));
            }
            out = tmp.optimize();
        }
        return out;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (level.isClientSide) return ItemInteractionResult.SUCCESS;
        boolean isHead = state.getValue(PART) == BedPart.HEAD;
        BlockPos head = isHead ? pos : pos.relative(state.getValue(FACING));
        BlockState headState = level.getBlockState(head);
        if (!headState.is(this) || headState.getValue(PART) != BedPart.HEAD) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        boolean filled = headState.getValue(FILLED);
        boolean filling = headState.getValue(FILLING);
        if (hit.getBlockPos().equals(head) && hit.getDirection() != Direction.DOWN && stack.isEmpty() && !filled && !filling) {
            BlockEntity be = level.getBlockEntity(head);
            if (be instanceof BathtubBlockEntity tub) {
                tub.startFilling(45 * 20);
                setFillState(level, head, true, false);
                level.playSound(null, head, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
            }
            return ItemInteractionResult.CONSUME;
        }
        if ((stack.is(Items.WATER_BUCKET) || stack.is(Items.GLASS_BOTTLE)) && !filled && !filling) {
            BlockEntity be = level.getBlockEntity(head);
            if (be instanceof BathtubBlockEntity tub) {
                tub.startFilling(45 * 20);
                setFillState(level, head, true, false);
                level.playSound(null, head, SoundEvents.BUCKET_EMPTY, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    if (stack.is(Items.WATER_BUCKET)) {
                        stack.shrink(1);
                        player.addItem(new ItemStack(Items.BUCKET));
                    } else {
                        stack.shrink(1);
                        player.addItem(new ItemStack(Items.GLASS_BOTTLE));
                    }
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if ((stack.is(Items.BUCKET) || stack.is(Items.GLASS_BOTTLE)) && (filled || filling)) {
            BlockEntity be = level.getBlockEntity(head);
            if (be instanceof BathtubBlockEntity tub) {
                tub.abortFilling();

                setFillState(level, head, false, false);
                level.playSound(null, head, SoundEvents.BUCKET_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (!player.isCreative()) {
                    if (stack.is(Items.BUCKET)) {
                        stack.shrink(1);
                        player.addItem(new ItemStack(Items.WATER_BUCKET));
                    } else {
                        stack.shrink(1);
                        PotionContents pc = new PotionContents(Potions.WATER);
                        ItemStack ps = new ItemStack(Items.POTION);
                        ps.set(DataComponents.POTION_CONTENTS, pc);
                        player.addItem(ps);
                    }
                }
            }
            return ItemInteractionResult.CONSUME;
        }
        if (filled && player.getVehicle() == null) {
            return GeneralUtil.onUse(level, player, hand, hit, 0.2D);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private void setFillState(Level level, BlockPos head, boolean filling, boolean filled) {
        BlockState hs = level.getBlockState(head).setValue(FILLING, filling).setValue(FILLED, filled);
        level.setBlock(head, hs, Block.UPDATE_ALL);
        Direction f = hs.getValue(FACING);
        BlockPos foot = head.relative(f.getOpposite());
        BlockState fs = level.getBlockState(foot);
        if (fs.is(this) && fs.getValue(PART) == BedPart.FOOT) level.setBlock(foot, fs.setValue(FILLING, filling).setValue(FILLED, filled), Block.UPDATE_ALL);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource r) {
        Direction f = state.getValue(FACING);
        boolean isHead = state.getValue(PART) == BedPart.HEAD;
        BlockPos headPos = isHead ? pos : pos.relative(f);
        BlockState headState = level.getBlockState(headPos);
        if (!headState.is(this) || headState.getValue(PART) != BedPart.HEAD) return;

        double base = 0.475;
        double height = 0.46;
        double ratio = headState.getValue(FILLED) ? 1.0 : 0.0;
        BlockEntity be = level.getBlockEntity(headPos);
        if (be instanceof BathtubBlockEntity tub) ratio = Mth.clamp(tub.getFillRatio(0), 0.0f, 1.0f);
        double ySurface = headPos.getY() + base + height * ratio + 0.02;

        if (headState.getValue(FILLING) && isHead) {
            double cx = headPos.getX() + 0.5 + f.getStepX() * 0.26;
            double cz = headPos.getZ() + 0.5 + f.getStepZ() * 0.26;
            double rx = -f.getStepZ();
            double rz = f.getStepX();
            double spx = cx + rx * 0.18;
            double spz = cz + rz * 0.18;
            double sy = headPos.getY() + 1.06;
            int seg = 11;
            double dy = (sy - ySurface) / seg;
            for (int i = 0; i < seg; i++) {
                double py = sy - i * dy;
                double ox = (r.nextDouble() - 0.5) * 0.01;
                double oz = (r.nextDouble() - 0.5) * 0.01;
                level.addParticle(ParticleTypes.FALLING_WATER, spx + ox, py, spz + oz, 0, -0.18, 0);
            }
            for (int i = 0; i < 4; i++) {
                double ix = spx + (r.nextDouble() - 0.5) * 0.10;
                double iz = spz + (r.nextDouble() - 0.5) * 0.10;
                double vy = 0.12 + r.nextDouble() * 0.06;
                double vx = (r.nextDouble() - 0.5) * 0.03;
                double vz = (r.nextDouble() - 0.5) * 0.03;
                level.addParticle(ParticleTypes.SPLASH, ix, ySurface, iz, vx, vy, vz);
            }
        }

        if (headState.getValue(FILLING) || headState.getValue(FILLED)) {
            for (int i = 0; i < 2; i++) {
                double x = pos.getX() + 0.5 + (r.nextDouble() - 0.5) * 0.6;
                double z = pos.getZ() + 0.5 + (r.nextDouble() - 0.5) * 0.9;
                level.addParticle(ParticleTypes.BUBBLE, x, ySurface - 0.005, z, 0, 0.006 + r.nextDouble() * 0.01, 0);
                if (r.nextFloat() < 0.15f) level.addParticle(ParticleTypes.BUBBLE_POP, x, ySurface + 0.01, z, 0, 0, 0);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        BlockPos headPos = state.getValue(PART) == BedPart.HEAD ? pos : pos.relative(state.getValue(FACING));
        BlockState headState = level.getBlockState(headPos);

        if (!headState.is(this)) return;

        boolean filled = headState.getValue(FILLED);
        if (!filled) return;

        if (entity instanceof EnderMan) {
            entity.hurt(level.damageSources().drown(), 4.0f);
        }
        if (entity instanceof Zombie z && !z.isRemoved()) {
            if (!level.isClientSide) {
                z.discard();
                Drowned drowned = EntityType.DROWNED.create(level);
                if (drowned != null) {
                    drowned.moveTo(z.getX(), z.getY(), z.getZ(), z.getYRot(), z.getXRot());
                    drowned.finalizeSpawn((ServerLevel) level, level.getCurrentDifficultyAt(pos), MobSpawnType.CONVERSION, null);
                    level.addFreshEntity(drowned);
                }
            }
        }
        if (entity instanceof LivingEntity le) {
            if (!level.isClientSide) {
                for (var e : le.getActiveEffectsMap().keySet()) {
                    if (e.value().getCategory() == MobEffectCategory.HARMFUL) {
                        le.removeEffect(e);
                    }
                }
            }
            Vec3 v = le.getDeltaMovement();
            le.setDeltaMovement(v.x * 0.6, Math.min(v.y, 0.0), v.z * 0.6);
        }
    }


    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            GeneralUtil.onStateReplaced(level, pos);
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    @SuppressWarnings("deprecation")
    public long getSeed(BlockState state, BlockPos pos) {
        return Mth.getSeed(pos.getX(), pos.getY(), pos.getZ());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return state.getValue(PART) == BedPart.HEAD ? new BathtubBlockEntity(pos, state) : null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (state.getValue(PART) != BedPart.HEAD) return null;
        if (type == EntityTypeRegistry.BATHTUB_BLOCK_ENTITY.get()) {
            return level.isClientSide
                    ? (l,p,s,be) -> BathtubBlockEntity.clientTick(l,p,s,(BathtubBlockEntity)be)
                    : (l,p,s,be) -> BathtubBlockEntity.serverTick(l,p,s,(BathtubBlockEntity)be);
        }
        return null;
    }
}
