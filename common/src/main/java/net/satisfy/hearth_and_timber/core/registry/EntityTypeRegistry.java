package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.entity.*;

import java.util.function.Supplier;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<SlidingDoorBlockEntity>> SLIDING_DOOR_BLOCK_ENTITY = registerBlockEntity("sliding_door", () -> BlockEntityType.Builder.of(SlidingDoorBlockEntity::new, SLIDING_HAYLOFT_DOOR.get(), SLIDING_BARN_DOOR.get(), SLIDING_STABLE_DOOR.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<TimberFrameBlockEntity>> TIMBER_FRAME_BLOCK_ENTITY = registerBlockEntity("timber_frame", () -> BlockEntityType.Builder.of(TimberFrameBlockEntity::new, TIMBER_FOUNDATION.get(), TIMBER_BASE_TRIM.get(), TIMBER_BASE_SKIRT.get(), TIMBER_FRAME.get(), TIMBER_GRID_FRAME.get(), TIMBER_CROSS_FRAME.get(), TIMBER_DIAGONAL_FRAME.get(), TIMBER_FRAME_STAIRS.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WindowCasingBlockEntity>> WINDOW_CASING_BLOCK_ENTITY = registerBlockEntity("window_casing", () -> BlockEntityType.Builder.of(WindowCasingBlockEntity::new, ACACIA_WINDOW_CASING.get(), CHERRY_WINDOW_CASING.get(), DARK_OAK_WINDOW_CASING.get(), JUNGLE_WINDOW_CASING.get(), MANGROVE_WINDOW_CASING.get(), OAK_WINDOW_CASING.get(), SPRUCE_WINDOW_CASING.get(), BIRCH_WINDOW_CASING.get(), PALE_OAK_WINDOW_CASING.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<RubbleMasonryBlockEntity>> RUBBLE_MASONRY_BLOCK_ENTITY = registerBlockEntity("rubble_masonry", () -> BlockEntityType.Builder.of(RubbleMasonryBlockEntity::new, COVERED_RUBBLESTONE.get(), PLASTERED_RUBBLESTONE.get(), RUBBLESTONE.get(), GROUTLESS_RUBBLESTONE.get()).build(null));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(HearthAndTimber.identifier(path), type);
    }

    public static void init() {
        ENTITY_TYPES.register();
    }
}
