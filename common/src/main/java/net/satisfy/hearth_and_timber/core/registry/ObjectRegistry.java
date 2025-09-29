package net.satisfy.hearth_and_timber.core.registry;

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

    public static final RegistrySupplier<Block> CATTLEGRID = registerWithItem("cattlegrid", () -> new net.satisfy.hearth_and_timber.core.block.CattlegridBlock(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(2.0f, 6.0f).noOcclusion()));
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

    public static final RegistrySupplier<Block> FIELDSTONE_BLOCK = registerWithItem("fieldstone_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE)));
    public static final RegistrySupplier<Block> FIELDSTONE_BLOCK_STAIRS = registerWithItem("fieldstone_stairs", () -> new StairBlock(FIELDSTONE_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> FIELDSTONE_BLOCK_SLAB = registerWithItem("fieldstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BLOCK = registerWithItem("mossy_fieldstone_block", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE)));
    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BLOCK_STAIRS = registerWithItem("mossy_fieldstone_stairs", () -> new StairBlock(MOSSY_FIELDSTONE_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_FIELDSTONE_BLOCK_SLAB = registerWithItem("mossy_fieldstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> SPLITSTONE_PATH_BLOCK = registerWithItem("splitstone_path_block", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> SPLITSTONE_BLOCK = registerWithItem("splitstone_block", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), SPLITSTONE_PATH_BLOCK));
    public static final RegistrySupplier<Block> SPLITSTONE_BLOCK_STAIRS = registerWithItem("splitstone_stairs", () -> new StairBlock(SPLITSTONE_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> SPLITSTONE_BLOCK_SLAB = registerWithItem("splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_PATH_BLOCK = registerWithItem("mossy_splitstone_path_block", () -> new SplitstonePathBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(2.5f, 6.0f).mapColor(MapColor.STONE).speedFactor(1.0625f)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_BLOCK = registerWithItem("mossy_splitstone_block", () -> new SplitstoneBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE).sound(SoundType.DEEPSLATE).strength(3.0f, 6.0f).mapColor(MapColor.STONE), MOSSY_SPLITSTONE_PATH_BLOCK));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_BLOCK_STAIRS = registerWithItem("mossy_splitstone_stairs", () -> new StairBlock(MOSSY_SPLITSTONE_BLOCK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_STAIRS)));
    public static final RegistrySupplier<Block> MOSSY_SPLITSTONE_BLOCK_SLAB = registerWithItem("mossy_splitstone_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLED_DEEPSLATE_SLAB)));

    public static final RegistrySupplier<Block> PACKED_DIRT = registerWithItem("packed_dirt", () -> new PackedDirtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(1.5f, 3.0f).sound(SoundType.PACKED_MUD)));
    public static final RegistrySupplier<Block> TRAMPLED_PACKED_DIRT = registerWithItem("trampled_packed_dirt", () -> new TrampledPackedDirtBlock(BlockBehaviour.Properties.of().mapColor(MapColor.DIRT).strength(1.0f, 3.0f).sound(SoundType.PACKED_MUD)));

    public static final RegistrySupplier<Block> STABLE_FLOOR = registerWithItem("stable_floor", () -> new StableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).strength(1.5f, 3.0f)));
    public static final RegistrySupplier<Block> TRAMPLED_STABLE_FLOOR = registerWithItem("trampled_stable_floor", () -> new TrampledStableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_BROWN).sound(SoundType.WOOD).strength(1.2f, 2.5f)));
    public static final RegistrySupplier<Block> STRAW_STABLE_FLOOR = registerWithItem("straw_stable_floor", () -> new StrawStableFloorBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.GRASS).strength(0.8f, 1.0f)));

    public static final RegistrySupplier<Block> RUSTIC_BED = registerWithItem("rustic_bed", () -> new BedBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GRAY_BED).sound(SoundType.WOOD).strength(0.2F).noOcclusion()));
    public static final RegistrySupplier<Block> RUSTIC_SOFA = registerWithItem("rustic_sofa", () -> new SofaBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).strength(2.0f, 3.0f).mapColor(MapColor.COLOR_LIGHT_GRAY)));
    public static final RegistrySupplier<Block> RUSTIC_DRESSER = registerWithItem("rustic_dresser", () -> new DresserBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE));
    public static final RegistrySupplier<Block> RUSTIC_SINK = registerWithItem("rustic_sink", () -> new SinkBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS)));
    public static final RegistrySupplier<Block> RUSTIC_CABINET = registerWithItem("rustic_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE, () -> false));
    public static final RegistrySupplier<Block> RUSTIC_WALL_CABINET = registerWithItem("rustic_wall_cabinet", () -> new CabinetBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS), () -> SoundEvents.WOODEN_TRAPDOOR_OPEN, () -> SoundEvents.WOODEN_TRAPDOOR_CLOSE, () -> true));
    public static final RegistrySupplier<Block> RUSTIC_SMOKER = registerWithItem("rustic_smoker", () -> new SmokeOvenBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SMOKER)));
    public static final RegistrySupplier<Block> RUSTIC_WARDROBE = registerWithItem("rustic_wardrobe", () -> new WardrobeBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).sound(SoundType.WOOD).mapColor(MapColor.TERRACOTTA_WHITE)));
    public static final RegistrySupplier<Block> RUSTIC_BATHTUB = registerWithItem("rustic_bathtub", () -> new BathtubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BRICKS).noOcclusion().strength(1.5f, 3.0f)));

    public static final RegistrySupplier<Block> RUSTIC_GLASS_PANE = registerWithItem("rustic_glass_pane", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS).strength(0.3f).sound(SoundType.GLASS).noOcclusion()));
    public static final RegistrySupplier<Block> RUSTIC_GLASS_BLOCK = registerWithItem("rustic_glass_block", () -> new WindowBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.GLASS)));


    static {
        ((PackedDirtBlock) PACKED_DIRT.get()).setTrampled(TRAMPLED_PACKED_DIRT);
        ((TrampledPackedDirtBlock) TRAMPLED_PACKED_DIRT.get()).setBase(PACKED_DIRT);
        ((StableFloorBlock) STABLE_FLOOR.get()).setTrampled(TRAMPLED_STABLE_FLOOR);
        ((StableFloorBlock) STABLE_FLOOR.get()).setStraw(STRAW_STABLE_FLOOR);
        ((StrawStableFloorBlock) STRAW_STABLE_FLOOR.get()).setBase(STABLE_FLOOR);
        ((StrawStableFloorBlock) STRAW_STABLE_FLOOR.get()).setStrawItem(() -> Items.WHEAT);
    }




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

    private static Item.Properties getSettings(Consumer<Item.Properties> consumer) {
        Item.Properties settings = new Item.Properties();
        consumer.accept(settings);
        return settings;
    }

    static Item.Properties getSettings() {
        return getSettings(s -> {});
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
