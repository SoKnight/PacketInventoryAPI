package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R3;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.ItemStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import ru.soknight.packetinventoryapi.menu.item.regular.AbstractRegularMenuItem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
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
            nmsItem = CraftItemStack.asNMSCopy(asBukkitItem());

        itemRemapRequired = false;
        return nmsItem;
    }

    @Override
    public int getCustomModelData() {
        ItemMeta itemMeta = asBukkitItem().getItemMeta();
        return itemMeta != null ? itemMeta.getCustomModelData() : 0;
    }

    @Override
    public boolean hasCustomModelData() throws UnsupportedOperationException {
        ItemMeta itemMeta = asBukkitItem().getItemMeta();
        return itemMeta != null && itemMeta.hasCustomModelData();
    }

    @Override
    public boolean assignHeadTexture(SkullMeta itemMeta, String base64Value, String signature) {
        if(itemMeta == null)
            return false;

        GameProfile profile = new GameProfile(UUID.randomUUID(), "");
        Property property = new Property("textures", base64Value, signature);
        profile.getProperties().put("textures", property);
        return setGameProfile(itemMeta, profile);
    }

    @Override
    public boolean assignHeadTexture(SkullMeta itemMeta, WrappedGameProfile gameProfile) {
        Object handle = gameProfile.getHandle();
        if(handle instanceof GameProfile)
            return setGameProfile(itemMeta, (GameProfile) handle);
        else
            return false;
    }

    private boolean setGameProfile(SkullMeta itemMeta, GameProfile gameProfile) {
        try {
            if(SET_PROFILE_METHOD == null) {
                SET_PROFILE_METHOD = itemMeta.getClass().getDeclaredMethod("setProfile", GameProfile.class);
                SET_PROFILE_METHOD.setAccessible(true);
            }

            SET_PROFILE_METHOD.invoke(itemMeta, gameProfile);
            return true;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
            return false;
        }
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
        public Builder customModelData(Integer value) {
            if(!Objects.equals(menuItem.customModelData, value)) {
                menuItem.customModelData = value;
                menuItem.updateItemCustomModelData();
            }
            return getThis();
        }
    }

}
