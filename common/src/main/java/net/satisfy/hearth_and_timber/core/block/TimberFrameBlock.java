package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class TimberFrameBlock extends BaseEntityBlock {
    public static final MapCodec<TimberFrameBlock> CODEC = simpleCodec(TimberFrameBlock::new);
    public static final BooleanProperty APPLIED = BooleanProperty.create("applied");

    public TimberFrameBlock(Properties properties) {
        super(properties.isViewBlocking((a, b, c) -> false).dynamicShape());
        this.registerDefaultState(this.stateDefinition.any().setValue(APPLIED, false));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TimberFrameBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(APPLIED);
    }

    @Override
    protected boolean isOcclusionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return super.isOcclusionShapeFullBlock(blockState, blockGetter, blockPos);
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        List<ItemStack> drops = super.getDrops(state, builder);
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof TimberFrameBlockEntity be) {
            BlockState held = be.getHeldBlock();
            if (held != null && !held.isAir()) {
                drops.add(new ItemStack(held.getBlock().asItem()));
            }
        }
        return drops;
    }


    @Override
    protected @NotNull ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (stack.getItem() instanceof ShearsItem || player.isShiftKeyDown()) {
            if (!level.isClientSide && level.getBlockEntity(pos) instanceof TimberFrameBlockEntity be) {
                popResource(level, pos, new ItemStack(ObjectRegistry.TIMBER_FRAME.get()));
                BlockState held = be.getHeldBlock();
                if (held != null && !held.isAir()) {
                    level.setBlock(pos, held, 11);
                } else {
                    level.removeBlock(pos, false);
                }
                level.playSound(player, pos, SoundEvents.LEASH_KNOT_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
                if (stack.getItem() instanceof ShearsItem) {
                    stack.hurtAndBreak(1, player, EquipmentSlot.MAINHAND);
                }
                level.setBlock(pos, state.setValue(APPLIED, false), 18);
            }
            return ItemInteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (level.getBlockEntity(pos) instanceof TimberFrameBlockEntity be) {
                if (be.getHeldBlock() == null || be.getHeldBlock().isAir()) {
                    BlockState toHold = blockItem.getBlock().defaultBlockState();
                    if (canAccept(level, pos, toHold)) {
                        if (!level.isClientSide) {
                            level.setBlockAndUpdate(pos, state.setValue(APPLIED, true));
                            be.setHeldBlock(toHold);
                            SoundType sound = toHold.getSoundType();
                            level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F);
                            if (!player.getAbilities().instabuild) stack.shrink(1);
                        }
                        return ItemInteractionResult.SUCCESS;
                    }
                }
            }
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected boolean isCollisionShapeFullBlock(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        Optional<TimberFrameBlockEntity> held = blockGetter.getBlockEntity(blockPos, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get());
        return !(held.isEmpty() || Objects.isNull(held.get().getHeldBlock())
                || held.get().getHeldBlock().getBlock() instanceof TransparentBlock
        );
    }

    private static boolean canAccept(BlockGetter level, BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) return false;
        if (state.getBlock() instanceof EntityBlock) return false;
        if (state.getBlock() instanceof TimberFrameBlock) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getShape(level, pos));
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
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame_trim.info_0").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
        list.add(Component.empty());
        list.add(Component.translatable("tooltip.hearth_and_timber.timber_frame.info_1").withStyle(Style.EMPTY.withColor(TextColor.fromRgb(beige))));
    }

    List<BlockPos> getPositions(BlockPos center) {
        ArrayList<BlockPos> outputs = new ArrayList<>();
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                for (int z = -1; z <= 1; z++) {
                    outputs.add(center.offset(new BlockPos(x, y, z)));
                }
            }
        }
        return outputs;
    }

    @Override
    protected void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if (!level.isClientSide) {
            getPositions(blockPos).stream()
                    .map(o -> level.getBlockEntity(o, EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get()))
                    .filter(Optional::isPresent).map(Optional::get)
                    .forEach(e -> {
                        BlockState b = e.getHeldBlock();
                        e.setHeldBlock(Blocks.AIR.defaultBlockState());
                        e.setHeldBlock(b);
                    });
        }
        super.neighborChanged(blockState, level, blockPos, block, blockPos2, bl);
    }
}
