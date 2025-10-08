package net.satisfy.hearth_and_timber.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.hearth_and_timber.client.renderer.block.BathtubRenderer;
import net.satisfy.hearth_and_timber.client.renderer.block.SlidingDoorRenderer;
import net.satisfy.hearth_and_timber.client.renderer.block.TimberFrameRenderer;
import net.satisfy.hearth_and_timber.client.renderer.block.WardrobeRenderer;
import net.satisfy.hearth_and_timber.client.renderer.entity.ChairRenderer;
import net.satisfy.hearth_and_timber.client.renderer.model.BathtubModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingBarnDoorModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingHayloftDoorModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingStableDoorModel;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class HearthAndTimberClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), FRAMEWORK.get(), RUSTIC_WARDROBE.get(), RUSTIC_SINK.get(), RUSTIC_WASHBASIN.get(), RUSTIC_BATHTUB.get(), TIMBER_FRAME.get(), TIMBER_GRID_FRAME.get(), TIMBER_CROSS_FRAME.get(), TIMBER_DIAGONAL_FRAME.get(), RUSTIC_TABLE.get(), RUSTIC_CHAIR.get(), TIMBER_FRAME_STAIRS.get(), RUSTIC_SOFA.get());
        RenderTypeRegistry.register(RenderType.translucent(), RUSTIC_GLASS_PANE.get(), RUSTIC_GLASS_BLOCK.get());

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex) -> {
            if (world == null || pos == null) {
                return -1;
            }
            return BiomeColors.getAverageWaterColor(world, pos);
        }, RUSTIC_SINK.get(), RUSTIC_WASHBASIN.get());

        registerBlockEntityRenderer();
    }

    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(EntityTypeRegistry.CHAIR_ENTITY, ChairRenderer::new);
    }


    public static void preInitClient() {
        registerEntityRenderers();
        registerEntityModelLayer();
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(BathtubModel.LAYER_LOCATION, BathtubModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SlidingHayloftDoorModel.LAYER_LOCATION, SlidingHayloftDoorModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SlidingBarnDoorModel.LAYER_LOCATION, SlidingBarnDoorModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SlidingStableDoorModel.LAYER_LOCATION, SlidingStableDoorModel::getTexturedModelData);
    }

    public static void registerBlockEntityRenderer() {
        BlockEntityRendererRegistry.register(EntityTypeRegistry.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.BATHTUB_BLOCK_ENTITY.get(), BathtubRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WARDROBE_BLOCK_ENTITY.get(), WardrobeRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get(), TimberFrameRenderer::new);
    }
}
