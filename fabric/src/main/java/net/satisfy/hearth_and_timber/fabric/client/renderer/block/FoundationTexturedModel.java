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
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.satisfy.hearth_and_timber.core.block.TimberBaseSkirtBlock;
import net.satisfy.hearth_and_timber.core.block.TimberBaseTrimBlock;
import net.satisfy.hearth_and_timber.core.block.TimberFoundationBlock;
import net.satisfy.hearth_and_timber.core.block.entity.TimberFoundationBlockEntity;
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

        if (!hasApplied(state)) {
            RandomSource r0 = random.get();
            for (Direction face : Direction.values()) {
                List<BakedQuad> qs = original.getQuads(state, face, r0);
                for (BakedQuad q : qs) {
                    QuadEmitter e = context.getEmitter();
                    e.fromVanilla(q, null, face);
                    e.emit();
                }
            }
            {
                List<BakedQuad> qs = original.getQuads(state, null, r0);
                for (BakedQuad q : qs) {
                    QuadEmitter e = context.getEmitter();
                    e.fromVanilla(q, null, null);
                    e.emit();
                }
            }
            EMITTING.set(false);
            return;
        }

        TextureAtlasSprite target = original.getParticleIcon();
        BlockEntity be = level.getBlockEntity(pos);
        if (be instanceof TimberFoundationBlockEntity fbe) {
            BlockState mimic = fbe.getMimicState();
            if (mimic != null && !mimic.isAir() && mimic.getRenderShape() == RenderShape.MODEL
                    && !(mimic.getBlock() instanceof TimberFoundationBlock)
                    && !(mimic.getBlock() instanceof TimberBaseSkirtBlock)
                    && !(mimic.getBlock() instanceof TimberBaseTrimBlock)) {
                BakedModel mm = Minecraft.getInstance().getBlockRenderer().getBlockModel(mimic);
                target = mm.getParticleIcon();
            }
        }

        RandomSource r = random.get();
        for (Direction face : Direction.values()) {
            List<BakedQuad> qs = original.getQuads(state, face, r);
            for (BakedQuad q : qs) {
                TextureAtlasSprite src = q.getSprite();
                QuadEmitter e = context.getEmitter();
                e.fromVanilla(q, null, face);
                float su0 = src.getU0(), su1 = src.getU1(), sv0 = src.getV0(), sv1 = src.getV1();
                float du = su1 - su0, dv = sv1 - sv0;
                for (int i = 0; i < 4; i++) {
                    float u = e.u(i), v = e.v(i);
                    float uN = du != 0f ? (u - su0) / du : 0f;
                    float vN = dv != 0f ? (v - sv0) / dv : 0f;
                    e.uv(i, uN, vN);
                }
                e.spriteBake(0, target, MutableQuadView.BAKE_NORMALIZED);
                e.emit();
            }
        }
        {
            List<BakedQuad> qs = original.getQuads(state, null, r);
            for (BakedQuad q : qs) {
                TextureAtlasSprite src = q.getSprite();
                QuadEmitter e = context.getEmitter();
                e.fromVanilla(q, null, null);
                float su0 = src.getU0(), su1 = src.getU1(), sv0 = src.getV0(), sv1 = src.getV1();
                float du = su1 - su0, dv = sv1 - sv0;
                for (int i = 0; i < 4; i++) {
                    float u = e.u(i), v = e.v(i);
                    float uN = du != 0f ? (u - su0) / du : 0f;
                    float vN = dv != 0f ? (v - sv0) / dv : 0f;
                    e.uv(i, uN, vN);
                }
                e.spriteBake(0, target, MutableQuadView.BAKE_NORMALIZED);
                e.emit();
            }
        }

        EMITTING.set(false);
    }

    private static boolean hasApplied(BlockState state) {
        for (var p : state.getProperties()) {
            if (p instanceof BooleanProperty bp && p.getName().equals("applied")) return state.getValue(bp);
        }
        return false;
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
