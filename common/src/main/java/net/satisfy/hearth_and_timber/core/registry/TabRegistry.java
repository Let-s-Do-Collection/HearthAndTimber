package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.CREATIVE_MODE_TAB);

    @SuppressWarnings("unused")
    public static final RegistrySupplier<CreativeModeTab> HEARTH_AND_TIMBER_TAB = CREATIVE_MODE_TABS.register("hearth_and_timber", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.SHINGLE_ROOF_BLOCK.get()))
            .title(Component.translatable("creativetab.hearth_and_timber.tab"))
            .displayItems((parameters, output) -> {
                output.accept(ObjectRegistry.FRAMEWORK.get());
                output.accept(ObjectRegistry.CATTLEGRID.get());
                output.accept(ObjectRegistry.SHINGLE_ROOF_BLOCK.get());
                output.accept(ObjectRegistry.SHINGLE_ROOF_STAIRS.get());
                output.accept(ObjectRegistry.SHINGLE_ROOF_SLAB.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_BLOCK.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_STAIRS.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_SLAB.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_BLOCK.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_STAIRS.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_SLAB.get());
                output.accept(ObjectRegistry.FIELDSTONE_BLOCK.get());
                output.accept(ObjectRegistry.FIELDSTONE_BLOCK_STAIRS.get());
                output.accept(ObjectRegistry.FIELDSTONE_BLOCK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BLOCK.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BLOCK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BLOCK_SLAB.get());
                output.accept(ObjectRegistry.SPLITSTONE_BLOCK.get());
                output.accept(ObjectRegistry.SPLITSTONE_BLOCK_STAIRS.get());
                output.accept(ObjectRegistry.SPLITSTONE_BLOCK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_BLOCK.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_BLOCK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_BLOCK_SLAB.get());
                output.accept(ObjectRegistry.SPLITSTONE_PATH_BLOCK.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_PATH_BLOCK.get());
                output.accept(ObjectRegistry.PACKED_DIRT.get());
                output.accept(ObjectRegistry.TRAMPLED_PACKED_DIRT.get());
                output.accept(ObjectRegistry.STABLE_FLOOR.get());
                output.accept(ObjectRegistry.STRAW_STABLE_FLOOR.get());
                output.accept(ObjectRegistry.TRAMPLED_STABLE_FLOOR.get());

                output.accept(ObjectRegistry.RUSTIC_BED.get());
                output.accept(ObjectRegistry.RUSTIC_SOFA.get());
                output.accept(ObjectRegistry.RUSTIC_WARDROBE.get());
                output.accept(ObjectRegistry.RUSTIC_SMOKER.get());
                output.accept(ObjectRegistry.RUSTIC_DRESSER.get());
                output.accept(ObjectRegistry.RUSTIC_CABINET.get());
                output.accept(ObjectRegistry.RUSTIC_WALL_CABINET.get());

            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
