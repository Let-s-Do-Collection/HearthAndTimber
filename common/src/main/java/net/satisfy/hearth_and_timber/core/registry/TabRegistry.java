package net.satisfy.hearth_and_timber.core.registry;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.satisfy.hearth_and_timber.HearthAndTimber;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(HearthAndTimber.MOD_ID, Registries.CREATIVE_MODE_TAB);

    @SuppressWarnings("unused")
    public static final RegistrySupplier<CreativeModeTab> HEARTH_AND_TIMBER_TAB = CREATIVE_MODE_TABS.register("hearth_and_timber", () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
            .icon(() -> new ItemStack(Blocks.GRANITE))
            .title(Component.translatable("creativetab.hearth_and_timber.tab"))
            .displayItems((parameters, output) -> {

            })
            .build());

    public static void init() {
        CREATIVE_MODE_TABS.register();
    }
}
