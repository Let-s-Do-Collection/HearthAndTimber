package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registrar;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.core.block.*;
import net.satisfy.hearth_and_timber.core.block.BedBlock;
import net.satisfy.hearth_and_timber.core.util.GeneralUtil;
import net.satisfy.hearth_and_timber.core.block.SmokeOvenBlock;

import java.util.function.Consumer;
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

    public static final RegistrySupplier<Block> OAK_SUPPORT = registerWithItem("oak_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> SPRUCE_SUPPORT = registerWithItem("spruce_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)));
    public static final RegistrySupplier<Block> BIRCH_SUPPORT = registerWithItem("birch_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.BIRCH_PLANKS)));
    public static final RegistrySupplier<Block> JUNGLE_SUPPORT = registerWithItem("jungle_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.JUNGLE_PLANKS)));
    public static final RegistrySupplier<Block> ACACIA_SUPPORT = registerWithItem("acacia_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.ACACIA_PLANKS)));
    public static final RegistrySupplier<Block> DARK_OAK_SUPPORT = registerWithItem("dark_oak_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DARK_OAK_PLANKS)));
    public static final RegistrySupplier<Block> MANGROVE_SUPPORT = registerWithItem("mangrove_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.MANGROVE_PLANKS)));
    public static final RegistrySupplier<Block> CHERRY_SUPPORT = registerWithItem("cherry_support", () -> new SupportBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.CHERRY_PLANKS)));

    public static final RegistrySupplier<Block> RUSTIC_TIMBER_FLOOR = registerWithItem("rustic_timber_floor", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.SPRUCE_PLANKS).sound(SoundType.WOOD).mapColor(MapColor.WOOD)));

    public static final RegistrySupplier<Block> RUSTIC_BED = registerWithItem("rustic_bed", () -> new BedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_BED).sound(SoundType.WOOD).strength(0.2F).noOcclusion()));
    public static final RegistrySupplier<Block> RUSTIC_SOFA = registerWithItem("rustic_sofa", () -> new SofaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistrySupplier<Block> RUSTIC_DRESSER = registerWithItem("rustic_dresser", () -> new DresserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE));
    public static final RegistrySupplier<Block> RUSTIC_SINK = registerWithItem("rustic_sink", () -> new SinkBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS), false));
    public static final RegistrySupplier<Block> RUSTIC_WASHBASIN = registerWithItem("rustic_washbasin", () -> new SinkBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), true));
    public static final RegistrySupplier<Block> RUSTIC_CABINET = registerWithItem("rustic_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE, () -> false));
    public static final RegistrySupplier<Block> RUSTIC_COOKING_AISLE = registerWithItem("rustic_cooking_aisle", () -> new ConnectibleCabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE, () -> false));
    public static final RegistrySupplier<Block> RUSTIC_WALL_CABINET = registerWithItem("rustic_wall_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE, () -> true));
    public static final RegistrySupplier<Block> RUSTIC_SMOKER = registerWithItem("rustic_smoker", () -> new SmokeOvenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOKER)));
    public static final RegistrySupplier<Block> RUSTIC_WARDROBE = registerWithItem("rustic_wardrobe", () -> new WardrobeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistrySupplier<Block> RUSTIC_BATHTUB = registerWithItem("rustic_bathtub", () -> new BathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion().strength(1.5f, 3.0f)));
    public static final RegistrySupplier<Block> RUSTIC_PRIVY = registerWithItem("rustic_privy", () -> new PrivyBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final RegistrySupplier<Block> RUSTIC_TABLE = registerWithItem("rustic_table", () -> new TableBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> RUSTIC_CHAIR = registerWithItem("rustic_chair", () -> new ChairBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).pushReaction(PushReaction.IGNORE)));
    public static final RegistrySupplier<Block> RUSTIC_GLASS_PANE = registerWithItem("rustic_glass_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistrySupplier<Block> RUSTIC_GLASS_BLOCK = registerWithItem("rustic_glass_block", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));

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

    public static final RegistrySupplier<Block> FOUNDATION_BLOCK = registerWithItem("foundation_block", () -> new FoundationBlock(Blocks.STONE.defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_STAIRS)));


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

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(s -> {
        });
    }

    public static <T extends Block> RegistrySupplier<T> registerWithItem(String name, Supplier<T> block) {
        return GeneralUtil.registerWithItem(BLOCKS, BLOCK_REGISTRAR, ITEMS, ITEM_REGISTRAR, HearthAndTimber.identifier(name), block);
    }

    public static <T extends Block> RegistrySupplier<T> registerWithoutItem(String path, Supplier<T> block) {
        return GeneralUtil.registerWithoutItem(BLOCKS, BLOCK_REGISTRAR, HearthAndTimber.identifier(path), block);
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String path, Supplier<T> itemSupplier) {
        return GeneralUtil.registerItem(ITEMS, ITEM_REGISTRAR, HearthAndTimber.identifier(path), itemSupplier);
    }
}
