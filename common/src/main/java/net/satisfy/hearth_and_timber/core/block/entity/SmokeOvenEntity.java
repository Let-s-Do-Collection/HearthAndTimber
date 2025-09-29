package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.SmokerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.NotNull;

public class SmokeOvenEntity extends AbstractFurnaceBlockEntity {
    public SmokeOvenEntity(BlockPos pos, BlockState state) {
        super(EntityTypeRegistry.SMOKER_BLOCK_ENTITY.get(), pos, state, RecipeType.SMOKING);
    }

    @Override
    protected @NotNull Component getDefaultName() {
        return Component.translatable("container.smoker");
    }

    @Override
    protected @NotNull AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new SmokerMenu(id, inv, this, this.dataAccess);
    }
}
