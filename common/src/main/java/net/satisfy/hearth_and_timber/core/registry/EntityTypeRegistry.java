package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.entity.CabinetBlockEntity;
import net.satisfy.hearth_and_timber.core.block.entity.SmokeOvenEntity;
import net.satisfy.hearth_and_timber.core.block.entity.WardrobeBlockEntity;
import net.satisfy.hearth_and_timber.core.entity.ChairEntity;

import java.util.function.Supplier;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.*;

public class EntityTypeRegistry {
    private static final Registrar<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.BLOCK_ENTITY_TYPE).getRegistrar();
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<BlockEntityType<CabinetBlockEntity>> CABINET_BLOCK_ENTITY = registerBlockEntity("cabinet", () -> BlockEntityType.Builder.of(CabinetBlockEntity::new, RUSTIC_CABINET.get(), RUSTIC_WALL_CABINET.get(), RUSTIC_DRESSER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<SmokeOvenEntity>> SMOKER_BLOCK_ENTITY = registerBlockEntity("smoker", () -> BlockEntityType.Builder.of(SmokeOvenEntity::new, RUSTIC_SMOKER.get()).build(null));
    public static final RegistrySupplier<BlockEntityType<WardrobeBlockEntity>> WARDROBE_BLOCK_ENTITY = registerBlockEntity("wardrobe", () -> BlockEntityType.Builder.of(WardrobeBlockEntity::new, RUSTIC_WARDROBE.get()).build(null));

    public static final RegistrySupplier<EntityType<ChairEntity>> CHAIR_ENTITY = registerEntityType("chair", () -> EntityType.Builder.of(ChairEntity::new, MobCategory.MISC).sized(0.001F, 0.001F).build((HearthAndTimber.identifier("chair")).toString()));

    private static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(final String path, final Supplier<T> type) {
        return BLOCK_ENTITY_TYPES.register(HearthAndTimber.identifier(path), type);
    }

    private static <T extends EntityType<?>> RegistrySupplier<T> registerEntityType(final String path, final Supplier<T> type) {
        return ENTITY_TYPES.register(HearthAndTimber.identifier(path), type);
    }


    public static void init() {
        ENTITY_TYPES.register();
    }
}
