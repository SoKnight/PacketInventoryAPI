package ru.soknight.packetinventoryapi.menu.item;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;

@Getter
public final class WrappedItemStack extends ItemStack {

    private final VanillaItem<?, ?> vanillaItem;

    public WrappedItemStack(@NotNull Material type, @NotNull VanillaItem<?, ?> vanillaItem) {
        super(type);
        this.vanillaItem = vanillaItem;
    }

    public WrappedItemStack(@NotNull ItemStack itemStack, @NotNull VanillaItem<?, ?> vanillaItem) {
        super(itemStack);
        this.vanillaItem = vanillaItem;
    }

    @Override
    @SuppressWarnings("MethodDoesntCallSuperMethod")
    public @NotNull WrappedItemStack clone() {
        return new WrappedItemStack(this, vanillaItem);
    }

}
