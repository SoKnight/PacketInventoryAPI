package ru.soknight.packetinventoryapi.placeholder.context;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.menu.item.WrappedItemStack;
import ru.soknight.packetinventoryapi.placeholder.LitePlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPlaceholderContext implements PlaceholderContext {

    protected final Player viewer;
    protected final List<PlaceholderReplacer> placeholderReplacers;

    public AbstractPlaceholderContext(@Nullable Player viewer) {
        this(viewer, new ArrayList<>());
    }

    protected AbstractPlaceholderContext(@Nullable Player viewer, @NotNull List<PlaceholderReplacer> placeholderReplacers) {
        this.viewer = viewer;
        this.placeholderReplacers = placeholderReplacers;
    }

    @Override
    public @Nullable Player getViewer() {
        return viewer;
    }

    @Override
    public @NotNull PlaceholderContext appendReplacerFirst(@NotNull PlaceholderReplacer replacer) {
        placeholderReplacers.add(0, replacer);
        return this;
    }

    @Override
    public @NotNull PlaceholderContext appendReplacer(@NotNull PlaceholderReplacer replacer) {
        placeholderReplacers.add(replacer);
        return this;
    }

    @Override
    public @NotNull PlaceholderContext appendReplacerFirst(@NotNull LitePlaceholderReplacer replacer) {
        placeholderReplacers.add(0, replacer);
        return this;
    }

    @Override
    public @NotNull PlaceholderContext appendReplacer(@NotNull LitePlaceholderReplacer replacer) {
        placeholderReplacers.add(replacer);
        return this;
    }

    @Override
    public @NotNull PlaceholderContext appendPAPIReplacerFirst() {
        return appendReplacerFirst(PacketInventoryAPIPlugin.getPlaceholderReplacerPAPI());
    }

    @Override
    public @NotNull PlaceholderContext appendPAPIReplacer() {
        return appendReplacer(PacketInventoryAPIPlugin.getPlaceholderReplacerPAPI());
    }

    @Override
    public @NotNull PlaceholderContext appendContextReplacerFirst(@NotNull PlaceholderContext context) {
        return appendReplacerFirst(new PlaceholderContextReplacer(context));
    }

    @Override
    public @NotNull PlaceholderContext appendContextReplacer(@NotNull PlaceholderContext context) {
        return appendReplacer(new PlaceholderContextReplacer(context));
    }

    @Override
    public @NotNull @UnmodifiableView List<PlaceholderReplacer> getReplacers() {
        return Collections.unmodifiableList(placeholderReplacers);
    }

    @Override
    public @NotNull PlaceholderContext removeReplacer(@NotNull PlaceholderReplacer replacer) {
        placeholderReplacers.remove(replacer);
        return this;
    }

    @Override
    public @NotNull String replacePlaceholders(@NotNull String original, @Nullable Integer slot) {
        StringContainer wrapper = StringContainer.wrap(original, slot);
        replacePlaceholders(wrapper);
        return wrapper.getString();
    }

    @Override
    public @NotNull PlaceholderContext replacePlaceholders(@NotNull StringContainer container) {
        if(viewer == null || placeholderReplacers.isEmpty() || container.isEmpty())
            return this;

        placeholderReplacers.forEach(replacer -> replacer.replace(viewer, container));
        return this;
    }

    @Override
    public @NotNull List<String> replacePlaceholders(@NotNull List<String> original, @Nullable Integer slot) {
        ListContainer wrapper = ListContainer.wrap(original, slot);
        replacePlaceholders(wrapper);
        return wrapper.getList();
    }

    @Override
    public @NotNull PlaceholderContext replacePlaceholders(@NotNull ListContainer container) {
        if(viewer == null || placeholderReplacers.isEmpty() || container.isEmpty())
            return this;

        placeholderReplacers.forEach(replacer -> replacer.replace(viewer, container));
        return this;
    }

    @Override
    @SuppressWarnings("deprecation")
    public @NotNull ItemStack replacePlaceholders(@NotNull ItemStack original, @Nullable Integer slot) {
        if(original == null || !original.hasItemMeta())
            return original;

        ItemStack clone = original.clone();
        ItemMeta itemMeta = original.getItemMeta();

        if(itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            itemMeta.setDisplayName(replacePlaceholders(displayName, slot));
        }

        if(itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            itemMeta.setLore(replacePlaceholders(lore, slot));
        }

        if(original instanceof WrappedItemStack) {
            WrappedItemStack wrapper = (WrappedItemStack) original;
            String playerHead = wrapper.getVanillaItem().getPlayerHead();
            if(playerHead != null && !playerHead.isEmpty() && itemMeta instanceof SkullMeta) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;
                OfflinePlayer owningPlayer = Bukkit.getOfflinePlayer(replacePlaceholders(playerHead, slot));
                skullMeta.setOwningPlayer(owningPlayer);
            }
        }

        clone.setItemMeta(itemMeta);
        return clone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPlaceholderContext that = (AbstractPlaceholderContext) o;
        return Objects.equals(viewer, that.viewer) &&
                Objects.equals(placeholderReplacers, that.placeholderReplacers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(viewer, placeholderReplacers);
    }

    @Override
    public @NotNull String toString() {
        return "PlaceholderContext{" +
                "viewer=" + viewer +
                ", replacers=" + placeholderReplacers +
                '}';
    }

}
