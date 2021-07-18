package ru.soknight.packetinventoryapi.plugin.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.configuration.MenuIO;
import ru.soknight.packetinventoryapi.container.type.AnvilContainer;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractMenuParseException;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractResourceException;
import ru.soknight.packetinventoryapi.menu.type.AbstractAnvilMenu;

@MenuIO(resourcePath = "//menu//example.yml", outputFile = "menu//example.yml")
public class ExampleMenu extends AbstractAnvilMenu {

    public ExampleMenu(Plugin plugin) throws AbstractMenuParseException, AbstractResourceException {
        super("example_menu", plugin);

        super.getContainer().getOriginal().setClickOutsideToClose(true);
        super.load(true);
//        super.updateContent()
//                .firstItem(Material.LIGHT_GRAY_STAINED_GLASS_PANE)
//                .resultItem(Material.DIAMOND)
//                .rows(Material.GREEN_STAINED_GLASS_PANE, 1, true, 0, 2)
//                .pushSync();
    }

    @Override
    protected void onRename(Player actor, AnvilContainer container, String customName) {
        actor.sendMessage("Your input is: " + customName);
    }

    @Override
    protected void onOpen(Player actor, AnvilContainer container) {
        actor.sendMessage("You opened an anvil menu!");
    }

    @Override
    protected void onClick(Player actor, AnvilContainer container, int clickedSlot, WindowClickType clickType, ItemStack clickedItem) {
        actor.sendMessage("You clicked at slot #" + clickedSlot + " (type: " + clickType + ")");
    }

    @Override
    protected void onClose(Player actor, AnvilContainer container) {
        actor.sendMessage("You closed the anvil menu!");
    }

}
