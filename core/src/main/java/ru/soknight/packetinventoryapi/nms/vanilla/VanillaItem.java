package ru.soknight.packetinventoryapi.nms.vanilla;

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

    String getName();

    BaseComponent getNameComponent();

    List<String> getLore();

    Material getMaterial();

    int getAmount();

    boolean isEnchanted();

    String getPlayerHead();

    String getBase64Head();

    String getASkinsHead();

    int getCustomModelData() throws UnsupportedOperationException;

    boolean assignHeadTexture(ItemStack item, String base64Value);

    boolean assignHeadTexture(SkullMeta itemMeta, String base64Value);

    boolean assignHeadTexture(ItemStack item, String base64Value, String signature);

    boolean assignHeadTexture(SkullMeta itemMeta, String base64Value, String signature);

    interface Builder<I extends VanillaItem<I, B>, B extends VanillaItem.Builder<I, B>> {
        I build();

        B name(String value);

        B nameComponent(BaseComponent value);

        B lore(List<String> value);

        B material(Material value);

        B amount(int value);

        B enchanted(boolean value);

        B playerHead(String value);

        B base64Head(String value);

        B aSkinsHead(String value);

        B customModelData(Integer value) throws UnsupportedOperationException;
    }

}
