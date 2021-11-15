package ru.soknight.packetinventoryapi.placeholder.context;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnmodifiableView;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.api.PacketInventoryAPI;
import ru.soknight.packetinventoryapi.menu.item.WrappedItemStack;
import ru.soknight.packetinventoryapi.nms.NMSAssistant;
import ru.soknight.packetinventoryapi.nms.vanilla.VanillaItem;
import ru.soknight.packetinventoryapi.placeholder.LitePlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;
import ru.soknight.packetinventoryapi.placeholder.container.list.ListContainer;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public abstract class AbstractPlaceholderContext implements PlaceholderContext {

    private static final String EMPTY_COMPONENT_JSON = "{\"text\":\"\"}";

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
    public @NotNull ItemStack replacePlaceholders(@NotNull ItemStack original, @Nullable Integer slot) {
        if(original == null || !original.hasItemMeta())
            return original;

        ItemStack clone = original.clone();
        ItemMeta itemMeta = original.getItemMeta();

        if(itemMeta.hasDisplayName()) {
            String displayName = itemMeta.getDisplayName();
            String value = replacePlaceholders(displayName, slot);

            if(value.isEmpty()) {
                NMSAssistant.getItemStackPatcher().setDisplayName(itemMeta, EMPTY_COMPONENT_JSON);
            } else {
                itemMeta.setDisplayName(value);
            }
        }

        if(itemMeta.hasLore()) {
            List<String> lore = itemMeta.getLore();
            itemMeta.setLore(replacePlaceholders(lore, slot));
        }

        if(original instanceof WrappedItemStack) {
            WrappedItemStack wrapper = (WrappedItemStack) original;
            VanillaItem<?, ?> vanillaItem = wrapper.getVanillaItem();

            // empty name
            String name = vanillaItem.getName();
            if(name != null && name.isEmpty()) {
                NMSAssistant.getItemStackPatcher().setDisplayName(itemMeta, EMPTY_COMPONENT_JSON);
            }

            // player head
            String playerHead = vanillaItem.getPlayerHead();
            if(playerHead != null && !playerHead.isEmpty() && itemMeta instanceof SkullMeta) {
                String value = replacePlaceholders(playerHead, slot);
                PacketInventoryAPI.getInstance()
                        .skinsProvidingBus()
                        .findPlayerSkin(value)
                        .ifPresent(gameProfile -> vanillaItem.assignHeadTexture((SkullMeta) itemMeta, gameProfile));
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
