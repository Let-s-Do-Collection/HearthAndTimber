package net.satisfy.hearth_and_timber.fabric.core;

import net.fabricmc.fabric.api.biome.v1.BiomeModification;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.satisfy.hearth_and_timber.HearthAndTimber;

import java.util.function.Predicate;

public final class HearthAndTimberFabricWorldgen {

    private static final Predicate<BiomeSelectionContext> IS_OVERWORLD = BiomeSelectors.tag(BiomeTags.IS_OVERWORLD);

    public static final ResourceKey<PlacedFeature> ORE_GROUTLESS_RUBBLESTONE = registerPlacedFeature("ore_groutless_rubblestone");

    public static void init() {
        registerFeatureAdditions();
    }

    public static void registerFeatureAdditions() {
        BiomeModification world = BiomeModifications.create(ResourceLocation.fromNamespaceAndPath(HearthAndTimber.MOD_ID, "world_features"));
        world.add(ModificationPhase.ADDITIONS, IS_OVERWORLD, context -> context.getGenerationSettings().addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, ORE_GROUTLESS_RUBBLESTONE));
    }

    public static ResourceKey<PlacedFeature> registerPlacedFeature(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, HearthAndTimber.identifier(name));
    }
}