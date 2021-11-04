package ru.soknight.packetinventoryapi.item;

import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.util.Colorizer;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemStackBuilder {

    private final ItemStack item;
    
    private ItemStackBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
    }
    
    public static @NotNull ItemStackBuilder newBuilder(@NotNull Material itemType) {
        Validate.notNull(itemType, "itemType");
        return new ItemStackBuilder(itemType);
    }
    
    public @NotNull ItemStack build() {
        return item;
    }
    
    public @NotNull ItemStackBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public @NotNull ItemStackBuilder addEnchantment(@NotNull Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }
    
    public @NotNull ItemStackBuilder removeEnchantment(@NotNull Enchantment enchantment) {
        item.removeEnchantment(enchantment);
        return this;
    }
    
    public @NotNull ItemStackBuilder addAttribute(@NotNull Attribute attribute, @NotNull AttributeModifier modifier) {
        return itemMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }
    
    public @NotNull ItemStackBuilder removeAttribute(@NotNull Attribute attribute) {
        return itemMeta(meta -> meta.removeAttributeModifier(attribute));
    }
    
    public @NotNull ItemStackBuilder removeAttribute(@NotNull EquipmentSlot equipmentSlot) {
        return itemMeta(meta -> meta.removeAttributeModifier(equipmentSlot));
    }
    
    public @NotNull ItemStackBuilder addItemFlags(ItemFlag... itemFlags) {
        return itemMeta(meta -> meta.addItemFlags(itemFlags));
    }
    
    public @NotNull ItemStackBuilder removeItemFlags(ItemFlag... itemFlags) {
        return itemMeta(meta -> meta.removeItemFlags(itemFlags));
    }
    
    public @NotNull ItemStackBuilder displayname(@NotNull String displayname) {
        return itemMeta(meta -> meta.setDisplayName(Colorizer.colorize(displayname)));
    }
    
    public @NotNull ItemStackBuilder displayname(@NotNull String displaynameFormat, Object... arguments) {
        return displayname(String.format(displaynameFormat, arguments));
    }
    
    public @NotNull ItemStackBuilder localizedName(@NotNull String localizedName) {
        return itemMeta(meta -> meta.setLocalizedName(localizedName));
    }
    
    public @NotNull ItemStackBuilder lore(@NotNull List<String> lore) {
        List<String> newLore = Colorizer.colorize(new ArrayList<>(lore));
        return itemMeta(meta -> meta.setLore(newLore));
    }
    
    public @NotNull ItemStackBuilder lore(String... lore) {
        if(lore == null || lore.length == 0)
            return this;
        
        List<String> newLore = Colorizer.colorize(Arrays.asList(lore));
        return itemMeta(meta -> meta.setLore(newLore));
    }
    
    public @NotNull ItemStackBuilder lore(@NotNull List<String> lore, Object... arguments) {
        List<String> newLore = Colorizer.colorize(new ArrayList<>(lore));
        newLore.replaceAll(l -> String.format(l, arguments));
        return itemMeta(meta -> meta.setLore(newLore));
    }
    
    public @NotNull ItemStackBuilder makeUnbreakable() {
        return itemMeta(meta -> meta.setUnbreakable(true));
    }

    @SuppressWarnings("unchecked")
    public <META extends ItemMeta> @NotNull ItemStackBuilder itemMeta(@NotNull Consumer<META> consumer) {
        META itemMeta = (META) item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
        return this;
    }
    
    private static <T> @NotNull List<T> map(@NotNull List<T> source, @NotNull Function<T, T> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }
    
}
