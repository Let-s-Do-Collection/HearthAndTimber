package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

    public static final RegistrySupplier<Block> SHINGLE_ROOF_BLOCK = registerWithItem("shingle_roof_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.TERRACOTTA_BLACK)));
    public static final RegistrySupplier<Block> SHINGLE_ROOF_STAIRS = registerWithItem("shingle_roof_stairs", () -> new StairBlock(SHINGLE_ROOF_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> SHINGLE_ROOF_SLAB = registerWithItem("shingle_roof_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLE_ROOF_BLOCK = registerWithItem("terracotta_shingle_roof_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.TERRACOTTA_RED)));
    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLE_ROOF_STAIRS = registerWithItem("terracotta_shingle_roof_stairs", () -> new StairBlock(TERRACOTTA_SHINGLE_ROOF_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> TERRACOTTA_SHINGLE_ROOF_SLAB = registerWithItem("terracotta_shingle_roof_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLE_ROOF_BLOCK = registerWithItem("patchwork_shingle_roof_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.TERRACOTTA_ORANGE)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLE_ROOF_STAIRS = registerWithItem("patchwork_shingle_roof_stairs", () -> new StairBlock(PATCHWORK_SHINGLE_ROOF_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_STAIRS)));
    public static final RegistrySupplier<Block> PATCHWORK_SHINGLE_ROOF_SLAB = registerWithItem("patchwork_shingle_roof_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SLAB)));

    public static final RegistrySupplier<Block> FIELDSTONE_BRICKS = registerWithItem("fieldstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> FIELDSTONE_BRICK_STAIRS = registerWithItem("fieldstone_brick_stairs", () -> new StairBlock(FIELDSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> FIELDSTONE_BRICK_SLAB = registerWithItem("fieldstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> FIELDSTONE_BRICK_WALL = registerWithItem("fieldstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BRICKS = registerWithItem("mossy_fieldstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BRICK_STAIRS = registerWithItem("mossy_fieldstone_brick_stairs", () -> new StairBlock(MOSSY_FIELDSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BRICK_SLAB = registerWithItem("mossy_fieldstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BRICK_WALL = registerWithItem("mossy_fieldstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> LAYERED_FIELDSTONE_BRICKS = registerWithItem("layered_fieldstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> LAYERED_FIELDSTONE_BRICK_STAIRS = registerWithItem("layered_fieldstone_brick_stairs", () -> new StairBlock(LAYERED_FIELDSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> LAYERED_FIELDSTONE_BRICK_SLAB = registerWithItem("layered_fieldstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> LAYERED_FIELDSTONE_BRICK_WALL = registerWithItem("layered_fieldstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_LAYERED_FIELDSTONE_BRICKS = registerWithItem("mossy_layered_fieldstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_FIELDSTONE_BRICK_STAIRS = registerWithItem("mossy_layered_fieldstone_brick_stairs", () -> new StairBlock(MOSSY_LAYERED_FIELDSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_FIELDSTONE_BRICK_SLAB = registerWithItem("mossy_layered_fieldstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_FIELDSTONE_BRICK_WALL = registerWithItem("mossy_layered_fieldstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> ROSESTONE_BRICKS = registerWithItem("rosestone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> ROSESTONE_BRICK_STAIRS = registerWithItem("rosestone_brick_stairs", () -> new StairBlock(ROSESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> ROSESTONE_BRICK_SLAB = registerWithItem("rosestone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> ROSESTONE_BRICK_WALL = registerWithItem("rosestone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_ROSESTONE_BRICKS = registerWithItem("mossy_rosestone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_ROSESTONE_BRICK_STAIRS = registerWithItem("mossy_rosestone_brick_stairs", () -> new StairBlock(MOSSY_ROSESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_ROSESTONE_BRICK_SLAB = registerWithItem("mossy_rosestone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_ROSESTONE_BRICK_WALL = registerWithItem("mossy_rosestone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> WHITESTONE_BRICKS = registerWithItem("whitestone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> WHITESTONE_BRICK_STAIRS = registerWithItem("whitestone_brick_stairs", () -> new StairBlock(WHITESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> WHITESTONE_BRICK_SLAB = registerWithItem("whitestone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> WHITESTONE_BRICK_WALL = registerWithItem("whitestone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_WHITESTONE_BRICKS = registerWithItem("mossy_whitestone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_WHITESTONE_BRICK_STAIRS = registerWithItem("mossy_whitestone_brick_stairs", () -> new StairBlock(MOSSY_WHITESTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_WHITESTONE_BRICK_SLAB = registerWithItem("mossy_whitestone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_WHITESTONE_BRICK_WALL = registerWithItem("mossy_whitestone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> AMBERSTONE_BRICKS = registerWithItem("amberstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> AMBERSTONE_BRICK_STAIRS = registerWithItem("amberstone_brick_stairs", () -> new StairBlock(AMBERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> AMBERSTONE_BRICK_SLAB = registerWithItem("amberstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> AMBERSTONE_BRICK_WALL = registerWithItem("amberstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_AMBERSTONE_BRICKS = registerWithItem("mossy_amberstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_AMBERSTONE_BRICK_STAIRS = registerWithItem("mossy_amberstone_brick_stairs", () -> new StairBlock(MOSSY_AMBERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_AMBERSTONE_BRICK_SLAB = registerWithItem("mossy_amberstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_AMBERSTONE_BRICK_WALL = registerWithItem("mossy_amberstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));


    public static final RegistrySupplier<Block> LAYERED_AMBERSTONE_BRICKS = registerWithItem("layered_amberstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> LAYERED_AMBERSTONE_BRICK_STAIRS = registerWithItem("layered_amberstone_brick_stairs", () -> new StairBlock(LAYERED_AMBERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> LAYERED_AMBERSTONE_BRICK_SLAB = registerWithItem("layered_amberstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> LAYERED_AMBERSTONE_BRICK_WALL = registerWithItem("layered_amberstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_LAYERED_AMBERSTONE_BRICKS = registerWithItem("mossy_layered_amberstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_AMBERSTONE_BRICK_STAIRS = registerWithItem("mossy_layered_amberstone_brick_stairs", () -> new StairBlock(MOSSY_LAYERED_AMBERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_AMBERSTONE_BRICK_SLAB = registerWithItem("mossy_layered_amberstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_LAYERED_AMBERSTONE_BRICK_WALL = registerWithItem("mossy_layered_amberstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MARLSTONE_BRICKS = registerWithItem("marlstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MARLSTONE_BRICK_STAIRS = registerWithItem("marlstone_brick_stairs", () -> new StairBlock(MARLSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MARLSTONE_BRICK_SLAB = registerWithItem("marlstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MARLSTONE_BRICK_WALL = registerWithItem("marlstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_MARLSTONE_BRICKS = registerWithItem("mossy_marlstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_MARLSTONE_BRICK_STAIRS = registerWithItem("mossy_marlstone_brick_stairs", () -> new StairBlock(MOSSY_MARLSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_MARLSTONE_BRICK_SLAB = registerWithItem("mossy_marlstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_MARLSTONE_BRICK_WALL = registerWithItem("mossy_marlstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> CINDERSTONE_BRICKS = registerWithItem("cinderstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> CINDERSTONE_BRICK_STAIRS = registerWithItem("cinderstone_brick_stairs", () -> new StairBlock(CINDERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> CINDERSTONE_BRICK_SLAB = registerWithItem("cinderstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> CINDERSTONE_BRICK_WALL = registerWithItem("cinderstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> MOSSY_CINDERSTONE_BRICKS = registerWithItem("mossy_cinderstone_bricks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE)));
    public static final RegistrySupplier<Block> MOSSY_CINDERSTONE_BRICK_STAIRS = registerWithItem("mossy_cinderstone_brick_stairs", () -> new StairBlock(MOSSY_CINDERSTONE_BRICKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_CINDERSTONE_BRICK_SLAB = registerWithItem("mossy_cinderstone_brick_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_CINDERSTONE_BRICK_WALL = registerWithItem("mossy_cinderstone_brick_wall", () -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_WALL)));

    public static final RegistrySupplier<Block> SPLITSTONE_PATH = registerWithItem("splitstone_path", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> SPLITSTONE = registerWithItem("splitstone", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), SPLITSTONE_PATH));
    public static final RegistrySupplier<Block> SPLITSTONE_STAIRS = registerWithItem("splitstone_stairs", () -> new StairBlock(SPLITSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> SPLITSTONE_SLAB = registerWithItem("splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_PATH = registerWithItem("mossy_splitstone_path", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE = registerWithItem("mossy_splitstone", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), MOSSY_SPLITSTONE_PATH));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_STAIRS = registerWithItem("mossy_splitstone_stairs", () -> new StairBlock(MOSSY_SPLITSTONE.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_SLAB = registerWithItem("mossy_splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> PACKED_DIRT = registerWithItem("packed_dirt", () -> new PackedDirtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(1.5f, 3.0f).sound(SoundType.PACKED_MUD)));
    public static final RegistrySupplier<Block> TRAMPLED_PACKED_DIRT = registerWithItem("trampled_packed_dirt", () -> new TrampledPackedDirtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(1.0f, 3.0f).sound(SoundType.PACKED_MUD)));

    public static final RegistrySupplier<Block> STABLE_FLOOR = registerWithItem("stable_floor", () -> new StableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).strength(1.5f, 3.0f)));
    public static final RegistrySupplier<Block> TRAMPLED_STABLE_FLOOR = registerWithItem("trampled_stable_floor", () -> new TrampledStableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).strength(1.2f, 2.5f)));
    public static final RegistrySupplier<Block> STRAW_STABLE_FLOOR = registerWithItem("straw_stable_floor", () -> new StrawStableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.GRASS).strength(0.8f, 1.0f)));

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

    public static final RegistrySupplier<Block> SLIDING_HAYLOFT_DOOR = registerWithItem("sliding_hayloft_door", () -> new SlidingHayloftDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));
    public static final RegistrySupplier<Block> SLIDING_BARN_DOOR = registerWithItem("sliding_barn_door", () -> new SlidingBarnDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));
    public static final RegistrySupplier<Block> SLIDING_STABLE_DOOR = registerWithItem("sliding_stable_door", () -> new SlidingStableDoorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.WOOD).sound(SoundType.WOOD).strength(2.0f, 3.0f).noOcclusion()));

    public static final RegistrySupplier<Block> TIMBER_FRAME = registerWithItem("timber_frame", () -> new TimberFrameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_FRAME_STAIRS = registerWithItem("timber_frame_stairs", () -> new StairBlock(TIMBER_FRAME.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_CROSS_FRAME = registerWithItem("timber_cross_frame", () -> new TimberFrameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_DIAGONAL_FRAME = registerWithItem("timber_diagonal_frame", () -> new TimberFrameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_GRID_FRAME = registerWithItem("timber_grid_frame", () -> new TimberFrameBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BAMBOO_PLANKS).noOcclusion()));

    public static final RegistrySupplier<Block> BLACK_PLASTER = registerWithItem("black_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_BLACK)));
    public static final RegistrySupplier<Block> BLUE_PLASTER = registerWithItem("blue_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_BLUE)));
    public static final RegistrySupplier<Block> BROWN_PLASTER = registerWithItem("brown_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_BROWN)));
    public static final RegistrySupplier<Block> CYAN_PLASTER = registerWithItem("cyan_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_CYAN)));
    public static final RegistrySupplier<Block> GRAY_PLASTER = registerWithItem("gray_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_GRAY)));
    public static final RegistrySupplier<Block> GREEN_PLASTER = registerWithItem("green_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_GREEN)));
    public static final RegistrySupplier<Block> LIGHT_BLUE_PLASTER = registerWithItem("light_blue_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_BLUE)));
    public static final RegistrySupplier<Block> LIGHT_GRAY_PLASTER = registerWithItem("light_gray_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistrySupplier<Block> LIME_PLASTER = registerWithItem("lime_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_LIGHT_GREEN)));
    public static final RegistrySupplier<Block> MAGENTA_PLASTER = registerWithItem("magenta_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_MAGENTA)));
    public static final RegistrySupplier<Block> ORANGE_PLASTER = registerWithItem("orange_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_ORANGE)));
    public static final RegistrySupplier<Block> PINK_PLASTER = registerWithItem("pink_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_PINK)));
    public static final RegistrySupplier<Block> PURPLE_PLASTER = registerWithItem("purple_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_PURPLE)));
    public static final RegistrySupplier<Block> RED_PLASTER = registerWithItem("red_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_RED)));
    public static final RegistrySupplier<Block> YELLOW_PLASTER = registerWithItem("yellow_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_RED)));
    public static final RegistrySupplier<Block> WHITE_PLASTER = registerWithItem("white_plaster", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_TERRACOTTA).mapColor(MapColor.COLOR_RED)));

    //public static final RegistrySupplier<Block> THATCH_BLOCK = registerWithItem("thatch_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.5f, 1.0f).mapColor(MapColor.COLOR_YELLOW)));
    //public static final RegistrySupplier<Block> THATCH_STAIRS = registerWithItem("thatch_stairs", () -> new StairBlock(THATCH_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.5f, 1.0f).mapColor(MapColor.COLOR_YELLOW)));
    //public static final RegistrySupplier<Block> THATCH_SLAB = registerWithItem("thatch_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.HAY_BLOCK).sound(SoundType.GRASS).strength(0.5f, 1.0f).mapColor(MapColor.COLOR_YELLOW)));

    public static final RegistrySupplier<Block> TIMBER_FOUNDATION = registerWithItem("timber_foundation", () -> new TimberFoundationBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_BASE_TRIM = registerWithItem("timber_base_trim", () -> new TimberBaseTrimBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));
    public static final RegistrySupplier<Block> TIMBER_BASE_SKIRT = registerWithItem("timber_base_skirt", () -> new TimberBaseSkirtBlock(Blocks.SPRUCE_PLANKS.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_STAIRS).noOcclusion()));

    public static void init() {
        ITEMS.register();
        BLOCKS.register();
        LifecycleEvent.SETUP.register(() -> {
            ((PackedDirtBlock) PACKED_DIRT.get()).setTrampled(TRAMPLED_PACKED_DIRT);
            ((TrampledPackedDirtBlock) TRAMPLED_PACKED_DIRT.get()).setBase(PACKED_DIRT);
            ((StableFloorBlock) STABLE_FLOOR.get()).setTrampled(TRAMPLED_STABLE_FLOOR);
            ((StableFloorBlock) STABLE_FLOOR.get()).setStraw(STRAW_STABLE_FLOOR);
            ((StrawStableFloorBlock) STRAW_STABLE_FLOOR.get()).setBase(STABLE_FLOOR);
            ((StrawStableFloorBlock) STRAW_STABLE_FLOOR.get()).setStrawItem(() -> Items.WHEAT);
        });
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

    /*
 TODO:
 - Add Quicklime (base material for Plaster)
 - Beam Textures
*/

}


