package net.satisfy.hearth_and_timber.client.renderer.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class DummyWardrobeEntity extends LivingEntity {
    private final ItemStack[] equipment = new ItemStack[4];

    public DummyWardrobeEntity(Level level) {
        super(null, level);
        for (int i = 0; i < equipment.length; i++) {
            equipment[i] = ItemStack.EMPTY;
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return java.util.List.of(equipment);
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return switch (slot) {
            case HEAD -> equipment[0];
            case CHEST -> equipment[1];
            case LEGS -> equipment[2];
            case FEET -> equipment[3];
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {
        switch (slot) {
            case HEAD -> equipment[0] = stack;
            case CHEST -> equipment[1] = stack;
            case LEGS -> equipment[2] = stack;
            case FEET -> equipment[3] = stack;
        }
    }

    @Override
    public BlockPos blockPosition() {
        return BlockPos.ZERO;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}
