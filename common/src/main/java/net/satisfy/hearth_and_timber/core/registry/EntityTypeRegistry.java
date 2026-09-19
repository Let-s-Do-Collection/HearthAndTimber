package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.entity.SlidingDoorBlockEntity;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import net.satisfy.hearth_and_timber.core.block.entity.WindowCasingBlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.ACACIA_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.BIRCH_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.CHERRY_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.COMPAT_WINDOW_CASINGS;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.DARK_OAK_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.JUNGLE_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.MANGROVE_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.OAK_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.PALE_OAK_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.SLIDING_BARN_DOOR;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.SLIDING_HAYLOFT_DOOR;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.SLIDING_STABLE_DOOR;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.SPRUCE_WINDOW_CASING;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_BASE_SKIRT;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_BASE_TRIM;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_CROSS_FRAME;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_DIAGONAL_FRAME;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_FOUNDATION;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_FRAME;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_FRAME_STAIRS;
import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.TIMBER_GRID_FRAME;

public final class EntityTypeRegistry {
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.BLOCK_ENTITY_TYPE);
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPE_REGISTRAR = BLOCK_ENTITY_TYPES.getRegistrar();

    public static final RegistrySupplier<BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY = registerBlockEntity("sliding_door", () -> BlockEntityType.Builder.of(SlidingDoorBlockEntity::new, SLIDING_HAYLOFT_DOOR.get(), SLIDING_BARN_DOOR.get(), SLIDING_STABLE_DOOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<TimberFrameBlockEntity>> TIMBER_FRAME_BLOCK_ENTITY = registerBlockEntity("timber_frame", () -> BlockEntityType.Builder.of(TimberFrameBlockEntity::new, TIMBER_FOUNDATION.get(), TIMBER_BASE_TRIM.get(), TIMBER_BASE_SKIRT.get(), TIMBER_FRAME.get(), TIMBER_GRID_FRAME.get(), TIMBER_CROSS_FRAME.get(), TIMBER_DIAGONAL_FRAME.get(), TIMBER_FRAME_STAIRS.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WindowCasingBlockEntity>> WINDOW_CASING_BLOCK_ENTITY = registerBlockEntity("window_casing", () -> {
        List<Block> windowCasings = new ArrayList<>(List.of(
                OAK_WINDOW_CASING.get(), SPRUCE_WINDOW_CASING.get(), BIRCH_WINDOW_CASING.get(), JUNGLE_WINDOW_CASING.get(),
                ACACIA_WINDOW_CASING.get(), DARK_OAK_WINDOW_CASING.get(), MANGROVE_WINDOW_CASING.get(), CHERRY_WINDOW_CASING.get(),
                PALE_OAK_WINDOW_CASING.get()
        ));
        COMPAT_WINDOW_CASINGS.values().forEach(supplier -> windowCasings.add(supplier.get()));
        return BlockEntityType.Builder.of(WindowCasingBlockEntity::new, windowCasings.toArray(new Block[0])).build(null);
    });

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String path, Supplier<T> type) {
        return BLOCK_ENTITY_TYPE_REGISTRAR.register(HearthAndTimber.identifier(path), type);
    }

    static {
        BLOCK_ENTITY_TYPES.register();
    }
}