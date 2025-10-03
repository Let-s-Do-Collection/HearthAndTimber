package net.satisfy.hearth_and_timber.core.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TimberFrameBlock extends BaseEntityBlock {
    public static final MapCodec<TimberFrameBlock> CODEC = simpleCodec(TimberFrameBlock::new);
    private static final TagKey<Block> FILLABLES = TagKey.create(Registries.BLOCK, HearthAndTimber.identifier("timber_frame_fillables"));

    public TimberFrameBlock(Properties properties) {
        super(properties);
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
            }
            return level.isClientSide ? ItemInteractionResult.CONSUME : ItemInteractionResult.SUCCESS;
        }
        if (stack.getItem() instanceof BlockItem blockItem) {
            if (level.getBlockEntity(pos) instanceof TimberFrameBlockEntity be) {
                if (be.getHeldBlock() == null || be.getHeldBlock().isAir()) {
                    BlockState toHold = blockItem.getBlock().defaultBlockState();
                    if (canAccept(level, pos, toHold)) {
                        if (level.isClientSide) return ItemInteractionResult.CONSUME;
                        be.setHeldBlock(toHold);
                        SoundType sound = toHold.getSoundType();
                        level.playSound(null, pos, sound.getPlaceSound(), SoundSource.BLOCKS, (sound.getVolume() + 1F) / 2F, sound.getPitch() * 0.8F);
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        return ItemInteractionResult.SUCCESS;
                    }
                }
                return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private static boolean canAccept(BlockGetter level, BlockPos pos, BlockState state) {
        if (state == null || state.isAir()) return false;
        if (state.getBlock() instanceof EntityBlock) return false;
        if (state.getBlock() instanceof TimberFrameBlock) return false;
        if (state.getRenderShape() != RenderShape.MODEL) return false;
        return Block.isShapeFullBlock(state.getShape(level, pos));
    }
}
