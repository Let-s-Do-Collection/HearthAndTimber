package net.satisfy.hearth_and_timber.core.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.PrivyBlock;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class PrivyBlockEntity extends BlockEntity {
    private final SimpleContainer inventory = new SimpleContainer(9);
    private final Deque<Pending> queue = new ArrayDeque<>();
    private final Map<UUID, Integer> sitTicks = new HashMap<>();
    private final Random random = new Random();
    private boolean lastSmelly = false;
    private int smellyCooldown = 0;

    public PrivyBlockEntity(BlockPos pos, BlockState blockState) {
        super(EntityTypeRegistry.PRIVY_BLOCK_ENTITY.get(), pos, blockState);
        this.lastSmelly = blockState.hasProperty(PrivyBlock.SMELLY) && blockState.getValue(PrivyBlock.SMELLY);
    }

    public static void serverTick(Level level, BlockPos pos, PrivyBlockEntity blockEntity) {
        blockEntity.tickSit((ServerLevel) level, pos);
        blockEntity.tickProcess((ServerLevel) level, pos);
        blockEntity.tickSmellyState((ServerLevel) level);
    }

    private void tickSit(ServerLevel level, BlockPos pos) {
        Player seated = null;
        var chair = GeneralUtil.getChairEntity(level, pos);
        if (chair != null && !chair.getPassengers().isEmpty() && chair.getPassengers().get(0) instanceof Player player) {
            seated = player;
        }
        if (seated == null) {
            sitTicks.clear();
            return;
        }
        UUID id = seated.getUUID();
        int accumulated = sitTicks.getOrDefault(id, 0);
        if (accumulated < 3000) {
            if (level.getGameTime() % 100 == 0) {
                FoodData food = seated.getFoodData();
                food.setSaturation(Mth.clamp(food.getSaturationLevel() - 0.5F, 0F, 20F));
                seated.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0, false, false, true));
                sitTicks.put(id, accumulated + 100);
            }
        }
    }

    private void tickProcess(ServerLevel level, BlockPos pos) {
        if (!queue.isEmpty()) {
            Pending pending = queue.peek();
            if (pending.readyTime <= level.getGameTime()) {
                ItemStack outputStack = random.nextFloat() < 0.4F ? new ItemStack(Items.ROTTEN_FLESH) : new ItemStack(Items.BONE_MEAL);
                insertOrDrop(level, pos, outputStack);
                queue.poll();
                setChanged();
                sync();
            }
        }
    }

    private void tickSmellyState(ServerLevel level) {
        if (smellyCooldown > 0) {
            smellyCooldown--;
            return;
        }
        boolean current = hasSmelly();
        BlockState blockState = getBlockState();
        if (blockState.hasProperty(PrivyBlock.SMELLY) && current != lastSmelly) {
            level.setBlock(worldPosition, blockState.setValue(PrivyBlock.SMELLY, current), Block.UPDATE_CLIENTS | Block.UPDATE_INVISIBLE);
            lastSmelly = current;
            smellyCooldown = 5;
        }
    }

    public boolean absorb(ItemStack itemStack) {
        if (itemStack.isEmpty()) return false;
        int count = itemStack.getCount();
        for (int i = 0; i < count; i++) {
            assert level != null;
            queue.add(new Pending(level.getGameTime() + 600));
        }
        setChanged();
        sync();
        return true;
    }

    private void insertOrDrop(ServerLevel level, BlockPos pos, ItemStack itemStack) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack currentStack = inventory.getItem(i);
            if (currentStack.isEmpty()) {
                inventory.setItem(i, itemStack);
                setChanged();
                sync();
                return;
            }
            if (ItemStack.isSameItemSameComponents(currentStack, itemStack) && currentStack.getCount() < currentStack.getMaxStackSize()) {
                int can = Math.min(itemStack.getCount(), currentStack.getMaxStackSize() - currentStack.getCount());
                currentStack.grow(can);
                itemStack.shrink(can);
                if (itemStack.isEmpty()) {
                    setChanged();
                    sync();
                    return;
                }
            }
        }
        GeneralUtil.popResourceFromFace(level, pos, Direction.UP, itemStack);
    }

    public void releaseAll(ServerLevel level, BlockPos pos) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                GeneralUtil.popResourceFromFace(level, pos, Direction.UP, stack.copy());
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
        setChanged();
        sync();
    }

    public void dropAll(ServerLevel level, BlockPos pos) {
        releaseAll(level, pos);
        queue.clear();
        setChanged();
        sync();
    }

    public int redstoneLevel() {
        int filled = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            if (!inventory.getItem(i).isEmpty()) filled++;
        }
        return Mth.clamp((int) Math.ceil((filled / 9.0) * 15.0), 0, 15);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.saveAdditional(tag, provider);
        ListTag items = new ListTag();
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (!stack.isEmpty()) {
                CompoundTag t = new CompoundTag();
                t.putByte("Slot", (byte) i);
                stack.save(provider, t);
                items.add(t);
            }
        }
        tag.put("Items", items);
        ListTag q = new ListTag();
        for (Pending pending : queue) {
            CompoundTag t = new CompoundTag();
            t.putLong("Ready", pending.readyTime);
            q.add(t);
        }
        tag.put("Queue", q);
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider provider) {
        super.loadAdditional(tag, provider);
        inventory.clearContent();
        ListTag items = tag.getList("Items", Tag.TAG_COMPOUND);
        for (int i = 0; i < items.size(); i++) {
            CompoundTag t = items.getCompound(i);
            if (t.contains("id", Tag.TAG_STRING)) {
                int slot = t.getByte("Slot") & 255;
                inventory.setItem(slot, ItemStack.parse(provider, t).orElse(ItemStack.EMPTY));
            }
        }
        queue.clear();
        ListTag q = tag.getList("Queue", Tag.TAG_COMPOUND);
        for (int i = 0; i < q.size(); i++) {
            CompoundTag t = q.getCompound(i);
            queue.add(new Pending(t.getLong("Ready")));
        }
        lastSmelly = getBlockState().hasProperty(PrivyBlock.SMELLY) && getBlockState().getValue(PrivyBlock.SMELLY);
        smellyCooldown = 0;
    }

    public boolean hasSmelly() {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack s = inventory.getItem(i);
            if (!s.isEmpty() && (s.is(Items.BONE_MEAL) || s.is(Items.ROTTEN_FLESH))) return true;
        }
        return false;
    }

    private void sync() {
        if (level != null) {
            setChanged();
            level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider provider) {
        return saveCustomOnly(provider);
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private record Pending(long readyTime) {}
}
