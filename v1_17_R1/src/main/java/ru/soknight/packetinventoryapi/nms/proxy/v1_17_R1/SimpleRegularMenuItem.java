package ru.soknight.packetinventoryapi.nms.proxy.v1_17_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.nbt.GameProfileSerializer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soknight.packetinventoryapi.menu.item.AbstractRegularMenuItem;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.UUID;

public final class SimpleRegularMenuItem extends AbstractRegularMenuItem<SimpleRegularMenuItem, SimpleRegularMenuItem.Builder> {

    private ItemStack nmsItem;

    @Override
    protected SimpleRegularMenuItem getThis() {
        return this;
    }

    @Override
    public ItemStack asVanillaItem() {
        if(itemRemapRequired || nmsItem == null)
            nmsItem = CraftItemStack.asNMSCopy(bukkitItem);

        itemRemapRequired = false;
        return nmsItem;
    }

    @Override
    public int getCustomModelData() {
        ItemMeta itemMeta = bukkitItem.getItemMeta();
        return itemMeta != null ? itemMeta.getCustomModelData() : 0;
    }

    public static Builder build() {
        return new Builder(new SimpleRegularMenuItem());
    }

    protected static final class Builder extends AbstractRegularMenuItem.Builder<SimpleRegularMenuItem, Builder> {
        private Builder(SimpleRegularMenuItem menuItem) {
            super(menuItem);
        }

        @Override
        protected Builder getThis() {
            return this;
        }

        @Override
        public Builder base64Head(String value) {
            Validate.notEmpty(value, "base64 value");

            if(menuItem.getMaterial() != Material.PLAYER_HEAD)
                super.material(Material.PLAYER_HEAD);

            GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            Property property = new Property("textures", value);
            profile.getProperties().put("textures", property);
            NBTTagCompound serialized = GameProfileSerializer.serialize(new NBTTagCompound(), profile);

            ItemStack itemStack = menuItem.asVanillaItem();
            NBTTagCompound tag = itemStack.getOrCreateTag();
            tag.set("SkullOwner", serialized);
            itemStack.setTag(tag);

            return getThis();
        }

        @Override
        public Builder customModelData(Integer value) {
            ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
            if(itemMeta != null && (!itemMeta.hasCustomModelData() || itemMeta.getCustomModelData() != value)) {
                itemMeta.setCustomModelData(value);
                menuItem.bukkitItem.setItemMeta(itemMeta);
                menuItem.requireItemRemap();
            }

            return getThis();
        }
    }

}
