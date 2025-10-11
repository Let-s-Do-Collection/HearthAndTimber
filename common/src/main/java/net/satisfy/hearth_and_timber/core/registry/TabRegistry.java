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
                output.accept(ObjectRegistry.TIMBER_FOUNDATION.get());
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
                output.accept(ObjectRegistry.FIELDSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.FIELDSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.FIELDSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_FIELDSTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICKS.get());
                output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.LAYERED_FIELDSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_FIELDSTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.ROSESTONE_BRICKS.get());
                output.accept(ObjectRegistry.ROSESTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.ROSESTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.ROSESTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_ROSESTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.WHITESTONE_BRICKS.get());
                output.accept(ObjectRegistry.WHITESTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.WHITESTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.WHITESTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_WHITESTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.CINDERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.CINDERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.CINDERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.CINDERSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_CINDERSTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.MARLSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MARLSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MARLSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MARLSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_MARLSTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.AMBERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.AMBERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.AMBERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.AMBERSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_AMBERSTONE_BRICK_WALL.get());

                output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.LAYERED_AMBERSTONE_BRICK_WALL.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICKS.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICK_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICK_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_LAYERED_AMBERSTONE_BRICK_WALL.get());

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

                output.accept(ObjectRegistry.OAK_BEAM.get());
                output.accept(ObjectRegistry.SPRUCE_BEAM.get());
                output.accept(ObjectRegistry.BIRCH_BEAM.get());
                output.accept(ObjectRegistry.JUNGLE_BEAM.get());
                output.accept(ObjectRegistry.ACACIA_BEAM.get());
                output.accept(ObjectRegistry.DARK_OAK_BEAM.get());
                output.accept(ObjectRegistry.MANGROVE_BEAM.get());
                output.accept(ObjectRegistry.CHERRY_BEAM.get());
                output.accept(ObjectRegistry.PALE_OAK_BEAM.get());

                output.accept(ObjectRegistry.OAK_RAILING.get());
                output.accept(ObjectRegistry.SPRUCE_RAILING.get());
                output.accept(ObjectRegistry.BIRCH_RAILING.get());
                output.accept(ObjectRegistry.JUNGLE_RAILING.get());
                output.accept(ObjectRegistry.ACACIA_RAILING.get());
                output.accept(ObjectRegistry.DARK_OAK_RAILING.get());
                output.accept(ObjectRegistry.MANGROVE_RAILING.get());
                output.accept(ObjectRegistry.CHERRY_RAILING.get());
                output.accept(ObjectRegistry.PALE_OAK_RAILING.get());

                output.accept(ObjectRegistry.OAK_SUPPORT.get());
                output.accept(ObjectRegistry.SPRUCE_SUPPORT.get());
                output.accept(ObjectRegistry.BIRCH_SUPPORT.get());
                output.accept(ObjectRegistry.JUNGLE_SUPPORT.get());
                output.accept(ObjectRegistry.ACACIA_SUPPORT.get());
                output.accept(ObjectRegistry.DARK_OAK_SUPPORT.get());
                output.accept(ObjectRegistry.MANGROVE_SUPPORT.get());
                output.accept(ObjectRegistry.CHERRY_SUPPORT.get());
                output.accept(ObjectRegistry.PALE_OAK_SUPPORT.get());

                output.accept(ObjectRegistry.OAK_PILLAR.get());
                output.accept(ObjectRegistry.SPRUCE_PILLAR.get());
                output.accept(ObjectRegistry.BIRCH_PILLAR.get());
                output.accept(ObjectRegistry.JUNGLE_PILLAR.get());
                output.accept(ObjectRegistry.ACACIA_PILLAR.get());
                output.accept(ObjectRegistry.DARK_OAK_PILLAR.get());
                output.accept(ObjectRegistry.MANGROVE_PILLAR.get());
                output.accept(ObjectRegistry.CHERRY_PILLAR.get());
                output.accept(ObjectRegistry.PALE_OAK_PILLAR.get());

                output.accept(ObjectRegistry.OAK_WINDOW_CASING.get());
                output.accept(ObjectRegistry.SPRUCE_WINDOW_CASING.get());
                output.accept(ObjectRegistry.BIRCH_WINDOW_CASING.get());
                output.accept(ObjectRegistry.JUNGLE_WINDOW_CASING.get());
                output.accept(ObjectRegistry.ACACIA_WINDOW_CASING.get());
                output.accept(ObjectRegistry.DARK_OAK_WINDOW_CASING.get());
                output.accept(ObjectRegistry.MANGROVE_WINDOW_CASING.get());
                output.accept(ObjectRegistry.CHERRY_WINDOW_CASING.get());
                output.accept(ObjectRegistry.PALE_OAK_WINDOW_CASING.get());

                output.accept(ObjectRegistry.FRAMEWORK.get());

                output.accept(ObjectRegistry.SLIDING_BARN_DOOR.get());
                output.accept(ObjectRegistry.SLIDING_HAYLOFT_DOOR.get());
                output.accept(ObjectRegistry.SLIDING_STABLE_DOOR.get());
            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
