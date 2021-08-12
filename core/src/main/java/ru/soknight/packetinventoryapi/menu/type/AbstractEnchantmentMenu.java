package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.EnchantmentSelectListener;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.type.EnchantmentTableContainer;
import ru.soknight.packetinventoryapi.event.container.EnchantmentSelectEvent;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractEnchantmentMenu extends AbstractMenu<EnchantmentTableContainer, EnchantmentTableContainer.EnchantmentTableUpdateRequest> {

    public AbstractEnchantmentMenu(String name, Plugin providingPlugin) {
        this(EnchantmentTableContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractEnchantmentMenu(String name, Plugin providingPlugin, String title) {
        this(EnchantmentTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractEnchantmentMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(EnchantmentTableContainer.create(null, title), name, providingPlugin);
    }

    public AbstractEnchantmentMenu(EnchantmentTableContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    @EnchantmentSelectListener
    public void onEnchantmentSelect(EnchantmentSelectEvent event) {
        runAsync(() -> onEnchantmentSelect(
                event.getActor(), 
                event.getContainer(), 
                event.getEnchantmentPosition()
        ));
    }

    protected void onEnchantmentSelect(
            Player actor, 
            EnchantmentTableContainer container,
            EnchantmentPosition position
    ) {}

}
