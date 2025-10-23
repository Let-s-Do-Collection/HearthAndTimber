package net.satisfy.hearth_and_timber.neoforge.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.IQuadTransformer;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class FoundationTexturedModel implements BakedModel {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    private final BakedModel original;
    private final boolean onlyPlaceholder;

    public FoundationTexturedModel(BakedModel original, boolean onlyPlaceholder) {
        this.original = original;
        this.onlyPlaceholder = onlyPlaceholder;
    }

    private static boolean isMissing(TextureAtlasSprite s) {
        if (s == null) return true;
        s.contents();
        return "missingno".equals(s.contents().name().getPath());
    }

    private static TextureAtlasSprite targetSprite(@Nullable BlockState mimic, TextureAtlasSprite fallback) {
        if (mimic == null || mimic.isAir()) return fallback;
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
        TextureAtlasSprite s = model.getParticleIcon();
        return isMissing(s) ? fallback : s;
    }

    private static BakedQuad remapQuad(BakedQuad quad, TextureAtlasSprite dst) {
        TextureAtlasSprite src = quad.getSprite();
        if (src == dst) return quad;

        int[] verts = quad.getVertices().clone();
        int stride = IQuadTransformer.STRIDE;
        int uvBase = IQuadTransformer.UV0;

        float wScale = dst.contents().width() / (float) src.contents().width();
        float hScale = dst.contents().height() / (float) src.contents().height();

        for (int i = 0; i < 4; i++) {
            int off = i * stride + uvBase;
            float uOrig = Float.intBitsToFloat(verts[off]);
            float vOrig = Float.intBitsToFloat(verts[off + 1]);

            float uRemap = (uOrig - src.getU0()) * wScale + dst.getU0();
            float vRemap = (vOrig - src.getV0()) * hScale + dst.getV0();

            verts[off] = Float.floatToRawIntBits(uRemap);
            verts[off + 1] = Float.floatToRawIntBits(vRemap);
        }

        return new BakedQuad(verts, quad.getTintIndex(), quad.getDirection(), dst, quad.isShade(), quad.hasAmbientOcclusion()
        );
    }

    private static List<BakedQuad> remapAll(List<BakedQuad> in, TextureAtlasSprite dst, boolean onlyPlaceholder) {
        if (in.isEmpty()) return in;
        List<BakedQuad> out = new ArrayList<>(in.size());
        for (BakedQuad quad : in) {
            quad.getSprite();
            quad.getSprite().contents();
            boolean isPlaceholder = quad.getSprite().contents().name().getPath().contains("placeholder");
            if (!onlyPlaceholder || isPlaceholder) {
                out.add(remapQuad(quad, dst));
            } else {
                out.add(quad);
            }
        }
        return out;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return original.getQuads(state, side, rand);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType layer) {
        List<BakedQuad> base = original.getQuads(state, side, rand, data, layer);
        if (base.isEmpty()) return base;
        if (!data.has(MIMIC)) return base;
        BlockState mimic = data.get(MIMIC);
        if (mimic == null || mimic.isAir()) return base;
        TextureAtlasSprite dst = targetSprite(mimic, original.getParticleIcon());
        if (isMissing(dst)) return base;
        return remapAll(base, dst, onlyPlaceholder);
    }

    @Override
    public boolean useAmbientOcclusion() {
        return original.useAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return original.isGui3d();
    }

    @Override
    public boolean usesBlockLight() {
        return original.usesBlockLight();
    }

    @Override
    public boolean isCustomRenderer() {
        return original.isCustomRenderer();
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return original.getParticleIcon();
    }

    @Override
    public @NotNull ItemOverrides getOverrides() {
        return original.getOverrides();
    }

    @Override
    public @NotNull ItemTransforms getTransforms() {
        return original.getTransforms();
    }
}