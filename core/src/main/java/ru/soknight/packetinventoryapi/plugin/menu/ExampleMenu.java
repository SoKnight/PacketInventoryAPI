package ru.soknight.packetinventoryapi.plugin.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.configuration.MenuIO;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractMenuParseException;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractResourceException;
import ru.soknight.packetinventoryapi.menu.type.AbstractGenericMenu;

@MenuIO(resourcePath = "//menu//example.yml", outputFile = "menu//example.yml")
public class ExampleMenu extends AbstractGenericMenu {

    public ExampleMenu(Plugin plugin) throws AbstractMenuParseException, AbstractResourceException {
        super("example_menu", plugin, 1);

        super.getContainer().getOriginal().appendPAPIReplacer();
        super.getContainer().getOriginal().setClickOutsideToClose(true);
        super.load(true);
    }

    @Override
    protected void onOpen(Player actor, GenericContainer container) {
        actor.sendMessage("You opened a staff menu!");
    }

    @Override
    protected void onClick(Player actor, GenericContainer container, int clickedSlot, WindowClickType clickType, ItemStack clickedItem) {
        actor.sendMessage("You clicked at slot #" + clickedSlot + " (type: " + clickType + ")");
    }

    @Override
    protected void onClose(Player actor, GenericContainer container) {
        actor.sendMessage("You closed the staff menu!");
    }

}
