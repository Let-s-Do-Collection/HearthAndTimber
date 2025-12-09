package net.satisfy.hearth_and_timber.core.compat.everycomp;

import net.mehvahdjukaar.every_compat.EveryCompat;
import net.mehvahdjukaar.every_compat.api.PaletteStrategies;
import net.mehvahdjukaar.every_compat.api.SimpleEntrySet;
import net.mehvahdjukaar.every_compat.api.SimpleModule;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.api.set.wood.VanillaWoodTypes;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodType;
import net.mehvahdjukaar.moonlight.api.util.Utils;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.satisfy.hearth_and_timber.core.block.PillarBlock;
import net.satisfy.hearth_and_timber.core.block.RailingBlock;
import net.satisfy.hearth_and_timber.core.block.SupportBlock;
import net.satisfy.hearth_and_timber.core.block.WindowCasingBlock;
import net.satisfy.hearth_and_timber.core.block.WoodenBoardBlock;
import net.satisfy.hearth_and_timber.core.registry.ObjectRegistry;

public class HearthAndTimberWoodGoodModule extends SimpleModule {

    public final SimpleEntrySet<WoodType, Block> shingles;
    public final SimpleEntrySet<WoodType, Block> shingleStairs;
    public final SimpleEntrySet<WoodType, Block> shingleSlabs;
    public final SimpleEntrySet<WoodType, Block> beams;
    public final SimpleEntrySet<WoodType, Block> supports;
    public final SimpleEntrySet<WoodType, Block> pillars;
    public final SimpleEntrySet<WoodType, Block> railings;
    public final SimpleEntrySet<WoodType, Block> windowCasings;
    public final SimpleEntrySet<WoodType, Block> boards;

    public HearthAndTimberWoodGoodModule(String modId) {
        super(modId, "ht", EveryCompat.MOD_ID);

        shingles = SimpleEntrySet.builder(
                        WoodType.class,
                        "shingles",
                        ObjectRegistry.OAK_SHINGLES,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new Block(Utils.copyPropertySafe(woodType.planks))
                )
                .addTexture(modRes("block/oak_shingles"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(shingles);

        shingleStairs = SimpleEntrySet.builder(
                        WoodType.class,
                        "shingle_stairs",
                        ObjectRegistry.OAK_SHINGLE_STAIRS,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new StairBlock(woodType.planks.defaultBlockState(), Utils.copyPropertySafe(woodType.planks))
                )
                .addTexture(modRes("block/oak_shingles"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(shingleStairs);

        shingleSlabs = SimpleEntrySet.builder(
                        WoodType.class,
                        "shingle_slab",
                        ObjectRegistry.OAK_SHINGLE_SLAB,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new SlabBlock(Utils.copyPropertySafe(woodType.planks))
                )
                .addTexture(modRes("block/oak_shingles"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(shingleSlabs);

        beams = SimpleEntrySet.builder(
                        WoodType.class,
                        "beam",
                        ObjectRegistry.OAK_BEAM,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new RotatedPillarBlock(Utils.copyPropertySafe(woodType.log))
                )
                .addTexture(modRes("block/oak_beam_side"), PaletteStrategies.LOG_SIDE_STANDARD)
                .addTexture(modRes("block/oak_beam_top"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();

        supports = SimpleEntrySet.builder(
                        WoodType.class,
                        "support",
                        ObjectRegistry.OAK_SUPPORT,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new SupportBlock(Utils.copyPropertySafe(woodType.planks))
                )
                .addTexture(modRes("block/oak_support"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(supports);

        pillars = SimpleEntrySet.builder(
                        WoodType.class,
                        "pillar",
                        ObjectRegistry.OAK_PILLAR,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new PillarBlock(Utils.copyPropertySafe(woodType.planks))
                )
                .addTexture(modRes("block/oak_pillar"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(pillars);

        railings = SimpleEntrySet.builder(
                        WoodType.class,
                        "railing",
                        ObjectRegistry.OAK_RAILING,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new RailingBlock(woodType.planks.defaultBlockState(), Utils.copyPropertySafe(woodType.planks).noOcclusion())
                )
                .addTexture(modRes("block/oak_railing"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(railings);

        windowCasings = SimpleEntrySet.builder(
                        WoodType.class,
                        "window_casing",
                        ObjectRegistry.OAK_WINDOW_CASING,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new WindowCasingBlock(Utils.copyPropertySafe(woodType.planks).noOcclusion())
                )
                .addTexture(modRes("block/oak_window_casing"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(windowCasings);

        boards = SimpleEntrySet.builder(
                        WoodType.class,
                        "board",
                        ObjectRegistry.OAK_BOARD,
                        () -> VanillaWoodTypes.OAK,
                        woodType -> new WoodenBoardBlock()
                )
                .addTexture(modRes("block/oak_board"), PaletteStrategies.LOG_SIDE_STANDARD)
                .setTabKey(CreativeModeTabs.BUILDING_BLOCKS)
                .defaultRecipe()
                .build();
        this.addEntry(boards);
    }
}