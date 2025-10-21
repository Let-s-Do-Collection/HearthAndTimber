package net.satisfy.hearth_and_timber.client;

import dev.architectury.registry.client.level.entity.EntityModelLayerRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.hearth_and_timber.client.renderer.block.SlidingDoorRenderer;
import net.satisfy.hearth_and_timber.client.renderer.block.TimberFrameRenderer;
import net.satisfy.hearth_and_timber.client.renderer.block.WindowCasingRenderer;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingBarnDoorModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingHayloftDoorModel;
import net.satisfy.hearth_and_timber.client.renderer.model.SlidingStableDoorModel;
import net.satisfy.hearth_and_timber.core.registry.EntityTypeRegistry;

import static net.satisfy.hearth_and_timber.core.registry.ObjectRegistry.*;

@Environment(EnvType.CLIENT)
public class HearthAndTimberClient {

    public static void onInitializeClient() {
        RenderTypeRegistry.register(RenderType.cutout(), FRAMEWORK.get(), TIMBER_FRAME.get(), TIMBER_GRID_FRAME.get(), TIMBER_CROSS_FRAME.get(), TIMBER_DIAGONAL_FRAME.get(), TIMBER_FRAME_STAIRS.get(), SPRUCE_WINDOW_CASING.get(), TIMBER_FOUNDATION.get(), TIMBER_BASE_TRIM.get(), TIMBER_BASE_SKIRT.get(), OAK_WINDOW_PANE.get(), SPRUCE_WINDOW_PANE.get(), BIRCH_WINDOW_PANE.get(), JUNGLE_WINDOW_PANE.get(), ACACIA_WINDOW_PANE.get(), DARK_OAK_WINDOW_PANE.get(), MANGROVE_WINDOW_PANE.get(), CHERRY_WINDOW_PANE.get(), PALE_OAK_WINDOW_PANE.get(), OAK_WINDOW.get(), SPRUCE_WINDOW.get(), BIRCH_WINDOW.get(), JUNGLE_WINDOW.get(), ACACIA_WINDOW.get(), DARK_OAK_WINDOW.get(), MANGROVE_WINDOW.get(), CHERRY_WINDOW.get(), PALE_OAK_WINDOW.get());

        registerBlockEntityRenderer();
    }

    public static void preInitClient() {
        registerEntityModelLayer();
    }

    public static void registerEntityModelLayer() {
        EntityModelLayerRegistry.register(SlidingHayloftDoorModel.LAYER_LOCATION, SlidingHayloftDoorModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SlidingBarnDoorModel.LAYER_LOCATION, SlidingBarnDoorModel::getTexturedModelData);
        EntityModelLayerRegistry.register(SlidingStableDoorModel.LAYER_LOCATION, SlidingStableDoorModel::getTexturedModelData);
    }

    public static void registerBlockEntityRenderer() {
        BlockEntityRendererRegistry.register(EntityTypeRegistry.SLIDING_DOOR_BLOCK_ENTITY.get(), SlidingDoorRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.TIMBER_FRAME_BLOCK_ENTITY.get(), TimberFrameRenderer::new);
        BlockEntityRendererRegistry.register(EntityTypeRegistry.WINDOW_CASING_BLOCK_ENTITY.get(), WindowCasingRenderer::new);
    }
}
