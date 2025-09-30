package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.block.entity.PrivyBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class PrivyBlock extends BaseEntityBlock {
    public static final MapCodec<PrivyBlock> CODEC = simpleCodec(PrivyBlock::new);
    public static final DirectionProperty FACING = DirectionProperty.create("facing", Direction.Plane.HORIZONTAL);
    public static final BooleanProperty SMELLY = BooleanProperty.create("smelly");

    public PrivyBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(SMELLY, false));
    }

    @Override
    public @NotNull MapCodec<PrivyBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        return SHAPE.get(state.getValue(FACING));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, SMELLY);
    }

    @Override
    public @NotNull BlockState rotate(BlockState blockState, Rotation rotation) {
        return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState blockState, Mirror mirror) {
        return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide && entity instanceof ItemEntity itemEntity) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PrivyBlockEntity privy && privy.absorb(itemEntity.getItem())) {
                itemEntity.discard();
            }
        }
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        if (player.isSecondaryUseActive()) {
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof PrivyBlockEntity privy) {
                    privy.releaseAll((ServerLevel) level, pos);
                }
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        ItemInteractionResult result = GeneralUtil.onUse(level, player, InteractionHand.MAIN_HAND, hit, 0.2D);
        return result.consumesAction() ? InteractionResult.sidedSuccess(level.isClientSide) : InteractionResult.PASS;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newState, boolean moved) {
        if (!blockState.is(newState.getBlock())) {
            if (!level.isClientSide) {
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if (blockEntity instanceof PrivyBlockEntity privy) {
                    privy.dropAll((ServerLevel) level, pos);
                }
            }
            super.onRemove(blockState, level, pos, newState, moved);
        }
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos pos, RandomSource random) {
        if (!level.isClientSide) return;
        if (!blockState.getValue(SMELLY)) return;
        Direction facing = blockState.getValue(FACING);
        double centerX = pos.getX() + 0.5D;
        double centerZ = pos.getZ() + 0.5D;
        double centerY = pos.getY() + 1.02D;
        switch (facing) {
            case NORTH -> centerZ = pos.getZ() + 0.62D;
            case SOUTH -> centerZ = pos.getZ() + 0.38D;
            case WEST -> centerX = pos.getX() + 0.62D;
            case EAST -> centerX = pos.getX() + 0.38D;
        }
        DustParticleOptions fly = new DustParticleOptions(new Vector3f(0.05F, 0.06F, 0.05F), 0.075F);
        long time = level.getGameTime();
        double base = time * 0.07D;
        double[] rings = new double[]{0.14D, 0.22D, 0.30D};
        for (double radius : rings) {
            int count = 6;
            for (int i = 0; i < count; i++) {
                double angle = base + i * (Math.PI * 2.0D / count);
                double x = centerX + Math.cos(angle) * radius + (random.nextDouble() - 0.5D) * 0.02D;
                double z = centerZ + Math.sin(angle) * radius + (random.nextDouble() - 0.5D) * 0.02D;
                double y = centerY + Math.sin((angle + radius) * 3.0D) * 0.06D + (random.nextDouble() - 0.5D) * 0.02D;
                double speedX = -Math.sin(angle) * 0.012D + (random.nextDouble() - 0.5D) * 0.002D;
                double speedZ = Math.cos(angle) * 0.012D + (random.nextDouble() - 0.5D) * 0.002D;
                double speedY = 0.010D + random.nextDouble() * 0.010D;
                level.addParticle(fly, x, y, z, speedX, speedY, speedZ);
            }
        }
        int wanderers = 10 + random.nextInt(6);
        for (int j = 0; j < wanderers; j++) {
            double radius = 0.34D * Math.sqrt(random.nextDouble());
            double angle = random.nextDouble() * Math.PI * 2.0D + base * 0.5D;
            double x = centerX + Math.cos(angle) * radius + (random.nextDouble() - 0.5D) * 0.02D;
            double z = centerZ + Math.sin(angle) * radius + (random.nextDouble() - 0.5D) * 0.02D;
            double y = (pos.getY() + 0.72D) + random.nextDouble() * 0.44D;
            double speedX = (random.nextDouble() - 0.5D) * 0.012D;
            double speedZ = (random.nextDouble() - 0.5D) * 0.012D;
            double speedY = 0.008D + random.nextDouble() * 0.012D;
            level.addParticle(fly, x, y, z, speedX, speedY, speedZ);
        }
        int interval = 6;
        if (((time + pos.asLong()) % interval) == 0) {
            SoundEvent[] beeBuzzSounds = new SoundEvent[]{SoundEvents.BEE_LOOP, SoundEvents.BEE_LOOP_AGGRESSIVE, SoundEvents.BEE_POLLINATE};
            int layers = 2 + random.nextInt(2);
            for (int i = 0; i < layers; i++) {
                SoundEvent buzzSound = beeBuzzSounds[(i + (int) (time % beeBuzzSounds.length)) % beeBuzzSounds.length];
                float volume = 0.05F + random.nextFloat() * 0.05F;
                float pitch = 1.8F + random.nextFloat() * 0.6F;
                double offsetX = (random.nextDouble() - 0.5D) * 0.2D;
                double offsetZ = (random.nextDouble() - 0.5D) * 0.2D;
                level.playLocalSound(centerX + offsetX, centerY, centerZ + offsetZ, buzzSound, SoundSource.AMBIENT, volume, pitch, false);
            }
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new PrivyBlockEntity(pos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(type, EntityTypeRegistry.PRIVY_BLOCK_ENTITY.get(), (level1, pos, blockState1, blockEntity) -> PrivyBlockEntity.serverTick(level1, pos, blockEntity));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PrivyBlockEntity privy) {
            return privy.redstoneLevel();
        }
        return 0;
    }

    @Override
    public @NotNull BlockState playerWillDestroy(Level level, BlockPos pos, BlockState blockState, Player player) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof PrivyBlockEntity privy) {
                privy.dropAll((ServerLevel) level, pos);
            }
        }
        super.playerWillDestroy(level, pos, blockState, player);
        return blockState;
    }

    private static final Supplier<VoxelShape> voxelShapeSupplier = () -> {
        VoxelShape shape = Shapes.empty();
        shape = Shapes.join(shape, Shapes.box(0.1875, 0, 0.125, 0.8125, 0.4375, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.625, 0.375, 0.0625, 0.875, 0.5, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.375, 0.5625, 0.875, 0.5, 0.75), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0.375, 0.0625, 0.375, 0.5, 0.5625), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.125, 0, 0.75, 0.875, 0.375, 1), BooleanOp.OR);
        shape = Shapes.join(shape, Shapes.box(0.0625, 0.375, 0.75, 0.9375, 1, 1), BooleanOp.OR);
        return shape;
    };

    public static final Map<Direction, VoxelShape> SHAPE = Util.make(new HashMap<>(), map -> {
        for (Direction direction : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(direction, GeneralUtil.rotateShape(Direction.NORTH, direction, voxelShapeSupplier.get()));
        }
    });
}
