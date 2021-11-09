package ru.soknight.packetinventoryapi.nms.vanilla;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

import java.util.List;

@ImplementedAs("SimpleRegularMenuItem")
public interface VanillaItem<I extends VanillaItem<I, B>, B extends VanillaItem.Builder<I, B>> {
    
    ItemStack asBukkitItem();

    Object asVanillaItem();

    Material getMaterial();

    String getItemsAdderItem();

    String getPlayerHead();

    String getBase64Head();

    int getAmount();

    String getName();

    BaseComponent getNameComponent();

    List<String> getLore();

    boolean isEnchanted();

    int getCustomModelData() throws UnsupportedOperationException;

    boolean assignHeadTexture(ItemStack item, String base64Value);

    boolean assignHeadTexture(SkullMeta itemMeta, String base64Value);

    boolean assignHeadTexture(ItemStack item, String base64Value, String signature);

    boolean assignHeadTexture(SkullMeta itemMeta, String base64Value, String signature);

    boolean assignHeadTexture(ItemStack item, WrappedGameProfile gameProfile);

    boolean assignHeadTexture(SkullMeta itemMeta, WrappedGameProfile gameProfile);

    interface Builder<I extends VanillaItem<I, B>, B extends VanillaItem.Builder<I, B>> {
        I build();

        B material(Material value);

        B itemsAdderItem(String value);

        B playerHead(String value);

        B base64Head(String value);

        B amount(int value);

        B name(String value);

        B nameComponent(BaseComponent value);

        B lore(List<String> value);

        B enchanted(boolean value);

        B customModelData(Integer value) throws UnsupportedOperationException;
    }

}
