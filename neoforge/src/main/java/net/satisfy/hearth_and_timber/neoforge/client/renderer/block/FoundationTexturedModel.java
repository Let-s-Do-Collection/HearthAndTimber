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
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiPredicate;

@SuppressWarnings("deprecation")
public class FoundationTexturedModel implements BakedModel {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    private final BakedModel original;
    private final BiPredicate<BakedQuad, TextureAtlasSprite> filter;

    public FoundationTexturedModel(BakedModel original, BiPredicate<BakedQuad, TextureAtlasSprite> filter) {
        this.original = original;
        this.filter = filter;
    }

    private static TextureAtlasSprite targetSprite(@Nullable BlockState mimic, TextureAtlasSprite fallback) {
        if (mimic == null || mimic.isAir()) return fallback;
        BakedModel m = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
        return m.getParticleIcon();
    }

    private static BakedQuad remapQuad(BakedQuad q, TextureAtlasSprite dst) {
        TextureAtlasSprite src = q.getSprite();
        int[] verts = q.getVertices().clone();
        float su0 = src.getU0(), su1 = src.getU1();
        float sv0 = src.getV0(), sv1 = src.getV1();
        float du = su1 - su0, dv = sv1 - sv0;
        float duDst = dst.getU1() - dst.getU0();
        float dvDst = dst.getV1() - dst.getV0();
        for (int i = 0; i < 4; i++) {
            int off = i * 8;
            float uA = Float.intBitsToFloat(verts[off + 4]);
            float vA = Float.intBitsToFloat(verts[off + 5]);
            float uNorm = du != 0f ? (uA - su0) / du : 0f;
            float vNorm = dv != 0f ? (vA - sv0) / dv : 0f;
            float u = dst.getU0() + uNorm * duDst;
            float v = dst.getV0() + vNorm * dvDst;
            verts[off + 4] = Float.floatToRawIntBits(u);
            verts[off + 5] = Float.floatToRawIntBits(v);
        }
        return new BakedQuad(verts, q.getTintIndex(), q.getDirection(), dst, q.isShade());
    }

    private static List<BakedQuad> remapAll(List<BakedQuad> in, TextureAtlasSprite dst, BiPredicate<BakedQuad, TextureAtlasSprite> filter) {
        if (in.isEmpty()) return in;
        List<BakedQuad> out = new ArrayList<>(in.size());
        for (BakedQuad q : in) out.add(filter.test(q, dst) ? remapQuad(q, dst) : q);
        return out;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return original.getQuads(state, side, rand);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType layer) {
        if (state == null || state.isAir()) {
            return original.getQuads(state, side, rand, data, layer);
        }
        List<BakedQuad> base = original.getQuads(state, side, rand, data, layer);
        if (!data.has(MIMIC)) return base;
        BlockState mimic = data.get(MIMIC);
        if (mimic == null || mimic.isAir()) return base;
        TextureAtlasSprite dst = targetSprite(mimic, original.getParticleIcon());
        return remapAll(base, dst, filter);
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
