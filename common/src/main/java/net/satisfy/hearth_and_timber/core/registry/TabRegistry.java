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
                    output.accept(ObjectRegistry.IRON_DIVIDER.get());
                    output.accept(ObjectRegistry.CHICKEN_FENCE.get());
                    output.accept(ObjectRegistry.FRAMEWORK.get());
                    output.accept(ObjectRegistry.CATTLEGRID.get());
                    output.accept(ObjectRegistry.TIMBER_FRAME_STAIRS.get());
                    output.accept(ObjectRegistry.TIMBER_FRAME.get());
                    output.accept(ObjectRegistry.TIMBER_GRID_FRAME.get());
                    output.accept(ObjectRegistry.TIMBER_DIAGONAL_FRAME.get());
                    output.accept(ObjectRegistry.TIMBER_CROSS_FRAME.get());
                    output.accept(ObjectRegistry.WHITE_PLASTER.get());
                    output.accept(ObjectRegistry.LIGHT_GRAY_PLASTER.get());
                    output.accept(ObjectRegistry.GRAY_PLASTER.get());
                    output.accept(ObjectRegistry.BLACK_PLASTER.get());
                    output.accept(ObjectRegistry.BROWN_PLASTER.get());
                    output.accept(ObjectRegistry.RED_PLASTER.get());
                    output.accept(ObjectRegistry.ORANGE_PLASTER.get());
                    output.accept(ObjectRegistry.YELLOW_PLASTER.get());
                    output.accept(ObjectRegistry.LIME_PLASTER.get());
                    output.accept(ObjectRegistry.GREEN_PLASTER.get());
                    output.accept(ObjectRegistry.CYAN_PLASTER.get());
                    output.accept(ObjectRegistry.LIGHT_BLUE_PLASTER.get());
                    output.accept(ObjectRegistry.BLUE_PLASTER.get());
                    output.accept(ObjectRegistry.PURPLE_PLASTER.get());
                    output.accept(ObjectRegistry.MAGENTA_PLASTER.get());
                    output.accept(ObjectRegistry.PINK_PLASTER.get());
                    output.accept(ObjectRegistry.SHINGLE_ROOF_BLOCK.get());
                    output.accept(ObjectRegistry.SHINGLE_ROOF_STAIRS.get());
                    output.accept(ObjectRegistry.SHINGLE_ROOF_SLAB.get());
                    output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_BLOCK.get());
                    output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_STAIRS.get());
                    output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_ROOF_SLAB.get());
                    output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_BLOCK.get());
                    output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_STAIRS.get());
                    output.accept(ObjectRegistry.PATCHWORK_SHINGLE_ROOF_SLAB.get());

                    output.accept(ObjectRegistry.FIELDSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.FIELDSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.FIELDSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.FIELDSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.ROSESTONE_BRICKS.get());
                    output.accept(ObjectRegistry.ROSESTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.ROSESTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.ROSESTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.WHITESTONE_BRICKS.get());
                    output.accept(ObjectRegistry.WHITESTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.WHITESTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.WHITESTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.CINDERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.CINDERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.CINDERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.CINDERSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.MARLSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MARLSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MARLSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MARLSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.AMBERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.AMBERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.AMBERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.AMBERSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICKS_WALL.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICKS.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICKS_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICKS_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICKS_WALL.get());

                    output.accept(ObjectRegistry.SPLITSTONE.get());
                    output.accept(ObjectRegistry.SPLITSTONE_STAIRS.get());
                    output.accept(ObjectRegistry.SPLITSTONE_SLAB.get());
                    output.accept(ObjectRegistry.MOSSY_SPLITSTONE.get());
                    output.accept(ObjectRegistry.MOSSY_SPLITSTONE_STAIRS.get());
                    output.accept(ObjectRegistry.MOSSY_SPLITSTONE_SLAB.get());
                    output.accept(ObjectRegistry.SPLITSTONE_PATH.get());
                    output.accept(ObjectRegistry.MOSSY_SPLITSTONE_PATH.get());

                    output.accept(ObjectRegistry.PACKED_DIRT.get());
                    output.accept(ObjectRegistry.TRAMPLED_PACKED_DIRT.get());
                    output.accept(ObjectRegistry.STABLE_FLOOR.get());
                    output.accept(ObjectRegistry.STRAW_STABLE_FLOOR.get());
                    output.accept(ObjectRegistry.TRAMPLED_STABLE_FLOOR.get());
                    output.accept(ObjectRegistry.RUSTIC_TIMBER_FLOOR.get());
                    output.accept(ObjectRegistry.RUSTIC_BED.get());
                    output.accept(ObjectRegistry.RUSTIC_SOFA.get());
                    output.accept(ObjectRegistry.RUSTIC_CHAIR.get());
                    output.accept(ObjectRegistry.RUSTIC_TABLE.get());
                    output.accept(ObjectRegistry.RUSTIC_WARDROBE.get());
                    output.accept(ObjectRegistry.RUSTIC_SINK.get());
                    output.accept(ObjectRegistry.RUSTIC_SMOKER.get());
                    output.accept(ObjectRegistry.RUSTIC_COOKING_AISLE.get());
                    output.accept(ObjectRegistry.RUSTIC_CABINET.get());
                    output.accept(ObjectRegistry.RUSTIC_DRESSER.get());
                    output.accept(ObjectRegistry.RUSTIC_WALL_CABINET.get());
                    output.accept(ObjectRegistry.RUSTIC_PRIVY.get());
                    output.accept(ObjectRegistry.RUSTIC_WASHBASIN.get());
                    output.accept(ObjectRegistry.RUSTIC_BATHTUB.get());
                    output.accept(ObjectRegistry.RUSTIC_GLASS_PANE.get());
                    output.accept(ObjectRegistry.RUSTIC_GLASS_BLOCK.get());
                    output.accept(ObjectRegistry.FOUNDATION_BLOCK.get());
                    output.accept(ObjectRegistry.SLIDING_BARN_DOOR.get());
                    output.accept(ObjectRegistry.SLIDING_HAYLOFT_DOOR.get());
                    output.accept(ObjectRegistry.SLIDING_STABLE_DOOR.get());
                })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
