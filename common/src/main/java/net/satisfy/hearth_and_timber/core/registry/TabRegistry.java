package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.platform.Platform;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.satisfy.hearth_and_timber.HearthAndTimber;

import java.util.Map;

@SuppressWarnings("unused")
public class TabRegistry {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.CREATIVE_MODE_TAB);

    public static final RegistrySupplier<CreativeModeTab> HEARTH_AND_TIMBER_TAB = CREATIVE_MODE_TABS.register("hearth_and_timber", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(ObjectRegistry.SLIDING_HAYLOFT_DOOR.get()))
            .title(Component.translatable("creativetab.hearth_and_timber.tab"))
            .displayItems((parameters, output) -> {
                output.accept(ObjectRegistry.FRAMEWORK_ITEM.get());
                output.accept(ObjectRegistry.TIMBER_BASE_SKIRT.get());
                output.accept(ObjectRegistry.TIMBER_BASE_TRIM.get());
                output.accept(ObjectRegistry.TIMBER_FOUNDATION.get());
                output.accept(ObjectRegistry.TIMBER_FRAME_STAIRS.get());
                output.accept(ObjectRegistry.TIMBER_FRAME.get());
                output.accept(ObjectRegistry.TIMBER_GRID_FRAME.get());
                output.accept(ObjectRegistry.TIMBER_DIAGONAL_FRAME.get());
                output.accept(ObjectRegistry.TIMBER_CROSS_FRAME.get());
                output.accept(ObjectRegistry.OAK_BEAM.get());
                output.accept(ObjectRegistry.SPRUCE_BEAM.get());
                output.accept(ObjectRegistry.BIRCH_BEAM.get());
                output.accept(ObjectRegistry.JUNGLE_BEAM.get());
                output.accept(ObjectRegistry.ACACIA_BEAM.get());
                output.accept(ObjectRegistry.DARK_OAK_BEAM.get());
                output.accept(ObjectRegistry.MANGROVE_BEAM.get());
                output.accept(ObjectRegistry.CHERRY_BEAM.get());
                output.accept(ObjectRegistry.PALE_OAK_BEAM.get());
                output.accept(ObjectRegistry.OAK_BOARD.get());
                output.accept(ObjectRegistry.SPRUCE_BOARD.get());
                output.accept(ObjectRegistry.BIRCH_BOARD.get());
                output.accept(ObjectRegistry.JUNGLE_BOARD.get());
                output.accept(ObjectRegistry.ACACIA_BOARD.get());
                output.accept(ObjectRegistry.DARK_OAK_BOARD.get());
                output.accept(ObjectRegistry.MANGROVE_BOARD.get());
                output.accept(ObjectRegistry.CHERRY_BOARD.get());
                output.accept(ObjectRegistry.PALE_OAK_BOARD.get());
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
                output.accept(ObjectRegistry.OAK_WINDOW_PANE.get());
                output.accept(ObjectRegistry.SPRUCE_WINDOW_PANE.get());
                output.accept(ObjectRegistry.BIRCH_WINDOW_PANE.get());
                output.accept(ObjectRegistry.JUNGLE_WINDOW_PANE.get());
                output.accept(ObjectRegistry.ACACIA_WINDOW_PANE.get());
                output.accept(ObjectRegistry.DARK_OAK_WINDOW_PANE.get());
                output.accept(ObjectRegistry.MANGROVE_WINDOW_PANE.get());
                output.accept(ObjectRegistry.CHERRY_WINDOW_PANE.get());
                output.accept(ObjectRegistry.PALE_OAK_WINDOW_PANE.get());
                output.accept(ObjectRegistry.OAK_WINDOW.get());
                output.accept(ObjectRegistry.SPRUCE_WINDOW.get());
                output.accept(ObjectRegistry.BIRCH_WINDOW.get());
                output.accept(ObjectRegistry.JUNGLE_WINDOW.get());
                output.accept(ObjectRegistry.ACACIA_WINDOW.get());
                output.accept(ObjectRegistry.DARK_OAK_WINDOW.get());
                output.accept(ObjectRegistry.MANGROVE_WINDOW.get());
                output.accept(ObjectRegistry.CHERRY_WINDOW.get());
                output.accept(ObjectRegistry.PALE_OAK_WINDOW.get());
                output.accept(ObjectRegistry.OAK_SHINGLES.get());
                output.accept(ObjectRegistry.SPRUCE_SHINGLES.get());
                output.accept(ObjectRegistry.BIRCH_SHINGLES.get());
                output.accept(ObjectRegistry.JUNGLE_SHINGLES.get());
                output.accept(ObjectRegistry.ACACIA_SHINGLES.get());
                output.accept(ObjectRegistry.DARK_OAK_SHINGLES.get());
                output.accept(ObjectRegistry.MANGROVE_SHINGLES.get());
                output.accept(ObjectRegistry.CHERRY_SHINGLES.get());
                output.accept(ObjectRegistry.PALE_OAK_SHINGLES.get());
                output.accept(ObjectRegistry.OAK_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.SPRUCE_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.BIRCH_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.JUNGLE_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.ACACIA_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.DARK_OAK_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.MANGROVE_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.CHERRY_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.PALE_OAK_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.OAK_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.SPRUCE_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.BIRCH_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.JUNGLE_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.ACACIA_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.DARK_OAK_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.MANGROVE_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.CHERRY_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.PALE_OAK_SHINGLE_SLAB.get());
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
                output.accept(ObjectRegistry.QUICKLIME.get());
                output.accept(ObjectRegistry.COVERED_RUBBLESTONE.get());
                output.accept(ObjectRegistry.COVERED_RUBBLESTONE_STAIRS.get());
                output.accept(ObjectRegistry.COVERED_RUBBLESTONE_SLAB.get());
                output.accept(ObjectRegistry.COVERED_RUBBLESTONE_WALL.get());
                output.accept(ObjectRegistry.PLASTERED_RUBBLESTONE.get());
                output.accept(ObjectRegistry.PLASTERED_RUBBLESTONE_STAIRS.get());
                output.accept(ObjectRegistry.PLASTERED_RUBBLESTONE_SLAB.get());
                output.accept(ObjectRegistry.PLASTERED_RUBBLESTONE_WALL.get());
                output.accept(ObjectRegistry.POINTED_RUBBLESTONE.get());
                output.accept(ObjectRegistry.POINTED_RRUBBLESTONE_STAIRS.get());
                output.accept(ObjectRegistry.POINTED_RRUBBLESTONE_SLAB.get());
                output.accept(ObjectRegistry.POINTED_RRUBBLESTONE_WALL.get());
                output.accept(ObjectRegistry.GROUTLESS_RUBBLESTONE.get());
                output.accept(ObjectRegistry.GROUTLESS_RUBBLESTONE_STAIRS.get());
                output.accept(ObjectRegistry.GROUTLESS_RUBBLESTONE_SLAB.get());
                output.accept(ObjectRegistry.GROUTLESS_RUBBLESTONE_WALL.get());
                output.accept(ObjectRegistry.SPLITSTONE.get());
                output.accept(ObjectRegistry.SPLITSTONE_STAIRS.get());
                output.accept(ObjectRegistry.SPLITSTONE_SLAB.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_STAIRS.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_SLAB.get());
                output.accept(ObjectRegistry.SPLITSTONE_PATH.get());
                output.accept(ObjectRegistry.MOSSY_SPLITSTONE_PATH.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLES.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.TERRACOTTA_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLES.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLE_STAIRS.get());
                output.accept(ObjectRegistry.PATCHWORK_SHINGLE_SLAB.get());
                output.accept(ObjectRegistry.THATCH.get());
                output.accept(ObjectRegistry.THATCH_STAIRS.get());
                output.accept(ObjectRegistry.THATCH_SLAB.get());
                output.accept(ObjectRegistry.WEATHERED_THATCH.get());
                output.accept(ObjectRegistry.WEATHERED_THATCH_STAIRS.get());
                output.accept(ObjectRegistry.WEATHERED_THATCH_SLAB.get());
                output.accept(ObjectRegistry.DRYING_THATCH.get());
                output.accept(ObjectRegistry.DRYING_THATCH_STAIRS.get());
                output.accept(ObjectRegistry.DRYING_THATCH_SLAB.get());
                output.accept(ObjectRegistry.AGED_THATCH.get());
                output.accept(ObjectRegistry.AGED_THATCH_STAIRS.get());
                output.accept(ObjectRegistry.AGED_THATCH_SLAB.get());
                output.accept(ObjectRegistry.SLIDING_BARN_DOOR.get());
                output.accept(ObjectRegistry.SLIDING_HAYLOFT_DOOR.get());
                output.accept(ObjectRegistry.SLIDING_STABLE_DOOR.get());
            })
            .build());

    public static RegistrySupplier<CreativeModeTab> HEARTH_AND_TIMBER_COMPAT_LAYER_TAB;

    static {
        boolean beachpartyLoaded = Platform.isModLoaded("beachparty");
        boolean meadowLoaded = Platform.isModLoaded("meadow");
        boolean vineryLoaded = Platform.isModLoaded("vinery");
        boolean bloomingNatureLoaded = Platform.isModLoaded("bloomingnature");

        if (beachpartyLoaded || meadowLoaded || vineryLoaded || bloomingNatureLoaded) {
            HEARTH_AND_TIMBER_COMPAT_LAYER_TAB = CREATIVE_MODE_TABS.register("hearth_and_timber_compat", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
                    .icon(() -> buildCompatIcon(beachpartyLoaded, meadowLoaded, vineryLoaded, bloomingNatureLoaded))
                    .title(Component.translatable("creativetab.hearth_and_timber.compat"))
                    .displayItems((parameters, output) -> {
                        String[] beachpartyWoodTypeOrder = {
                                "palm"
                        };
                        String[] meadowWoodTypeOrder = {
                                "pine"
                        };
                        String[] vineryWoodTypeOrder = {
                                "dark_cherry"
                        };
                        String[] bloomingNatureWoodTypeOrder = {
                                "aspen", "larch", "baobab", "cypress", "ebony", "chestnut", "fan_palm", "fir", "swamp_oak", "swamp_cypress"
                        };
                        String[] compatWoodTypeOrder = buildCompatWoodTypeOrder(beachpartyLoaded, meadowLoaded, vineryLoaded, bloomingNatureLoaded, beachpartyWoodTypeOrder, meadowWoodTypeOrder, vineryWoodTypeOrder, bloomingNatureWoodTypeOrder);

                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_SHINGLES, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_SHINGLE_STAIRS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_SHINGLE_SLAB, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_BEAMS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_SUPPORTS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_PILLARS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_RAILINGS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_WINDOW_CASINGS, woodType, output);
                        for (String woodType : compatWoodTypeOrder) acceptIfPresent(ObjectRegistry.COMPAT_BOARDS, woodType, output);
                    })
                    .build());
        }

        CREATIVE_MODE_TABS.register();
    }

    private static String[] buildCompatWoodTypeOrder(boolean beachpartyLoaded, boolean meadowLoaded, boolean vineryLoaded, boolean bloomingNatureLoaded, String[] beachpartyWoodTypeOrder, String[] meadowWoodTypeOrder, String[] vineryWoodTypeOrder, String[] bloomingNatureWoodTypeOrder) {
        String[] result = new String[0];

        if (beachpartyLoaded) result = concat(result, beachpartyWoodTypeOrder);
        if (meadowLoaded) result = concat(result, meadowWoodTypeOrder);
        if (vineryLoaded) result = concat(result, vineryWoodTypeOrder);
        if (bloomingNatureLoaded) result = concat(result, bloomingNatureWoodTypeOrder);

        return result;
    }

    private static String[] concat(String[] first, String[] second) {
        String[] result = new String[first.length + second.length];
        System.arraycopy(first, 0, result, 0, first.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    private static ItemStack buildCompatIcon(boolean beachpartyLoaded, boolean meadowLoaded, boolean vineryLoaded, boolean bloomingNatureLoaded) {
        if (beachpartyLoaded) {
            RegistrySupplier<?> supplier = ObjectRegistry.COMPAT_SHINGLES.get("palm");
            if (supplier != null) {
                Object value = supplier.get();
                if (value instanceof ItemLike itemLike) return new ItemStack(itemLike);
            }
        }
        if (meadowLoaded) {
            RegistrySupplier<?> supplier = ObjectRegistry.COMPAT_SHINGLES.get("pine");
            if (supplier != null) {
                Object value = supplier.get();
                if (value instanceof ItemLike itemLike) return new ItemStack(itemLike);
            }
        }
        if (vineryLoaded) {
            RegistrySupplier<?> supplier = ObjectRegistry.COMPAT_SHINGLES.get("dark_cherry");
            if (supplier != null) {
                Object value = supplier.get();
                if (value instanceof ItemLike itemLike) return new ItemStack(itemLike);
            }
        }
        if (bloomingNatureLoaded) {
            RegistrySupplier<?> supplier = ObjectRegistry.COMPAT_SHINGLES.get("aspen");
            if (supplier != null) {
                Object value = supplier.get();
                if (value instanceof ItemLike itemLike) return new ItemStack(itemLike);
            }
        }
        return new ItemStack(ObjectRegistry.OAK_SHINGLES.get());
    }

    private static void acceptIfPresent(Map<String, ? extends RegistrySupplier<?>> registrySuppliers, String key, CreativeModeTab.Output out) {
        RegistrySupplier<?> supplier = registrySuppliers.get(key);
        if (supplier != null) {
            supplier.ifPresent(value -> {
                if (value instanceof ItemLike itemLike) {
                    out.accept(new ItemStack(itemLike));
                }
            });
        }
    }
}