package net.satisfy.hearth_and_timber.fabric.client.renderer.block;

import net.fabricmc.fabric.api.renderer.v1.Renderer;
import net.fabricmc.fabric.api.renderer.v1.RendererAccess;
import net.fabricmc.fabric.api.renderer.v1.material.RenderMaterial;
import net.fabricmc.fabric.api.renderer.v1.mesh.Mesh;
import net.fabricmc.fabric.api.renderer.v1.mesh.MeshBuilder;
import net.fabricmc.fabric.api.renderer.v1.mesh.MutableQuadView;
import net.fabricmc.fabric.api.renderer.v1.mesh.QuadEmitter;
import net.fabricmc.fabric.api.renderer.v1.render.RenderContext;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.hearth_and_timber.HearthAndTimber;
import net.satisfy.hearth_and_timber.client.TimberHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class ConnectingTimberModel implements BakedModel {

    BakedModel original;

    public ConnectingTimberModel(BakedModel original) {
        this.original = original;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState blockState, @Nullable Direction direction, RandomSource randomSource) {
        return original.getQuads(blockState, direction, randomSource);
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

    @Override
    public void emitBlockQuads(BlockAndTintGetter blockView, BlockState state, BlockPos pos, Supplier<RandomSource> rand, RenderContext context) {
        Renderer renderer = RendererAccess.INSTANCE.getRenderer();
        MeshBuilder meshBuilder = renderer.meshBuilder();
        QuadEmitter emitter = meshBuilder.getEmitter();

        for(Direction direction : Direction.values()) {
            if (TimberHelper.isConnecting(blockView.getBlockState(pos.relative(direction)))) continue;

            // Yeehaw. Sweet, sweet unchecked casts, with
            // an access widener so we can access protected fields :O
            TextureAtlasSprite oSprite = ((SimpleBakedModel)original).unculledFaces.get(0).getSprite();
            int[] uv = TimberHelper.getFusionTexUV(TimberHelper.getCardinals(blockView, state, pos, direction));

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

    }


}
