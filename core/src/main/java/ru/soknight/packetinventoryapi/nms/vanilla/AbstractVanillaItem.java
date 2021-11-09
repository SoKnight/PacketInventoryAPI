package ru.soknight.packetinventoryapi.nms.vanilla;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import lombok.AccessLevel;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.integration.Integrations;
import ru.soknight.packetinventoryapi.integration.itemsadder.ItemsAdderService;
import ru.soknight.packetinventoryapi.menu.item.WrappedItemStack;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;

@Getter
public abstract class AbstractVanillaItem<I extends AbstractVanillaItem<I, B>, B extends AbstractVanillaItem.Builder<I, B>> implements VanillaItem<I, B> {

    public static final Enchantment GLOWING_ENCHANTMENT = Enchantment.DURABILITY;

    @Getter(AccessLevel.NONE) private WrappedItemStack bukkitItem;
    @Getter(AccessLevel.NONE) protected boolean itemRemapRequired;

    protected int amount;
    protected String name;
    protected List<String> lore;

    protected boolean enchanted;
    protected String itemsAdderItem;
    protected String playerHead;
    protected String base64Head;
    protected Integer customModelData;

    protected AbstractVanillaItem() {
        this.bukkitItem = new WrappedItemStack(Material.AIR, this);
    }

    protected abstract I getThis();

    protected void requireItemRemap() {
        this.itemRemapRequired = true;
    }

    protected void changeWrappedItem(@NotNull Material type) {
        this.bukkitItem = new WrappedItemStack(type, this);
    }

    protected void changeWrappedItem(@NotNull ItemStack itemStack) {
        this.bukkitItem = new WrappedItemStack(itemStack, this);
    }

    @Override
    public WrappedItemStack asBukkitItem() {
        return bukkitItem;
    }

    @Override
    public Material getMaterial() {
        return bukkitItem.getType();
    }

    @Override
    public int getCustomModelData() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("custom model data modification methods isn't implemented for your version!");
    }

    @Override
    public boolean hasCustomModelData() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("custom model data modification methods isn't implemented for your version!");
    }

    @Override
    public void updateItemsAdderItem() {
        if(itemsAdderItem == null || itemsAdderItem.isEmpty())
            return;

        if(Integrations.availableItemsAdder()) {
            ItemStack asBukkitItem = ItemsAdderService.getAsBukkitItem(itemsAdderItem);
            if(asBukkitItem != null) {
                changeWrappedItem(asBukkitItem);
                updateAll(false);
                return;
            }
        }

        changeWrappedItem(Material.AIR);
        requireItemRemap();
    }

    protected void updateItemAmount() {
        bukkitItem.setAmount(Math.max(1, amount));
        requireItemRemap();
    }

    protected void updateItemName() {
        updateAndRefreshItemMeta(meta -> meta.setDisplayName(name));
    }

    protected void updateItemLore() {
        updateAndRefreshItemMeta(meta -> meta.setLore(lore));
    }

    protected void updateItemFlags() {
        updateAndRefreshItemMeta(meta -> meta.addItemFlags(ItemFlag.values()));
    }

    protected void updateItemEnchantedFlag() {
        updateAndRefreshItemMeta(meta -> {
            if(enchanted)
                meta.addEnchant(GLOWING_ENCHANTMENT, 1, true);
            else
                meta.removeEnchant(GLOWING_ENCHANTMENT);
        });
    }

    protected void updateItemCustomModelData() {
        updateItemMeta(meta -> {
            if(!hasCustomModelData() && customModelData == null) {
                return false;
            }

            if(hasCustomModelData() && customModelData != null) {
                int currentValue = getCustomModelData();
                if(currentValue == customModelData) {
                    return false;
                }
            }

            meta.setCustomModelData(customModelData);
            return true;
        });
    }

    protected void updateAll(boolean updateCustomModelData) {
        updateItemAmount();
        updateItemName();
        updateItemLore();
        updateItemEnchantedFlag();

        if(updateCustomModelData) {
            try {
                updateItemCustomModelData();
            } catch (UnsupportedOperationException ignored) {
            }
        }
    }

    private void updateItemMeta(Function<ItemMeta, Boolean> action) {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        if(itemMeta != null) {
            boolean somethingChanged = action.apply(itemMeta);
            if(somethingChanged) {
                bukkitItem.setItemMeta(itemMeta);
                requireItemRemap();
            }
        }
    }

    private void updateAndRefreshItemMeta(Consumer<ItemMeta> action) {
        updateItemMeta(meta -> {
            action.accept(meta);
            return true;
        });
    }

    @Override
    public boolean assignHeadTexture(ItemStack item, String base64Value) {
        return assignHeadTexture(item, base64Value, null);
    }

    @Override
    public boolean assignHeadTexture(SkullMeta itemMeta, String base64Value) {
        return assignHeadTexture(itemMeta, base64Value, null);
    }

    @Override
    public boolean assignHeadTexture(ItemStack item, String base64Value, String signature) {
        return assignHeadTexture(item, meta -> assignHeadTexture(meta, base64Value, signature));
    }

    @Override
    public boolean assignHeadTexture(ItemStack item, WrappedGameProfile gameProfile) {
        return assignHeadTexture(item, meta -> assignHeadTexture(meta, gameProfile));
    }

    private boolean assignHeadTexture(ItemStack item, Function<SkullMeta, Boolean> mapper) {
        if(item == null)
            return false;

        ItemMeta itemMeta = item.getItemMeta();
        if(itemMeta == null)
            return false;

        if(itemMeta instanceof SkullMeta) {
            boolean assigned = mapper.apply((SkullMeta) itemMeta);
            if(assigned)
                item.setItemMeta(itemMeta);
            return assigned;
        }

        return false;
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
        public B material(Material value) {
            if(menuItem.getMaterial() != value) {
                menuItem.asBukkitItem().setType(value != null ? value : Material.AIR);
                menuItem.updateItemFlags();
                menuItem.updateAll(false);
            }
            return getThis();
        }

        @Override
        public B itemsAdderItem(String value) {
            if(value == null || value.isEmpty())
                return getThis();

            menuItem.itemsAdderItem = value;
            menuItem.updateItemsAdderItem();
            return getThis();
        }

        @Override
        public B playerHead(String value) {
            if(value == null || value.isEmpty() || value.length() > 16)
                return getThis();

            material(Material.PLAYER_HEAD);

            menuItem.playerHead = value;
            return getThis();
        }

        @Override
        public B base64Head(String value) {
            if(value == null || value.isEmpty())
                return getThis();

            material(Material.PLAYER_HEAD);

            menuItem.assignHeadTexture(menuItem.asBukkitItem(), value);
            menuItem.base64Head = value;
            return getThis();
        }

        @Override
        public B amount(int value) {
            int amount = value;
            int maxStackSize = menuItem.asBukkitItem().getMaxStackSize();

            if(amount < 1)
                amount = 1;
            else if(amount > maxStackSize)
                amount = maxStackSize;

            menuItem.amount = amount;
            menuItem.updateItemAmount();
            return getThis();
        }

        @Override
        public B name(String value) {
            if(!Objects.equals(menuItem.name, value)) {
                menuItem.name = value;
                menuItem.updateItemName();
            }
            return getThis();
        }

        @Override
        public B lore(List<String> value) {
            if(!Objects.equals(menuItem.lore, value)) {
                menuItem.lore = value;
                menuItem.updateItemLore();
            }
            return getThis();
        }

        @Override
        public B enchanted(boolean value) {
            if(menuItem.enchanted != value) {
                menuItem.enchanted = value;
                menuItem.updateItemEnchantedFlag();
            }
            return getThis();
        }

        @Override
        public B customModelData(Integer value) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("custom model data modification methods isn't implemented for your version!");
        }
    }

}
