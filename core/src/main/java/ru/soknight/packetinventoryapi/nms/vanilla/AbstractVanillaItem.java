package ru.soknight.packetinventoryapi.nms.vanilla;

import lombok.Getter;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public abstract class AbstractVanillaItem<I extends AbstractVanillaItem<I, B>, B extends AbstractVanillaItem.Builder<I, B>> implements VanillaItem<I, B> {

    public static final Enchantment GLOWING_ENCHANTMENT = Enchantment.DURABILITY;

    protected final ItemStack bukkitItem;
    protected boolean itemRemapRequired;

    @Getter protected boolean enchanted;
    @Getter protected String playerHead;
    @Getter protected String base64Head;

    protected AbstractVanillaItem() {
        this.bukkitItem = new ItemStack(Material.AIR);
    }

    protected void addItemFlags() {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        if(itemMeta != null) {
            itemMeta.addItemFlags(ItemFlag.values());
            bukkitItem.setItemMeta(itemMeta);
        }
    }

    protected abstract I getThis();

    protected void requireItemRemap() {
        this.itemRemapRequired = true;
    }

    @Override
    public ItemStack asBukkitItem() {
        return bukkitItem;
    }

    @Override
    public String getName() {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        if(itemMeta != null) {
            if(itemMeta.hasDisplayName())
                return itemMeta.getDisplayName();

            if(itemMeta.hasLocalizedName())
                return itemMeta.getLocalizedName();
        }
        return "";
    }

    @Override
    public BaseComponent getNameComponent() {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        if(itemMeta != null) {
            if(itemMeta.hasDisplayName())
                return new TextComponent(itemMeta.getDisplayName());

            if(itemMeta.hasLocalizedName())
                return new TranslatableComponent(itemMeta.getLocalizedName());
        }
        return new TextComponent();
    }

    @Override
    public List<String> getLore() {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        return itemMeta != null ? itemMeta.getLore() : null;
    }

    @Override
    public Material getMaterial() {
        return bukkitItem.getType();
    }

    @Override
    public int getAmount() {
        return bukkitItem.getAmount();
    }

    @Override
    public int getCustomModelData() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("custom model data modification methods isn't implemented for your version!");
    }

    protected static abstract class Builder<I extends AbstractVanillaItem<I, B>, B extends AbstractVanillaItem.Builder<I, B>> implements VanillaItem.Builder<I, B> {
        protected final I menuItem;

        protected Builder(I menuItem) {
            this.menuItem = menuItem;
        }

        protected abstract B getThis();

        @Override
        public I build() {
            return menuItem;
        }

        @Override
        public B name(String value) {
            ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
            if(itemMeta != null) {
                itemMeta.setDisplayName(value != null ? ChatColor.translateAlternateColorCodes('&', value) : null);
                menuItem.bukkitItem.setItemMeta(itemMeta);
                menuItem.requireItemRemap();
            }
            return getThis();
        }

        @Override
        public B nameComponent(BaseComponent value) {
            ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
            if(itemMeta != null) {
                itemMeta.setDisplayName(value != null ? value.toLegacyText() : null);
                menuItem.bukkitItem.setItemMeta(itemMeta);
                menuItem.requireItemRemap();
            }
            return getThis();
        }

        @Override
        public B lore(List<String> value) {
            ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
            if(itemMeta != null) {
                itemMeta.setLore(value);
                menuItem.bukkitItem.setItemMeta(itemMeta);
                menuItem.requireItemRemap();
            }
            return getThis();
        }

        @Override
        public B material(Material value) {
            menuItem.bukkitItem.setType(value != null ? value : Material.AIR);
            menuItem.addItemFlags();
            menuItem.requireItemRemap();
            return getThis();
        }

        @Override
        public B amount(int value) {
            int amount = value;
            int maxStackSize = menuItem.bukkitItem.getMaxStackSize();

            if(amount < 1)
                amount = 1;
            else if(amount > maxStackSize)
                amount = maxStackSize;

            menuItem.bukkitItem.setAmount(amount);
            menuItem.requireItemRemap();
            return getThis();
        }

        @Override
        public B enchanted(boolean value) {
            if(menuItem.enchanted != value) {
                ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
                if(value)
                    itemMeta.addEnchant(GLOWING_ENCHANTMENT, 1, true);
                else
                    itemMeta.removeEnchant(GLOWING_ENCHANTMENT);
                menuItem.enchanted = value;
                menuItem.requireItemRemap();
            }
            return getThis();
        }

        @Override
        public B customModelData(Integer value) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("custom model data modification methods isn't implemented for your version!");
        }
    }

}
