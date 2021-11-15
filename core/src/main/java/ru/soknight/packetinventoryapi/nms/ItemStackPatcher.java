package ru.soknight.packetinventoryapi.nms;

import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ImplementedAs("SimpleItemStackPatcher")
public interface ItemStackPatcher {

    void setDisplayName(@NotNull ItemMeta itemMeta, @Nullable TextComponent component);

    void setDisplayName(@NotNull ItemMeta itemMeta, @Nullable String rawJson);

}
