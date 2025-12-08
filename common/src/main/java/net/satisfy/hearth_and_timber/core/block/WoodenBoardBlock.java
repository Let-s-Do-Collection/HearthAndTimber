package net.satisfy.hearth_and_timber.core.block;

import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@MethodsReturnNonnullByDefault
public class WoodenBoardBlock extends Block {
    public static final IntegerProperty LAYERS = IntegerProperty.create("layers", 0, 3);
    public static final BooleanProperty TOP = BooleanProperty.create("top");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public WoodenBoardBlock() {
        super(BlockBehaviour.Properties.of().strength(2.0F, 3.0F).sound(SoundType.WOOD));
        registerDefaultState(defaultBlockState()
                .setValue(LAYERS, 0)
                .setValue(TOP, false)
                .setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int height = (state.getValue(LAYERS) + 1) * 4;
        if (height >= 16) {
            return Shapes.block();
        }
        if (state.getValue(TOP)) {
            return Block.box(0, 16 - height, 0, 16, 16, 16);
        }
        return Block.box(0, 0, 0, 16, height, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return getShape(state, level, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(LAYERS, TOP, FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos blockPos = context.getClickedPos();
        Direction face = context.getClickedFace();
        double localY = context.getClickLocation().y - blockPos.getY();
        boolean isTop = face == Direction.DOWN || (face != Direction.UP && localY > 0.5D);
        Direction facing = context.getHorizontalDirection();
        return defaultBlockState()
                .setValue(LAYERS, 0)
                .setValue(TOP, isTop)
                .setValue(FACING, facing);
    }

    @Override
    public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        Direction face = hit.getDirection();
        boolean isTop = state.getValue(TOP);
        double localY = hit.getLocation().y - pos.getY();
        boolean validHit;
        if (!isTop) {
            validHit = face == Direction.UP || (face.getAxis().isHorizontal() && localY > 0.5D);
        } else {
            validHit = face == Direction.DOWN || (face.getAxis().isHorizontal() && localY < 0.5D);
        }
        if (!validHit) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (stack.getItem() != asItem()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        if (!level.isClientSide) {
            int current = state.getValue(LAYERS);
            if (current < 3) {
                level.setBlock(pos, state.setValue(LAYERS, current + 1), Block.UPDATE_ALL);
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                level.playSound(null, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return ItemInteractionResult.CONSUME;
            } else {
                BlockPos targetPos = isTop ? pos.below() : pos.above();
                BlockState targetState = level.getBlockState(targetPos);
                if (targetState.canBeReplaced() || targetState.isAir()) {
                    level.setBlock(targetPos, defaultBlockState()
                            .setValue(LAYERS, 0)
                            .setValue(TOP, isTop)
                            .setValue(FACING, state.getValue(FACING)), Block.UPDATE_ALL);
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                    level.playSound(null, targetPos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return ItemInteractionResult.CONSUME;
                }
            }
        }
        return ItemInteractionResult.CONSUME;
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hit) {
        Direction face = hit.getDirection();
        boolean isTop = state.getValue(TOP);
        double localY = hit.getLocation().y - pos.getY();
        boolean validHit;
        if (!isTop) {
            validHit = face == Direction.UP || (face.getAxis().isHorizontal() && localY > 0.5D);
        } else {
            validHit = face == Direction.DOWN || (face.getAxis().isHorizontal() && localY < 0.5D);
        }
        if (!validHit) {
            return InteractionResult.PASS;
        }
        if (!level.isClientSide) {
            ItemStack heldItem = player.getMainHandItem();
            if (heldItem.getItem() instanceof AxeItem) {
                int current = state.getValue(LAYERS);
                if (current > 0) {
                    popResource(level, pos, new ItemStack(asItem()));
                    level.setBlock(pos, state.setValue(LAYERS, current - 1), Block.UPDATE_ALL);
                    level.playSound(null, pos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (!player.getAbilities().instabuild) {
                        heldItem.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                    }
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext tooltipContext, List<Component> tooltipList, TooltipFlag tooltipFlag) {
        int beige = 0xF5DEB3;
        int gold = 0xFFD700;
        if (!Screen.hasShiftDown()) {
            Component key = Component.literal("[SHIFT]").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(gold)));
            tooltipList.add(Component.translatable("tooltip.hearth_and_timber.tooltip_information.hold", key).withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
            return;
        }
        tooltipList.add(Component.translatable("tooltip.hearth_and_timber.board.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        tooltipList.add(Component.empty());
        tooltipList.add(Component.translatable("tooltip.hearth_and_timber.board.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }
}