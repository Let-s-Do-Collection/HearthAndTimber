package net.satisfy.hearth_and_timber.neoforge.core.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.common.extensions.IBlockEntityExtension;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.neoforge.client.renderer.block.ConnectingTimberModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;

@Mixin(TimberFrameBlockEntity.class)
public abstract class TimberBlockEntityMixin implements IBlockEntityExtension {

    @Override
    public ModelData getModelData() {
        ModelData.Builder builder = ModelData.builder();
        TimberFrameBlockEntity self = ((TimberFrameBlockEntity)(Object)this);
        builder.with(ConnectingTimberModel.CONNECTIONS, TimberHelper.getIntermediate(
                self.getLevel(), self.getBlockPos(), self.getBlockState()
        ));
        return builder.build();
    }

    @Inject(method = "setHeldBlock", at = @At("TAIL"))
    private void hearthAndTimber$refreshModelData(BlockState state, CallbackInfo ci) {
        TimberFrameBlockEntity be = (TimberFrameBlockEntity)(Object)this;
        Level level = be.getLevel();
        if (level == null || !level.isClientSide) return;
        be.requestModelDataUpdate();
        BlockPos pos = be.getBlockPos();
        BlockState bs = be.getBlockState();
        level.sendBlockUpdated(pos, bs, bs, Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
    }

    @Inject(method = "loadAdditional", at = @At("TAIL"))
    private void hearthAndTimber$refreshAfterLoad(CompoundTag tag, HolderLookup.Provider provider, CallbackInfo ci) {
        TimberFrameBlockEntity be = (TimberFrameBlockEntity)(Object)this;
        Level level = be.getLevel();
        if (level == null || !level.isClientSide) return;
        be.requestModelDataUpdate();
        BlockPos pos = be.getBlockPos();
        BlockState bs = be.getBlockState();
        level.sendBlockUpdated(pos, bs, bs, Block.UPDATE_CLIENTS | Block.UPDATE_IMMEDIATE);
    }
}
