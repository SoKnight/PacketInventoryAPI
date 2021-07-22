package ru.soknight.packetinventoryapi.plugin.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.configuration.MenuIO;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItem;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractMenuParseException;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractResourceException;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.StateableMenuItem;
import ru.soknight.packetinventoryapi.menu.type.AbstractGenericMenu;

@MenuIO(resourcePath = "//menu//example.yml", outputFile = "menu//example.yml")
public class ExampleMenu extends AbstractGenericMenu {

    @ConfigurationItem("creative")
    private RegularMenuItem<?, ?> creativeItem;
    @ConfigurationItem("stats")
    private StateableMenuItem statsItem;

    public ExampleMenu(Plugin plugin) throws AbstractMenuParseException, AbstractResourceException {
        super("example_menu", plugin, 1);

        super.getContainer().getOriginal().appendPAPIReplacer();
        super.getContainer().getOriginal().setClickOutsideToClose(true);

        super.load(true);

        super.setStateSelector("stats", this::selectStats);
        super.setClickListener("stats", "second", this::handleStatsSecondClick);
    }

    private RegularMenuItem<?, ?> selectStats(StateableMenuItem item, Player player) {
        int clickCount = getDataHolder(player).get("click_count", 0);
        return item.getStateItem(clickCount % 2 == 0 ? "first" : "second");
    }

    private void handleStatsSecondClick(Player actor, GenericContainer container, int clickedSlot, WindowClickType clickType, ItemStack itemStack) {
        actor.sendMessage("You clicked on the second stats part!");

        RegularMenuItem<?, ?> stateItem = statsItem.selectStateItem(actor);
        getDataHolder(actor).addInt("click_count", 1);
        updateItem(stateItem, actor).pushSync();
    }

    @Override
    protected void onOpen(Player actor, GenericContainer container) {
        actor.sendMessage("You opened a staff menu!");
    }

    @Override
    protected void onClick(Player actor, GenericContainer container, int clickedSlot, WindowClickType clickType, ItemStack clickedItem) {
        RegularMenuItem<?, ?> stateItem = statsItem.selectStateItem(actor);
        if(stateItem.isSetAt(clickedSlot)) {
            getDataHolder(actor).addInt("click_count", 1);
            updateItem(stateItem, actor).pushSync();
            return;
        }

        actor.sendMessage("You clicked at slot #" + clickedSlot + " (type: " + clickType + ")");
    }

    @Override
    protected void onClose(Player actor, GenericContainer container) {
        actor.sendMessage("You closed the staff menu!");
    }

}
