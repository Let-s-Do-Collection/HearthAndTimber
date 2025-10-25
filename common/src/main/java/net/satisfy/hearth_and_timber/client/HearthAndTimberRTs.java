package net.satisfy.hearth_and_timber.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderType;

import static net.minecraft.client.renderer.RenderType.*;

public class HearthAndTimberRTs {
    public static RenderType FOUNDATION_LAYER = RenderType.create("foundation_layer", DefaultVertexFormat.BLOCK, VertexFormat.Mode.QUADS, 4194304, true, false, RenderType.CompositeState.builder().setLightmapState(LIGHTMAP).setShaderState(RENDERTYPE_CUTOUT_MIPPED_SHADER).setTextureState(BLOCK_SHEET_MIPPED).setLayeringState(POLYGON_OFFSET_LAYERING).createCompositeState(true));
}