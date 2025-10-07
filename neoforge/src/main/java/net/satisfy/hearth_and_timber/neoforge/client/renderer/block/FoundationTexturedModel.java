package net.satisfy.hearth_and_timber.neoforge.client.renderer.block;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.extensions.IBakedModelExtension;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.satisfy.hearth_and_timber.core.block.FoundationBlock;
import net.satisfy.hearth_and_timber.core.block.entity.FoundationBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class FoundationTexturedModel implements BakedModel, IBakedModelExtension {
    public static final ModelProperty<BlockState> MIMIC = new ModelProperty<>();
    private final BakedModel original;

    public FoundationTexturedModel(BakedModel original) {
        this.original = original;
    }

    @Override
    public @NotNull ModelData getModelData(BlockAndTintGetter level, BlockPos pos, BlockState state, ModelData data) {
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FoundationBlockEntity f) {
            BlockState m = f.getMimicState();
            if (m != null && m.getRenderShape() == RenderShape.MODEL && !(m.getBlock() instanceof FoundationBlock))
                return ModelData.builder().with(MIMIC, m).build();
        }
        return data;
    }

    private static TextureAtlasSprite target(@Nullable BlockState mimic, TextureAtlasSprite fallback) {
        if (mimic != null) {
            BakedModel bm = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
            TextureAtlasSprite s = bm.getParticleIcon();
            if (s != null) return s;
        }
        return fallback;
    }

    private static BakedQuad remap(BakedQuad q, TextureAtlasSprite dst) {
        TextureAtlasSprite src = q.getSprite();
        if (src == null) return q;
        int[] v = q.getVertices().clone();
        float su0 = src.getU0(), su1 = src.getU1(), sv0 = src.getV0(), sv1 = src.getV1();
        float du = su1 - su0, dv = sv1 - sv0;
        for (int i = 0; i < 4; i++) {
            int off = i * 8;
            float uA = Float.intBitsToFloat(v[off + 4]);
            float vA = Float.intBitsToFloat(v[off + 5]);
            float uN = du != 0f ? (uA - su0) / du : 0f;
            float vN = dv != 0f ? (vA - sv0) / dv : 0f;
            float u = dst.getU(uN * 16f);
            float w = dst.getV(vN * 16f);
            v[off + 4] = Float.floatToRawIntBits(u);
            v[off + 5] = Float.floatToRawIntBits(w);
        }
        return new BakedQuad(v, q.getTintIndex(), q.getDirection(), dst, q.isShade());
    }

    private static List<BakedQuad> remapAll(List<BakedQuad> in, TextureAtlasSprite dst) {
        if (in.isEmpty()) return in;
        List<BakedQuad> out = new ArrayList<>(in.size());
        for (BakedQuad q : in) out.add(q.getSprite() == dst ? q : remap(q, dst));
        return out;
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand) {
        return original.getQuads(state, side, rand);
    }

    @Override
    public @NotNull List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, @NotNull RandomSource rand, @NotNull ModelData data, @Nullable RenderType renderType) {
        TextureAtlasSprite dst = target(data.get(MIMIC), original.getParticleIcon());
        return remapAll(original.getQuads(state, side, rand, data, renderType), dst);
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