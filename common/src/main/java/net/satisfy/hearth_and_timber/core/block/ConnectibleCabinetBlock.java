package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.hearth_and_timber.core.block.entity.CabinetBlockEntity;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class ConnectibleCabinetBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<GeneralUtil.LineConnectingType> TYPE = GeneralUtil.LINE_CONNECTING_TYPE;
    private final Supplier<SoundEvent> openSound;
    private final Supplier<SoundEvent> closeSound;

    public ConnectibleCabinetBlock(Properties settings, Supplier<SoundEvent> openSound, Supplier<SoundEvent> closeSound, Supplier<Boolean> thin) {
        super(settings);
        this.openSound = openSound;
        this.closeSound = closeSound;
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(OPEN, false).setValue(TYPE, GeneralUtil.LineConnectingType.NONE));
    }


    @Override
    protected @NotNull InteractionResult useWithoutItem(BlockState blockState, Level level, BlockPos blockPos, Player player, BlockHitResult blockHitResult) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        } else {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            if (blockEntity instanceof CabinetBlockEntity blockEntity1) {
                player.openMenu(blockEntity1);
            }

            return InteractionResult.CONSUME;
        }
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
        if (!state.is(newState.getBlock())) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof Container container) {
                Containers.dropContents(world, pos, container);
                world.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, world, pos, newState, moved);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new CabinetBlockEntity(pos, state);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return null;
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public void setPlacedBy(Level world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
        if (itemStack.has(DataComponents.CUSTOM_NAME)) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof CabinetBlockEntity blockEntity1) {
                blockEntity1.setComponents(DataComponentMap.builder().set(DataComponents.CUSTOM_NAME, itemStack.getHoverName()).build());
            }
        }
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState state, Level world, BlockPos pos) {
        return AbstractContainerMenu.getRedstoneSignalFromBlockEntity(world.getBlockEntity(pos));
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, OPEN, TYPE);
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Direction facing = ctx.getHorizontalDirection().getOpposite();
        BlockState s = this.defaultBlockState().setValue(FACING, facing).setValue(OPEN, false);
        Level level = ctx.getLevel();
        BlockPos p = ctx.getClickedPos();
        GeneralUtil.LineConnectingType t;
        switch (facing) {
            case EAST -> t = getType(s, level.getBlockState(p.south()), level.getBlockState(p.north()));
            case SOUTH -> t = getType(s, level.getBlockState(p.west()), level.getBlockState(p.east()));
            case WEST -> t = getType(s, level.getBlockState(p.north()), level.getBlockState(p.south()));
            default -> t = getType(s, level.getBlockState(p.east()), level.getBlockState(p.west()));
        }
        return s.setValue(TYPE, t);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block sourceBlock, BlockPos sourcePos, boolean notify) {
        if (level.isClientSide) return;
        Direction facing = state.getValue(FACING);
        GeneralUtil.LineConnectingType t;
        switch (facing) {
            case EAST -> t = getType(state, level.getBlockState(pos.south()), level.getBlockState(pos.north()));
            case SOUTH -> t = getType(state, level.getBlockState(pos.west()), level.getBlockState(pos.east()));
            case WEST -> t = getType(state, level.getBlockState(pos.north()), level.getBlockState(pos.south()));
            default -> t = getType(state, level.getBlockState(pos.east()), level.getBlockState(pos.west()));
        }
        if (state.getValue(TYPE) != t) {
            level.setBlock(pos, state.setValue(TYPE, t), 3);
        }
    }

    private GeneralUtil.LineConnectingType getType(BlockState self, BlockState left, BlockState right) {
        boolean l = isConnectable(left, self);
        boolean r = isConnectable(right, self);
        if (l && r) return GeneralUtil.LineConnectingType.MIDDLE;
        if (l) return GeneralUtil.LineConnectingType.LEFT;
        if (r) return GeneralUtil.LineConnectingType.RIGHT;
        return GeneralUtil.LineConnectingType.NONE;
    }

    private boolean isConnectable(BlockState a, BlockState b) {
        return a.getBlock() == b.getBlock() && a.getValue(FACING) == b.getValue(FACING);
    }

    public void playSound(Level world, BlockPos pos, boolean isOpen) {
        world.playSound(null, pos, isOpen ? openSound.get() : closeSound.get(), SoundSource.BLOCKS, 1.0f, 1.1f);
    }
}
