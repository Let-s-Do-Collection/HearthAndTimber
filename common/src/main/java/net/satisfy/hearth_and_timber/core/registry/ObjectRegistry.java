package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.*;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;

import java.util.function.Supplier;

public class ObjectRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.ITEM);
    public static final Registrar<Item> ITEM_REGISTRAR = ITEMS.getRegistrar();
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.BLOCK);
    public static final Registrar<Block> BLOCK_REGISTRAR = BLOCKS.getRegistrar();

    public static final RegistrySupplier<Block> FRAMEWORK = registerWithItem("framework", FrameworkBlock::new);

    public static final RegistrySupplier<Block> OAK_SHINGLES = registerWithItem("oak_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> SPRUCE_SHINGLES = registerWithItem("spruce_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> BIRCH_SHINGLES = registerWithItem("birch_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> JUNGLE_SHINGLES = registerWithItem("jungle_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> ACACIA_SHINGLES = registerWithItem("acacia_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> DARK_OAK_SHINGLES = registerWithItem("dark_oak_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> MANGROVE_SHINGLES = registerWithItem("mangrove_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> CHERRY_SHINGLES = registerWithItem("cherry_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));
    public static final RegistrySupplier<Block> PALE_OAK_SHINGLES = registerWithItem("pale_oak_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f)));

    public static final RegistrySupplier<Block> OAK_SHINGLE_STAIRS = registerWithItem("oak_shingle_stairs", () -> new StairBlock(OAK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> SPRUCE_SHINGLE_STAIRS = registerWithItem("spruce_shingle_stairs", () -> new StairBlock(SPRUCE_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS)));
    public static final RegistrySupplier<Block> BIRCH_SHINGLE_STAIRS = registerWithItem("birch_shingle_stairs", () -> new StairBlock(BIRCH_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_STAIRS)));
    public static final RegistrySupplier<Block> JUNGLE_SHINGLE_STAIRS = registerWithItem("jungle_shingle_stairs", () -> new StairBlock(JUNGLE_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_STAIRS)));
    public static final RegistrySupplier<Block> ACACIA_SHINGLE_STAIRS = registerWithItem("acacia_shingle_stairs", () -> new StairBlock(ACACIA_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_STAIRS)));
    public static final RegistrySupplier<Block> DARK_OAK_SHINGLE_STAIRS = registerWithItem("dark_oak_shingle_stairs", () -> new StairBlock(DARK_OAK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_STAIRS)));
    public static final RegistrySupplier<Block> MANGROVE_SHINGLE_STAIRS = registerWithItem("mangrove_shingle_stairs", () -> new StairBlock(MANGROVE_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_STAIRS)));
    public static final RegistrySupplier<Block> CHERRY_SHINGLE_STAIRS = registerWithItem("cherry_shingle_stairs", () -> new StairBlock(CHERRY_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_STAIRS)));
    public static final RegistrySupplier<Block> PALE_OAK_SHINGLE_STAIRS = registerWithItem("pale_oak_shingle_stairs", () -> new StairBlock(PALE_OAK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));

    public static final RegistrySupplier<Block> OAK_SHINGLE_SLAB = registerWithItem("oak_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final RegistrySupplier<Block> SPRUCE_SHINGLE_SLAB = registerWithItem("spruce_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_SLAB)));
    public static final RegistrySupplier<Block> BIRCH_SHINGLE_SLAB = registerWithItem("birch_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_SLAB)));
    public static final RegistrySupplier<Block> JUNGLE_SHINGLE_SLAB = registerWithItem("jungle_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_SLAB)));
    public static final RegistrySupplier<Block> ACACIA_SHINGLE_SLAB = registerWithItem("acacia_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_SLAB)));
    public static final RegistrySupplier<Block> DARK_OAK_SHINGLE_SLAB = registerWithItem("dark_oak_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_SLAB)));
    public static final RegistrySupplier<Block> MANGROVE_SHINGLE_SLAB = registerWithItem("mangrove_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_SLAB)));
    public static final RegistrySupplier<Block> CHERRY_SHINGLE_SLAB = registerWithItem("cherry_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_SLAB)));
    public static final RegistrySupplier<Block> PALE_OAK_SHINGLE_SLAB = registerWithItem("pale_oak_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLES = registerWithItem("terracotta_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.TERRACOTTA_RED)));
    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLE_STAIRS = registerWithItem("terracotta_shingle_stairs", () -> new StairBlock(TERRACOTTA_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLE_SLAB = registerWithItem("terracotta_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLES = registerWithItem("patchwork_shingles", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLE_STAIRS = registerWithItem("patchwork_shingle_stairs", () -> new StairBlock(PATCHWORK_SHINGLES.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLE_SLAB = registerWithItem("patchwork_shingle_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final RegistrySupplier<Block> SPLITSTONE_PATH = registerWithItem("splitstone_path", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> SPLITSTONE = registerWithItem("splitstone", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), SPLITSTONE_PATH));
    public static final RegistrySupplier<Block> SPLITSTONE_STAIRS = registerWithItem("splitstone_stairs", () -> new StairBlock(SPLITSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> SPLITSTONE_SLAB = registerWithItem("splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_PATH = registerWithItem("mossy_splitstone_path", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE = registerWithItem("mossy_splitstone", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), MOSSY_SPLITSTONE_PATH));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_STAIRS = registerWithItem("mossy_splitstone_stairs", () -> new StairBlock(MOSSY_SPLITSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_SLAB = registerWithItem("mossy_splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> COVERED_RUBBLESTONE = registerWithItem("covered_rubblestone", () -> new RubbleMasonryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get(), () -> null));
    public static final RegistrySupplier<Block> COVERED_RUBBLESTONE_STAIRS = registerWithItem("covered_rubblestone_stairs", () -> new RubbleMasonryStairs(() -> ObjectRegistry.COVERED_RUBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get(), () -> null));
    public static final RegistrySupplier<Block> COVERED_RUBBLESTONE_SLAB = registerWithItem("covered_rubblestone_slab", () -> new RubbleMasonrySlab(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get(), () -> null));
    public static final RegistrySupplier<Block> COVERED_RUBBLESTONE_WALL = registerWithItem("covered_rubblestone_wall", () -> new RubbleMasonryWall(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get(), () -> null));
    public static final RegistrySupplier<Block> PLASTERED_RUBBLESTONE = registerWithItem("plastered_rubblestone", () -> new RubbleMasonryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), () -> ObjectRegistry.POINTED_RUBBLESTONE.get(), () -> ObjectRegistry.COVERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> PLASTERED_RUBBLESTONE_STAIRS = registerWithItem("plastered_rubblestone_stairs", () -> new RubbleMasonryStairs(() -> ObjectRegistry.PLASTERED_RUBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS), () -> ObjectRegistry.POINTED_RUBBLESTONE.get(), () -> ObjectRegistry.COVERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> PLASTERED_RUBBLESTONE_SLAB = registerWithItem("plastered_rubblestone_slab", () -> new RubbleMasonrySlab(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB), () -> ObjectRegistry.POINTED_RUBBLESTONE.get(), () -> ObjectRegistry.COVERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> PLASTERED_RUBBLESTONE_WALL = registerWithItem("plastered_rubblestone_wall", () -> new RubbleMasonryWall(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL), () -> ObjectRegistry.POINTED_RUBBLESTONE.get(), () -> ObjectRegistry.COVERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> POINTED_RUBBLESTONE = registerWithItem("pointed_rubblestone", () -> new RubbleMasonryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), () -> ObjectRegistry.GROUTLESS_RUBBLESTONE.get(), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> POINTED_RRUBBLESTONE_STAIRS = registerWithItem("pointed_rubblestone_stairs", () -> new RubbleMasonryStairs(() -> ObjectRegistry.POINTED_RUBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS), () -> ObjectRegistry.GROUTLESS_RUBBLESTONE.get(), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> POINTED_RRUBBLESTONE_SLAB = registerWithItem("pointed_rubblestone_slab", () -> new RubbleMasonrySlab(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB), () -> ObjectRegistry.GROUTLESS_RUBBLESTONE.get(), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> POINTED_RRUBBLESTONE_WALL = registerWithItem("pointed_rubblestone_wall", () -> new RubbleMasonryWall(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL), () -> ObjectRegistry.GROUTLESS_RUBBLESTONE.get(), () -> ObjectRegistry.PLASTERED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> GROUTLESS_RUBBLESTONE = registerWithItem("groutless_rubblestone", () -> new RubbleMasonryBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), () -> null, () -> ObjectRegistry.POINTED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> GROUTLESS_RUBBLESTONE_STAIRS = registerWithItem("groutless_rubblestone_stairs", () -> new RubbleMasonryStairs(() -> ObjectRegistry.GROUTLESS_RUBBLESTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_STAIRS), () -> null, () -> ObjectRegistry.POINTED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> GROUTLESS_RUBBLESTONE_SLAB = registerWithItem("groutless_rubblestone_slab", () -> new RubbleMasonrySlab(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_SLAB), () -> null, () -> ObjectRegistry.POINTED_RUBBLESTONE.get()));
    public static final RegistrySupplier<Block> GROUTLESS_RUBBLESTONE_WALL = registerWithItem("groutless_rubblestone_wall", () -> new RubbleMasonryWall(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICK_WALL), () -> null, () -> ObjectRegistry.POINTED_RUBBLESTONE.get()));

    public static final RegistrySupplier<Block> OAK_BEAM = registerWithItem("oak_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> SPRUCE_BEAM = registerWithItem("spruce_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> BIRCH_BEAM = registerWithItem("birch_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> JUNGLE_BEAM = registerWithItem("jungle_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> ACACIA_BEAM = registerWithItem("acacia_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> DARK_OAK_BEAM = registerWithItem("dark_oak_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> MANGROVE_BEAM = registerWithItem("mangrove_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> CHERRY_BEAM = registerWithItem("cherry_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_LOG).sound(SoundType.WOOD)));
    public static final RegistrySupplier<Block> PALE_OAK_BEAM = registerWithItem("pale_oak_beam", () -> new RotatedPillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG).sound(SoundType.WOOD)));

    public static final RegistrySupplier<Block> OAK_SUPPORT = registerWithItem("oak_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> SPRUCE_SUPPORT = registerWithItem("spruce_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)));
    public static final RegistrySupplier<Block> BIRCH_SUPPORT = registerWithItem("birch_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS)));
    public static final RegistrySupplier<Block> JUNGLE_SUPPORT = registerWithItem("jungle_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS)));
    public static final RegistrySupplier<Block> ACACIA_SUPPORT = registerWithItem("acacia_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)));
    public static final RegistrySupplier<Block> DARK_OAK_SUPPORT = registerWithItem("dark_oak_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistrySupplier<Block> MANGROVE_SUPPORT = registerWithItem("mangrove_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS)));
    public static final RegistrySupplier<Block> CHERRY_SUPPORT = registerWithItem("cherry_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));
    public static final RegistrySupplier<Block> PALE_OAK_SUPPORT = registerWithItem("pale_oak_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final RegistrySupplier<Block> OAK_PILLAR = registerWithItem("oak_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> SPRUCE_PILLAR = registerWithItem("spruce_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS)));
    public static final RegistrySupplier<Block> BIRCH_PILLAR = registerWithItem("birch_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS)));
    public static final RegistrySupplier<Block> JUNGLE_PILLAR = registerWithItem("jungle_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS)));
    public static final RegistrySupplier<Block> ACACIA_PILLAR = registerWithItem("acacia_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)));
    public static final RegistrySupplier<Block> DARK_OAK_PILLAR = registerWithItem("dark_oak_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistrySupplier<Block> MANGROVE_PILLAR = registerWithItem("mangrove_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS)));
    public static final RegistrySupplier<Block> CHERRY_PILLAR = registerWithItem("cherry_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));
    public static final RegistrySupplier<Block> PALE_OAK_PILLAR = registerWithItem("pale_oak_pillar", () -> new PillarBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));

    public static final RegistrySupplier<Block> OAK_RAILING = registerWithItem("oak_railing", () -> new RailingBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_RAILING = registerWithItem("spruce_railing", () -> new RailingBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> BIRCH_RAILING = registerWithItem("birch_railing", () -> new RailingBlock(Blocks.BIRCH_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> JUNGLE_RAILING = registerWithItem("jungle_railing", () -> new RailingBlock(Blocks.JUNGLE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> ACACIA_RAILING = registerWithItem("acacia_railing", () -> new RailingBlock(Blocks.ACACIA_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_OAK_RAILING = registerWithItem("dark_oak_railing", () -> new RailingBlock(Blocks.DARK_OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> MANGROVE_RAILING = registerWithItem("mangrove_railing", () -> new RailingBlock(Blocks.MANGROVE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> CHERRY_RAILING = registerWithItem("cherry_railing", () -> new RailingBlock(Blocks.CHERRY_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> PALE_OAK_RAILING = registerWithItem("pale_oak_railing", () -> new RailingBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistrySupplier<Block> OAK_WINDOW_CASING = registerWithItem("oak_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> SPRUCE_WINDOW_CASING = registerWithItem("spruce_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> MANGROVE_WINDOW_CASING = registerWithItem("mangrove_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> BIRCH_WINDOW_CASING = registerWithItem("birch_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> JUNGLE_WINDOW_CASING = registerWithItem("jungle_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> DARK_OAK_WINDOW_CASING = registerWithItem("dark_oak_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> ACACIA_WINDOW_CASING = registerWithItem("acacia_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> CHERRY_WINDOW_CASING = registerWithItem("cherry_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> PALE_OAK_WINDOW_CASING = registerWithItem("pale_oak_window_casing", () -> new WindowCasingBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistrySupplier<Block> OAK_BOARD = registerWithItem("oak_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> SPRUCE_BOARD = registerWithItem("spruce_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> BIRCH_BOARD = registerWithItem("birch_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> JUNGLE_BOARD = registerWithItem("jungle_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> ACACIA_BOARD = registerWithItem("acacia_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> DARK_OAK_BOARD = registerWithItem("dark_oak_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> MANGROVE_BOARD = registerWithItem("mangrove_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> CHERRY_BOARD = registerWithItem("cherry_board", WoodenBoardBlock::new);
    public static final RegistrySupplier<Block> PALE_OAK_BOARD = registerWithItem("pale_oak_board", WoodenBoardBlock::new);

    public static final RegistrySupplier<Block> OAK_WINDOW_PANE = registerWithItem("oak_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> SPRUCE_WINDOW_PANE = registerWithItem("spruce_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> BIRCH_WINDOW_PANE = registerWithItem("birch_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> JUNGLE_WINDOW_PANE = registerWithItem("jungle_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> ACACIA_WINDOW_PANE = registerWithItem("acacia_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> DARK_OAK_WINDOW_PANE = registerWithItem("dark_oak_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> MANGROVE_WINDOW_PANE = registerWithItem("mangrove_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> CHERRY_WINDOW_PANE = registerWithItem("cherry_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));
    public static final RegistrySupplier<Block> PALE_OAK_WINDOW_PANE = registerWithItem("pale_oak_window_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS_PANE).sound(SoundType.GLASS)));

    public static final RegistrySupplier<Block> OAK_WINDOW = registerWithItem("oak_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> SPRUCE_WINDOW = registerWithItem("spruce_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> BIRCH_WINDOW = registerWithItem("birch_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> JUNGLE_WINDOW = registerWithItem("jungle_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> ACACIA_WINDOW = registerWithItem("acacia_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> DARK_OAK_WINDOW = registerWithItem("dark_oak_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> MANGROVE_WINDOW = registerWithItem("mangrove_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> CHERRY_WINDOW = registerWithItem("cherry_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));
    public static final RegistrySupplier<Block> PALE_OAK_WINDOW = registerWithItem("pale_oak_window", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

    public static final RegistrySupplier<Block> TIMBER_FRAME = registerWithItem("timber_frame", () -> new TimberFrameBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_FRAME_STAIRS = registerWithItem("timber_frame_stairs", () -> new TimberFrameStairsBlock(TIMBER_FRAME.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_CROSS_FRAME = registerWithItem("timber_cross_frame", () -> new TimberFrameBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_DIAGONAL_FRAME = registerWithItem("timber_diagonal_frame", () -> new TimberDiagonalFrameBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_GRID_FRAME = registerWithItem("timber_grid_frame", () -> new TimberFrameBlock(Blocks.OAK_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()));

    public static final RegistrySupplier<Block> QUICKLIME = registerWithItem("quicklime", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CLAY).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistrySupplier<Block> BLACK_PLASTER = registerWithItem("black_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BLACK_TERRACOTTA).mapColor(MapColor.COLOR_BLACK)));
    public static final RegistrySupplier<Block> BLUE_PLASTER = registerWithItem("blue_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BLUE_TERRACOTTA).mapColor(MapColor.COLOR_BLUE)));
    public static final RegistrySupplier<Block> BROWN_PLASTER = registerWithItem("brown_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.BROWN_TERRACOTTA).mapColor(MapColor.COLOR_BROWN)));
    public static final RegistrySupplier<Block> CYAN_PLASTER = registerWithItem("cyan_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.CYAN_TERRACOTTA).mapColor(MapColor.COLOR_CYAN)));
    public static final RegistrySupplier<Block> GRAY_PLASTER = registerWithItem("gray_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_TERRACOTTA).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistrySupplier<Block> GREEN_PLASTER = registerWithItem("green_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GREEN_TERRACOTTA).mapColor(MapColor.COLOR_GREEN)));
    public static final RegistrySupplier<Block> LIGHT_BLUE_PLASTER = registerWithItem("light_blue_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_BLUE)));
    public static final RegistrySupplier<Block> LIGHT_GRAY_PLASTER = registerWithItem("light_gray_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_GRAY_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistrySupplier<Block> LIME_PLASTER = registerWithItem("lime_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LIME_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final RegistrySupplier<Block> MAGENTA_PLASTER = registerWithItem("magenta_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.MAGENTA_TERRACOTTA).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<Block> ORANGE_PLASTER = registerWithItem("orange_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_TERRACOTTA).mapColor(MapColor.COLOR_ORANGE)));
    public static final RegistrySupplier<Block> PINK_PLASTER = registerWithItem("pink_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PINK_TERRACOTTA).mapColor(MapColor.COLOR_PINK)));
    public static final RegistrySupplier<Block> PURPLE_PLASTER = registerWithItem("purple_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.PURPLE_TERRACOTTA).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistrySupplier<Block> RED_PLASTER = registerWithItem("red_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.RED_TERRACOTTA).mapColor(MapColor.COLOR_RED)));
    public static final RegistrySupplier<Block> YELLOW_PLASTER = registerWithItem("yellow_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_TERRACOTTA).mapColor(MapColor.COLOR_RED)));
    public static final RegistrySupplier<Block> WHITE_PLASTER = registerWithItem("white_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_RED)));

    public static final RegistrySupplier<Block> THATCH = registerWithItem("thatch", () -> new WeatheringThatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).randomTicks(), () -> ObjectRegistry.WEATHERED_THATCH.get()));
    public static final RegistrySupplier<Block> WEATHERED_THATCH = registerWithItem("weathered_thatch", () -> new WeatheringThatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).randomTicks(), () -> ObjectRegistry.DRYING_THATCH.get()));
    public static final RegistrySupplier<Block> DRYING_THATCH = registerWithItem("drying_thatch", () -> new WeatheringThatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).randomTicks(), () -> ObjectRegistry.AGED_THATCH.get()));
    public static final RegistrySupplier<Block> AGED_THATCH = registerWithItem("aged_thatch", () -> new WeatheringThatchBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).randomTicks(), () -> null));
    public static final RegistrySupplier<Block> THATCH_STAIRS = registerWithItem("thatch_stairs", () -> new WeatheringThatchStairs(() -> THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.WEATHERED_THATCH_STAIRS.get()));
    public static final RegistrySupplier<Block> WEATHERED_THATCH_STAIRS = registerWithItem("weathered_thatch_stairs", () -> new WeatheringThatchStairs(() -> WEATHERED_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.DRYING_THATCH_STAIRS.get()));
    public static final RegistrySupplier<Block> DRYING_THATCH_STAIRS = registerWithItem("drying_thatch_stairs", () -> new WeatheringThatchStairs(() -> DRYING_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.AGED_THATCH_STAIRS.get()));
    public static final RegistrySupplier<Block> AGED_THATCH_STAIRS = registerWithItem("aged_thatch_stairs", () -> new WeatheringThatchStairs(() -> AGED_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS).randomTicks(), () -> null));
    public static final RegistrySupplier<Block> THATCH_SLAB = registerWithItem("thatch_slab", () -> new WeatheringThatchSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.WEATHERED_THATCH_SLAB.get()));
    public static final RegistrySupplier<Block> WEATHERED_THATCH_SLAB = registerWithItem("weathered_thatch_slab", () -> new WeatheringThatchSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.DRYING_THATCH_SLAB.get()));
    public static final RegistrySupplier<Block> DRYING_THATCH_SLAB = registerWithItem("drying_thatch_slab", () -> new WeatheringThatchSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS).randomTicks(), () -> ObjectRegistry.AGED_THATCH_SLAB.get()));
    public static final RegistrySupplier<Block> AGED_THATCH_SLAB = registerWithItem("aged_thatch_slab", () -> new WeatheringThatchSlab(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS).randomTicks(), () -> null));

    public static final RegistrySupplier<Block> WAXED_THATCH = registerWithItem("waxed_thatch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK)));
    public static final RegistrySupplier<Block> WAXED_WEATHERED_THATCH = registerWithItem("waxed_weathered_thatch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.6f, 1.2f).mapColor(MapColor.TERRACOTTA_YELLOW)));
    public static final RegistrySupplier<Block> WAXED_DRYING_THATCH = registerWithItem("waxed_drying_thatch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.7f, 1.4f).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final RegistrySupplier<Block> WAXED_AGED_THATCH = registerWithItem("waxed_aged_thatch", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.8f, 1.6f).mapColor(MapColor.COLOR_BROWN)));
    public static final RegistrySupplier<Block> WAXED_THATCH_STAIRS = registerWithItem("waxed_thatch_stairs", () -> new StairBlock(ObjectRegistry.WAXED_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_WEATHERED_THATCH_STAIRS = registerWithItem("waxed_weathered_thatch_stairs", () -> new StairBlock(ObjectRegistry.WAXED_WEATHERED_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_DRYING_THATCH_STAIRS = registerWithItem("waxed_drying_thatch_stairs", () -> new StairBlock(ObjectRegistry.WAXED_DRYING_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_AGED_THATCH_STAIRS = registerWithItem("waxed_aged_thatch_stairs", () -> new StairBlock(ObjectRegistry.WAXED_AGED_THATCH.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_THATCH_SLAB = registerWithItem("waxed_thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_WEATHERED_THATCH_SLAB = registerWithItem("waxed_weathered_thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_DRYING_THATCH_SLAB = registerWithItem("waxed_drying_thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS)));
    public static final RegistrySupplier<Block> WAXED_AGED_THATCH_SLAB = registerWithItem("waxed_aged_thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB).sound(SoundType.GRASS)));

    public static final RegistrySupplier<Block> TIMBER_FOUNDATION = registerWithItem("timber_foundation", () -> new TimberFoundationBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_BASE_TRIM = registerWithItem("timber_base_trim", () -> new TimberBaseTrimBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_BASE_SKIRT = registerWithItem("timber_base_skirt", () -> new TimberBaseSkirtBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));


    public static final RegistrySupplier<Block> SLIDING_HAYLOFT_DOOR = registerWithItem("sliding_hayloft_door", () -> new SlidingHayloftDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));
    public static final RegistrySupplier<Block> SLIDING_BARN_DOOR = registerWithItem("sliding_barn_door", () -> new SlidingBarnDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));
    public static final RegistrySupplier<Block> SLIDING_STABLE_DOOR = registerWithItem("sliding_stable_door", () -> new SlidingStableDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));


    public static void init() {
        ITEMS.register();
        BLOCKS.register();

    }

    public static BlockBehaviour.Properties properties(float strength) {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist) {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return GeneralUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, HearthAndTimber.identifier(name), block);
    }
}


