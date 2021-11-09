package ru.soknight.packetinventoryapi.integration.itemsadder;

import dev.lone.itemsadder.api.CustomStack;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class ItemsAdderService {

    public static @Nullable CustomStack getAsCustomStack(@NotNull NamespacedKey namespacedKey) {
        return getAsCustomStack(asString(namespacedKey));
    }

    public static @Nullable CustomStack getAsCustomStack(@NotNull String namespacedKey) {
        return CustomStack.getInstance(namespacedKey);
    }

    public static @Nullable ItemStack getAsBukkitItem(@NotNull NamespacedKey namespacedKey) {
        return getAsBukkitItem(asString(namespacedKey));
    }

    public static @Nullable ItemStack getAsBukkitItem(@NotNull String namespacedKey) {
        CustomStack customStack = getAsCustomStack(namespacedKey);
        return customStack != null ? customStack.getItemStack() : null;
    }

    private static @NotNull String asString(@NotNull NamespacedKey namespacedKey) {
        return namespacedKey.getNamespace() + ":" + namespacedKey.getKey();
    }

}
