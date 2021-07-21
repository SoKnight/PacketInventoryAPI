package ru.soknight.packetinventoryapi.item;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ItemStackBuilder {

    private final ItemStack item;
    
    private ItemStackBuilder(Material material) {
        this.item = new ItemStack(material);
    }
    
    public static ItemStackBuilder newBuilder(Material itemType) {
        return new ItemStackBuilder(itemType);
    }
    
    public ItemStack build() {
        return item;
    }
    
    public ItemStackBuilder amount(int amount) {
        item.setAmount(amount);
        return this;
    }
    
    public ItemStackBuilder addEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }
    
    public ItemStackBuilder removeEnchantment(Enchantment enchantment) {
        item.removeEnchantment(enchantment);
        return this;
    }
    
    public ItemStackBuilder addAttribute(Attribute attribute, AttributeModifier modifier) {
        return itemMeta(meta -> meta.addAttributeModifier(attribute, modifier));
    }
    
    public ItemStackBuilder removeAttribute(Attribute attribute) {
        return itemMeta(meta -> meta.removeAttributeModifier(attribute));
    }
    
    public ItemStackBuilder removeAttribute(EquipmentSlot equipmentSlot) {
        return itemMeta(meta -> meta.removeAttributeModifier(equipmentSlot));
    }
    
    public ItemStackBuilder addItemFlags(ItemFlag... itemFlags) {
        return itemMeta(meta -> meta.addItemFlags(itemFlags));
    }
    
    public ItemStackBuilder removeItemFlags(ItemFlag... itemFlags) {
        return itemMeta(meta -> meta.removeItemFlags(itemFlags));
    }
    
    public ItemStackBuilder displayname(String displayname) {
        return itemMeta(meta -> meta.setDisplayName(colorize(displayname)));
    }
    
    public ItemStackBuilder displayname(String displaynameFormat, Object... arguments) {
        return displayname(String.format(displaynameFormat, arguments));
    }
    
    public ItemStackBuilder localizedName(String localizedName) {
        return itemMeta(meta -> meta.setLocalizedName(localizedName));
    }
    
    public ItemStackBuilder lore(List<String> lore) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(ItemStackBuilder::colorize);
        
        return itemMeta(meta -> meta.setLore(newLore));
    }
    
    public ItemStackBuilder lore(String... lore) {
        if(lore == null || lore.length == 0) return this;
        
        List<String> colored = Arrays.stream(lore)
                .map(ItemStackBuilder::colorize)
                .collect(Collectors.toList());
        
        return itemMeta(meta -> meta.setLore(colored));
    }
    
    public ItemStackBuilder lore(List<String> lore, Object... arguments) {
        List<String> newLore = new ArrayList<>(lore);
        newLore.replaceAll(ItemStackBuilder::colorize);

        List<String> colored = map(lore, ItemStackBuilder::colorize);
        List<String> formatted = map(colored, l -> String.format(l, arguments));
        
        return itemMeta(meta -> meta.setLore(formatted));
    }
    
    public ItemStackBuilder makeUnbreakable() {
        return itemMeta(meta -> meta.setUnbreakable(true));
    }

    @SuppressWarnings("unchecked")
    public <META extends ItemMeta> ItemStackBuilder itemMeta(Consumer<META> consumer) {
        META itemMeta = (META) item.getItemMeta();
        consumer.accept(itemMeta);
        item.setItemMeta(itemMeta);
        return this;
    }
    
    private static <T> List<T> map(List<T> source, Function<T, T> mapper) {
        return source.stream().map(mapper).collect(Collectors.toList());
    }
    
    private static String colorize(String source) {
        return source != null && !source.isEmpty()
                ? ChatColor.translateAlternateColorCodes('&', source)
                : source;
    }
    
}
