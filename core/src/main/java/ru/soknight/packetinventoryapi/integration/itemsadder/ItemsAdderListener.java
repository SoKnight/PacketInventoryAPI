package ru.soknight.packetinventoryapi.integration.itemsadder;

import dev.lone.itemsadder.api.Events.ItemsAdderLoadDataEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.menu.Menu;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.registry.MenuRegistry;

import java.util.Collection;

public final class ItemsAdderListener implements Listener {

    private final Plugin plugin;
    private final MenuRegistry menuRegistry;

    public ItemsAdderListener(@NotNull Plugin plugin, @NotNull MenuRegistry menuRegistry) {
        this.plugin = plugin;
        this.menuRegistry = menuRegistry;

        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onItemsAdderLoadData(@NotNull ItemsAdderLoadDataEvent event) {
        Collection<Menu<?, ?>> menus = menuRegistry.getRegisteredMenus().values();
        if(menus.isEmpty())
            return;

        menus.parallelStream().forEach(this::updateMenuItems);
        plugin.getLogger().info("Updated all items in " + menus.size() + " registered menu(s) due ItemsAdder reload event!");
    }

    private void updateMenuItems(@NotNull Menu<?, ?> menu) {
        menu.getMenuItems().values().forEach(this::updateMenuItem);
    }

    private void updateMenuItem(@NotNull MenuItem menuItem) {
        if(menuItem.isRegular())
            menuItem.asRegularItem().updateItemsAdderItem();
        else if(menuItem.isStateable())
            menuItem.asStateableItem().getStateItems().values().forEach(this::updateMenuItem);
        else if(menuItem.isPageElement())
            updateMenuItem(menuItem.asPageElementItem().getElementPattern());
        else
            plugin.getLogger().warning("Unexpected menu item implementation: " + menuItem);
    }

}
