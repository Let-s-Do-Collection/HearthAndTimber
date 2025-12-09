package net.satisfy.hearth_and_timber.fabric.client.renderer.block;

import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.model.FabricBakedModel;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.core.block.TimberBaseSkirtBlock;
import net.satisfy.hearth_and_timber.core.block.TimberBaseTrimBlock;
import net.satisfy.hearth_and_timber.core.block.TimberFoundationBlock;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFrameBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation, removal")
public class FoundationTexturedModel implements BakedModel, FabricBakedModel {
    private static final ThreadLocal<Boolean> EMITTING = ThreadLocal.withInitial(() -> false);
    private final BakedModel original;
    private final boolean onlyPlaceholder;

    public FoundationTexturedModel(BakedModel original, boolean onlyPlaceholder) {
        this.original = original;
        this.onlyPlaceholder = onlyPlaceholder;
    }

    private static boolean isMissing(TextureAtlasSprite sprite) {
        if (sprite == null) return true;
        sprite.contents();
        return "missingno".equals(sprite.contents().name().getPath());
    }

    private static TextureAtlasSprite targetSprite(@Nullable BlockState mimic, TextureAtlasSprite fallback) {
        if (mimic == null || mimic.isAir()) return fallback;
        BakedModel model = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
        TextureAtlasSprite sprite = model.getParticleIcon();
        return isMissing(sprite) ? fallback : sprite;
    }

    private static BakedQuad remapQuad(BakedQuad quad, TextureAtlasSprite dst) {
        TextureAtlasSprite src = quad.getSprite();
        if (src == dst) return quad;

        int[] verts = quad.getVertices().clone();

        float wScale = dst.contents().width() / (float) src.contents().width();
        float hScale = dst.contents().height() / (float) src.contents().height();

        int stride = 8;
        int uvBase = 4;

        for (int i = 0; i < 4; i++) {
            int off = i * stride + uvBase;
            float uOrig = Float.intBitsToFloat(verts[off]);
            float vOrig = Float.intBitsToFloat(verts[off + 1]);

            float uRemap = (uOrig - src.getU0()) * wScale + dst.getU0();
            float vRemap = (vOrig - src.getV0()) * hScale + dst.getV0();

            verts[off] = Float.floatToRawIntBits(uRemap);
            verts[off + 1] = Float.floatToRawIntBits(vRemap);
        }

        return new BakedQuad(verts, quad.getTintIndex(), quad.getDirection(), dst, quad.isShade());
    }

    private static List<BakedQuad> remapAll(List<BakedQuad> in, TextureAtlasSprite dst, boolean onlyPlaceholder) {
        if (in.isEmpty()) return in;
        List<BakedQuad> out = new ArrayList<>(in.size());
        for (BakedQuad quad : in) {
            TextureAtlasSprite src = quad.getSprite();
            src.contents();
            boolean isPlaceholder = src.contents().name().getPath().contains("placeholder");
            if (!onlyPlaceholder || isPlaceholder) {
                out.add(remapQuad(quad, dst));
            } else {
                out.add(remapQuad(quad, dst));
                out.add(quad);
            }
        }
        return out;
    }

    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> random, RenderContext context) {
        if (EMITTING.get()) return;
        EMITTING.set(true);

        RenderMaterial material = context.getEmitter().material();
        RandomSource rand = random.get();

        TextureAtlasSprite fallbackSprite = original.getParticleIcon();
        BlockState mimicState = null;
        var be = level.getBlockEntity(pos);
        if (be instanceof TimberFrameBlockEntity frameBlockEntity) {
            BlockState mimic = frameBlockEntity.getMimicState();
            if (mimic != null && !mimic.isAir() && mimic.getRenderShape() == RenderShape.MODEL
                    && !(mimic.getBlock() instanceof TimberFoundationBlock)
                    && !(mimic.getBlock() instanceof TimberBaseSkirtBlock)
                    && !(mimic.getBlock() instanceof TimberBaseTrimBlock)) {
                mimicState = mimic;
            }
        }
        TextureAtlasSprite dst = targetSprite(mimicState, fallbackSprite);
        boolean hasMimic = !isMissing(dst) && mimicState != null;

        for (Direction face : Direction.values()) {
            List<BakedQuad> base = original.getQuads(state, face, rand);
            List<BakedQuad> quads = hasMimic ? remapAll(base, dst, onlyPlaceholder) : base;
            for (BakedQuad quad : quads) {
                var emitter = context.getEmitter();
                emitter.fromVanilla(quad, material, face);
                emitter.emit();
            }
        }

        List<BakedQuad> baseAll = original.getQuads(state, null, rand);
        List<BakedQuad> quadsAll = hasMimic ? remapAll(baseAll, dst, onlyPlaceholder) : baseAll;
        for (BakedQuad quad : quadsAll) {
            var emitter = context.getEmitter();
            emitter.fromVanilla(quad, material, null);
            emitter.emit();
        }

        EMITTING.set(false);
    }

    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> random, RenderContext context) {
        if (original instanceof FabricBakedModel fabricBakedModel) {
            fabricBakedModel.emitItemQuads(stack, random, context);
        } else {
            context.fallbackConsumer().accept(null);
        }
    }

    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, @NotNull RandomSource random) {
        return original.getQuads(state, side, random);
    }

    public boolean useAmbientOcclusion() {
        return original.useAmbientOcclusion();
    }

    public boolean isGui3d() {
        return original.isGui3d();
    }

    public boolean usesBlockLight() {
        return original.usesBlockLight();
    }

    public boolean isCustomRenderer() {
        return true;
    }

    @Override
    public @NotNull TextureAtlasSprite getParticleIcon() {
        return original.getParticleIcon();
    }

    public @NotNull ItemOverrides getOverrides() {
        return original.getOverrides();
    }

    public @NotNull ItemTransforms getTransforms() {
        return original.getTransforms();
    }
}