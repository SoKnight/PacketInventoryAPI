package ru.soknight.packetinventoryapi.nms.proxy.v1_16_R1;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.chat.ComponentSerializer;
import net.minecraft.server.v1_16_R1.IChatBaseComponent;
import org.bukkit.inventory.meta.ItemMeta;
import ru.soknight.packetinventoryapi.nms.ItemStackPatcher;
import ru.soknight.packetinventoryapi.util.ReflectionHelper;
import ru.soknight.packetinventoryapi.util.Validate;

public final class SimpleItemStackPatcher implements ItemStackPatcher {

    @Override
    public void setDisplayName(ItemMeta itemMeta, TextComponent component) {
        setDisplayName(itemMeta, componentToJson(component));
    }

    @Override
    public void setDisplayName(ItemMeta itemMeta, String rawJson) {
        Validate.notNull(itemMeta, "itemMeta");

        try {
            IChatBaseComponent asComponent = IChatBaseComponent.ChatSerializer.a(rawJson);
            ReflectionHelper.findFieldAndPutValue(itemMeta, "displayName", asComponent);
        } catch (Exception ignored) {
        }
    }

    private String componentToJson(TextComponent component) {
        return component != null ? ComponentSerializer.toString(component) : null;
    }

    private IChatBaseComponent jsonToComponent(String json) {
        return json != null ? IChatBaseComponent.ChatSerializer.a(json) : null;
    }

}
