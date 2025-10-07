package net.satisfy.hearth_and_timber.fabric.client.renderer.block;

import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.hearth_and_timber.core.block.FoundationBlock;
import net.satisfy.hearth_and_timber.core.block.entity.FoundationBlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("deprecation, removal")
public class FoundationTexturedModel implements BakedModel, FabricBakedModel {
    private static final ThreadLocal<Boolean> EMITTING = ThreadLocal.withInitial(() -> false);
    private final BakedModel original;

    public FoundationTexturedModel(BakedModel original) {
        this.original = original;
    }

    public boolean isVanillaAdapter() {
        return false;
    }

    @Override
    public void emitBlockQuads(BlockAndTintGetter level, BlockState state, BlockPos pos, Supplier<RandomSource> random, RenderContext context) {
        if (EMITTING.get()) return;
        EMITTING.set(true);

        TextureAtlasSprite target = null;
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof FoundationBlockEntity fbe) {
            BlockState mimic = fbe.getMimicState();
            if (mimic != null && !(mimic.getBlock() instanceof FoundationBlock)) {
                BakedModel mm = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
                target = mm.getParticleIcon();
            }
        }
        if (target == null) target = original.getParticleIcon();

        RandomSource r = random.get();

        for (Direction face : Direction.values()) {
            List<BakedQuad> quads = original.getQuads(state, face, r);
            for (BakedQuad q : quads) {
                TextureAtlasSprite src = q.getSprite();
                QuadEmitter e = context.getEmitter();
                e.fromVanilla(q, null, face);

                float su0 = src.getU0();
                float su1 = src.getU1();
                float sv0 = src.getV0();
                float sv1 = src.getV1();
                float du = su1 - su0;
                float dv = sv1 - sv0;

                for (int i = 0; i < 4; i++) {
                    float uAtlas = e.u(i);
                    float vAtlas = e.v(i);
                    float uNorm = du != 0f ? (uAtlas - su0) / du : 0f;
                    float vNorm = dv != 0f ? (vAtlas - sv0) / dv : 0f;
                    e.uv(i, uNorm, vNorm);
                }

                e.spriteBake(0, target, MutableQuadView.BAKE_NORMALIZED);
                e.emit();
            }
        }

        {
            List<BakedQuad> quads = original.getQuads(state, null, r);
            for (BakedQuad q : quads) {
                TextureAtlasSprite src = q.getSprite();
                QuadEmitter e = context.getEmitter();
                e.fromVanilla(q, null, null);

                float su0 = src.getU0(), su1 = src.getU1();
                float sv0 = src.getV0(), sv1 = src.getV1();
                float du = su1 - su0, dv = sv1 - sv0;

                for (int i = 0; i < 4; i++) {
                    float uAtlas = e.u(i), vAtlas = e.v(i);
                    float uNorm = du != 0f ? (uAtlas - su0) / du : 0f;
                    float vNorm = dv != 0f ? (vAtlas - sv0) / dv : 0f;
                    e.uv(i, uNorm, vNorm);
                }

                e.spriteBake(0, target, MutableQuadView.BAKE_NORMALIZED);
                e.emit();
            }
        }

        EMITTING.set(false);
    }

    public void emitItemQuads(ItemStack stack, Supplier<RandomSource> random, RenderContext context) {
        if (original instanceof FabricBakedModel fbm) {
            fbm.emitItemQuads(stack, random, context);
        } else {
            context.fallbackConsumer().accept(null);
        }
    }

    public @NotNull List<BakedQuad> getQuads(BlockState state, Direction side, RandomSource random) {
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
        return original.isCustomRenderer();
    }

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
