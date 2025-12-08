package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.Util;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SupportBlock extends Block {
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final BooleanProperty CONNECTED = BooleanProperty.create("connected");
    public static final BooleanProperty EXTENDED = BooleanProperty.create("extended");
    public static final BooleanProperty REST = BooleanProperty.create("rest");
    public static final BooleanProperty REINFORCED = BooleanProperty.create("reinforced");

    private static final Map<Direction, VoxelShape> SHAPE_SINGLE = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.empty();
        base = Shapes.join(base, Shapes.box(5.0 / 16.0, 0.0, 14.0 / 16.0, 11.0 / 16.0, 10.0 / 16.0, 1.0), BooleanOp.OR);
        base = Shapes.join(base, Shapes.box(5.0 / 16.0, 10.0 / 16.0, 0.0, 11.0 / 16.0, 1.0, 1.0), BooleanOp.OR);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(dir, GeneralUtil.rotateShape(Direction.NORTH, dir, base));
        }
    });

    private static final Map<Direction, VoxelShape> SHAPE_CONNECTED = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.box(5.0 / 16.0, 10.0 / 16.0, 0.0, 11.0 / 16.0, 1.0, 1.0);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(dir, GeneralUtil.rotateShape(Direction.NORTH, dir, base));
        }
    });

    private static final Map<Direction, VoxelShape> SHAPE_EXTENDED = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.box(5.0 / 16.0, 10.0 / 16.0, 10.0 / 16.0, 11.0 / 16.0, 1.0, 1.0);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(dir, GeneralUtil.rotateShape(Direction.NORTH, dir, base));
        }
    });

    private static final Map<Direction, VoxelShape> SHAPE_NO_REST = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.box(5.0 / 16.0, 0.0, 14.0 / 16.0, 11.0 / 16.0, 1.0, 1.0);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(dir, GeneralUtil.rotateShape(Direction.NORTH, dir, base));
        }
    });

    private static final Map<Direction, VoxelShape> SHAPE_REINFORCED = Util.make(new HashMap<>(), map -> {
        VoxelShape base = Shapes.empty();
        base = Shapes.join(base, Shapes.box(0.3125, 0.0, 0.875, 0.6875, 1.0, 1.0), BooleanOp.OR);
        base = Shapes.join(base, Shapes.box(0.3125, 0.625, 0.0, 0.6875, 1.0, 0.875), BooleanOp.OR);
        base = Shapes.join(base, Shapes.box(0.0, 0.75, 0.0, 0.3125, 1.0, 1.0), BooleanOp.OR);
        base = Shapes.join(base, Shapes.box(0.6875, 0.75, 0.0, 1.0, 1.0, 1.0), BooleanOp.OR);
        for (Direction dir : Direction.Plane.HORIZONTAL.stream().toList()) {
            map.put(dir, GeneralUtil.rotateShape(Direction.NORTH, dir, base));
        }
    });

    public SupportBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(FACING, Direction.NORTH)
                .setValue(CONNECTED, false)
                .setValue(EXTENDED, false)
                .setValue(REST, true)
                .setValue(REINFORCED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();

        Direction defaultFacing = context.getHorizontalDirection().getOpposite();
        Direction clickedFace = context.getClickedFace();

        if (!clickedFace.getAxis().isHorizontal()) {
            return this.defaultBlockState()
                    .setValue(FACING, defaultFacing)
                    .setValue(CONNECTED, false)
                    .setValue(EXTENDED, false)
                    .setValue(REST, true)
                    .setValue(REINFORCED, false);
        }

        BlockPos attachedPos = pos.relative(clickedFace.getOpposite());
        BlockState attached = level.getBlockState(attachedPos);

        boolean attachesToPillar = attached.getBlock() instanceof PillarBlock;
        boolean attachesToSupport = attached.getBlock() instanceof SupportBlock
                && attached.hasProperty(FACING);

        Direction facing = defaultFacing;

        if (attachesToPillar) {
            facing = clickedFace.getOpposite();
        } else if (attachesToSupport) {
            facing = attached.getValue(FACING);
        }

        boolean connected = false;

        if (attachesToPillar) {
            connected = true;
        } else if (attachesToSupport) {
            connected = attached.getValue(FACING) == facing;
        }

        return this.defaultBlockState()
                .setValue(FACING, facing)
                .setValue(CONNECTED, connected)
                .setValue(EXTENDED, false)
                .setValue(REST, true)
                .setValue(REINFORCED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, CONNECTED, EXTENDED, REST, REINFORCED);
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
        Direction facing = state.getValue(FACING);
        if (state.getValue(EXTENDED)) {
            return SHAPE_EXTENDED.get(facing);
        }
        if (!state.getValue(REST)) {
            return SHAPE_NO_REST.get(facing);
        }
        if (state.getValue(REINFORCED)) {
            return SHAPE_REINFORCED.get(facing);
        }
        return (state.getValue(CONNECTED) ? SHAPE_CONNECTED : SHAPE_SINGLE).get(facing);
    }

    private boolean isSameSupportFacing(BlockState state, Direction facing) {
        if (state.getBlock() != this) {
            return false;
        }
        if (!state.hasProperty(FACING)) {
            return false;
        }
        return state.getValue(FACING) == facing;
    }

    private boolean isMiddleOfConnectedRun(Level level, BlockPos pos, Direction facing) {
        BlockState frontState = level.getBlockState(pos.relative(facing));
        BlockState behindState = level.getBlockState(pos.relative(facing.getOpposite()));
        boolean frontOk = isSameSupportFacing(frontState, facing) && frontState.hasProperty(CONNECTED) && frontState.getValue(CONNECTED);
        boolean behindOk = isSameSupportFacing(behindState, facing) && behindState.hasProperty(CONNECTED) && behindState.getValue(CONNECTED);
        return frontOk && behindOk;
    }

    @Override
    public @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Item item = stack.getItem();

        if (Block.byItem(item) instanceof WoodenBoardBlock) {
            if (state.getValue(EXTENDED)) {
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
            if (!level.isClientSide) {
                boolean wasReinforced = state.getValue(REINFORCED);
                boolean newReinforced = !wasReinforced;
                level.setBlock(pos, state.setValue(REINFORCED, newReinforced), 3);
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }

        if (!(item instanceof AxeItem)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        if (!level.isClientSide) {
            if (level instanceof ServerLevel server) {
                server.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, state), pos.getX() + 0.5, pos.getY() + 1.02, pos.getZ() + 0.5, 12, 0.2, 0.0, 0.2, 0.08);
            }
            level.playSound(null, pos, SoundEvents.AXE_STRIP, SoundSource.BLOCKS, 1.0F, 1.0F);
            if (state.getValue(CONNECTED)) {
                Direction facing = state.getValue(FACING);
                if (isMiddleOfConnectedRun(level, pos, facing)) {
                    return ItemInteractionResult.sidedSuccess(false);
                }
                boolean newExtended = !state.getValue(EXTENDED);
                level.setBlock(pos, state.setValue(EXTENDED, newExtended), 3);
            } else {
                boolean newRest = !state.getValue(REST);
                level.setBlock(pos, state.setValue(REST, newRest), 3);
            }
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
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
        list.add(Component.translatable("tooltip.hearth_and_timber.support.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}