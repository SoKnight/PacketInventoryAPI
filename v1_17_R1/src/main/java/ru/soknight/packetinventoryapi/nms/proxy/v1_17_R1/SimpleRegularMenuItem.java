package ru.soknight.packetinventoryapi.nms.proxy.v1_17_R1;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.soknight.packetinventoryapi.menu.item.regular.AbstractRegularMenuItem;
import ru.soknight.packetinventoryapi.util.Validate;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public final class SimpleRegularMenuItem extends AbstractRegularMenuItem<SimpleRegularMenuItem, SimpleRegularMenuItem.Builder> {

    private static Method SET_PROFILE_METHOD;

    private ItemStack nmsItem;

    private SimpleRegularMenuItem(ConfigurationSection configuration) {
        super(configuration);
    }

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

    public static Builder build(ConfigurationSection configuration) {
        return new Builder(new SimpleRegularMenuItem(configuration));
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

            ItemMeta itemMeta = menuItem.bukkitItem.getItemMeta();
            if(itemMeta != null) {
                SkullMeta skullMeta = (SkullMeta) itemMeta;

                GameProfile profile = new GameProfile(UUID.randomUUID(), "");
                Property property = new Property("textures", value);
                profile.getProperties().put("textures", property);

                try {
                    if(SET_PROFILE_METHOD == null) {
                        SET_PROFILE_METHOD = skullMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                        SET_PROFILE_METHOD.setAccessible(true);
                    }

                    SET_PROFILE_METHOD.invoke(skullMeta, profile);

                    menuItem.bukkitItem.setItemMeta(itemMeta);
                    menuItem.requireItemRemap();
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {}
            }

            /*GameProfile profile = new GameProfile(UUID.randomUUID(), "");
            Property property = new Property("textures", value);
            profile.getProperties().put("textures", property);
            NBTTagCompound serialized = GameProfileSerializer.serialize(new NBTTagCompound(), profile);

            ItemStack itemStack = menuItem.asVanillaItem();
            NBTTagCompound tag = itemStack.getOrCreateTag();
            tag.set("SkullOwner", serialized);
            itemStack.setTag(tag);*/

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
