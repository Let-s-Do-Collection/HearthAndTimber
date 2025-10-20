package net.satisfy.hearth_and_timber.neoforge.client.renderer.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.data.ModelData;
import net.neoforged.neoforge.client.model.data.ModelProperty;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import org.jetbrains.annotations.Nullable;
import oshi.util.tuples.Pair;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConnectingTimberModel implements BakedModel {

    public static final ModelProperty<Integer> CONNECTIONS = new ModelProperty<>();

    BakedModel original;

    public ConnectingTimberModel(BakedModel original) {
        this.original = original;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return List.of();
    }

    public static int getCardinals(ModelData data, Direction face) {

        Pair<Direction, Direction> axis = new Pair<>(TimberHelper.getPer1(face), TimberHelper.getPer2(face)); // LEFT, UP
        Function<Vec3i, Boolean> exists = (vec) -> TimberHelper.exists(vec, Objects.requireNonNullElse(data.get(CONNECTIONS), 0));

        Vec3i left = axis.getA().getNormal();
        Vec3i right = left.multiply(-1);
        Vec3i up = axis.getB().getNormal();
        Vec3i down = up.multiply(-1);

        AtomicInteger result = new AtomicInteger();
        // right, left, above, below, top right, top left, bottom right, bottom left
        List.of(
                exists.apply(right),
                exists.apply(left),
                exists.apply(up),
                exists.apply(down),
                exists.apply(right.offset(up)),
                exists.apply(left.offset(up)),
                exists.apply(right.offset(down)),
                exists.apply(left.offset(down))
        ).forEach(b -> result.updateAndGet(v -> (v << 1) | (b ? 1 : 0)));
        return result.get();
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand, ModelData data, @Nullable RenderType renderType) {
        List<BakedQuad> quads = original.getQuads(state, side, rand);
        return quads.stream().map((q) -> {
            int[] verts = q.getVertices();
            TextureAtlasSprite sprite = q.getSprite();
            int[] uv = TimberHelper.getFusionTexUV(getCardinals(data, q.getDirection()));
            verts[4] = Float.floatToRawIntBits(sprite.getU(uv[0] / 8f));
            verts[5] = Float.floatToRawIntBits(sprite.getV(uv[1] / 6f));
            verts[12] = Float.floatToRawIntBits(sprite.getU(uv[0] / 8f));
            verts[13] = Float.floatToRawIntBits(sprite.getV((uv[1] + 1) / 6f));
            verts[20] = Float.floatToRawIntBits(sprite.getU((uv[0] + 1) / 8f));
            verts[21] = Float.floatToRawIntBits(sprite.getV((uv[1] + 1) / 6f));
            verts[28] = Float.floatToRawIntBits(sprite.getU((uv[0] + 1) / 8f));
            verts[29] = Float.floatToRawIntBits(sprite.getV(uv[1] / 6f));
            return new BakedQuad(verts, q.getTintIndex(), q.getDirection(), q.getSprite(), q.isShade(), q.hasAmbientOcclusion());
        }).toList();
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
    public TextureAtlasSprite getParticleIcon() {
        return original.getParticleIcon();
    }

    @Override
    public ItemTransforms getTransforms() {
        return original.getTransforms();
    }

    @Override
    public ItemOverrides getOverrides() {
        return original.getOverrides();
    }

    /*@Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> rand, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder meshBuilder = renderer.meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        for(Direction direction : Direction.values()) {
            // ouchee, particle sprite as texture
            TextureAtlasSprite oSprite = original.getParticleIcon();
            int[] uv = TimberHelper.getFusionTexUV(blockView, state, pos, direction);

            emitter.square(direction, 0, 0, 1f, 1f, 0f);
            emitter.spriteBake(oSprite, MutableQuadView.BAKE_LOCK_UV);
            emitter.uv(0, oSprite.getU(uv[0] / 8f), oSprite.getV(uv[1] / 6f));
            emitter.uv(1, oSprite.getU(uv[0] / 8f), oSprite.getV((uv[1] + 1) / 6f));
            emitter.uv(2, oSprite.getU((uv[0] + 1) / 8f), oSprite.getV((uv[1] + 1) / 6f));
            emitter.uv(3, oSprite.getU((uv[0] + 1) / 8f), oSprite.getV(uv[1] / 6f));
            emitter.color(-1, -1, -1, -1);
            emitter.emit();
        }

        meshBuilder.build().outputTo(context.getEmitter());

    }*/


}
