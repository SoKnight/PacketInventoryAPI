package ru.soknight.packetinventoryapi.plugin.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.configuration.MenuIO;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItem;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationStates;
import ru.soknight.packetinventoryapi.container.type.GenericContainer;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractMenuParseException;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractResourceException;
import ru.soknight.packetinventoryapi.menu.context.state.selector.StateSelectorContext;
import ru.soknight.packetinventoryapi.menu.extended.PageableMenu;
import ru.soknight.packetinventoryapi.menu.item.page.element.PageElementMenuItem;
import ru.soknight.packetinventoryapi.menu.item.page.element.filler.PageContentFiller;
import ru.soknight.packetinventoryapi.menu.item.page.element.renderer.SlotItemRenderer;
import ru.soknight.packetinventoryapi.menu.item.regular.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.stateable.StateableMenuItem;
import ru.soknight.packetinventoryapi.placeholder.container.string.StringContainer;

@MenuIO(resourcePath = "//menu//example.yml", outputFile = "menu//example.yml")
public class ExampleMenu extends PageableMenu<Person> {

    private static final Person[] PERSONS;

    @ConfigurationItem("creative")
    private RegularMenuItem<?, ?> creativeItem;

    @ConfigurationItem("stats")
    @ConfigurationStates({"first", "second"})
    private StateableMenuItem statsItem;

    @ConfigurationItem("page-element")
    @ConfigurationStates({"second", "third"})
    private PageElementMenuItem<StateableMenuItem> pageElement;

    public ExampleMenu(Plugin plugin) throws AbstractMenuParseException, AbstractResourceException {
        super("example_menu", plugin, 1);

        super.getContainer().getOriginal().appendPAPIReplacer();
        super.getContainer().getOriginal().setClickOutsideToClose(true);

        super.load(true);

        super.setStateSelector("stats", this::selectStats);
        super.setStateSelector("page-element", this::selectPageElementState);
        super.setClickListener("stats", "second", this::handleStatsSecondClick);
        super.setClickListener("page-element", "third", this::handlePageElementThirdClick);
        super.setPageContentFiller("page-element", PageContentFiller.create(this::renderItemSlot, false).appendReplacer(this::putPersonData));
    }

    private void putPersonData(@NotNull Player player, @NotNull StringContainer container, int slot, int pageIndex, int totalIndex) {
        Person person = PERSONS[totalIndex];
        container
                .replace("%id%", pageIndex + 1)
                .replace("%real_name%", person.getRealName())
                .replace("%nickname%", person.getNickname())
                .replace("%age%", person.getAge());
    }

    private ItemStack renderItemSlot(Player player, RegularMenuItem<?, ?> menuItem, int slot, int pageIndex, int totalIndex) {
        if(totalIndex >= PERSONS.length)
            return SlotItemRenderer.EMPTY_ITEM;

        return menuItem.asBukkitItem().clone();
    }

    @Override
    public int getElementCount(Player viewer) {
        return PERSONS.length;
    }

    @Override
    public int getPageSize(Player viewer) {
        return pageElement.getElementPattern().getItemFor(viewer).getSlots().length;
    }

    private RegularMenuItem<?, ?> selectPageElementState(StateSelectorContext context)  {
        return context.getMenuItem().getStateItem("third");
    }

    private RegularMenuItem<?, ?> selectStats(StateSelectorContext context) {
        int clickCount = getDataHolder(context.getViewer()).get("click_count", 0);
        return context.getMenuItem().getStateItem(clickCount % 2 == 0 ? "first" : "second");
    }

    private void handleStatsSecondClick(Player actor, GenericContainer container, int clickedSlot, WindowClickType clickType, ItemStack itemStack) {
        actor.sendMessage("You clicked on the second-stated stats part!");

        RegularMenuItem<?, ?> stateItem = statsItem.selectStateItem(actor);
        getDataHolder(actor).addInt("click_count", 1);
        updateItem(stateItem, actor).pushSync();
    }

    private void handlePageElementThirdClick(Player actor, GenericContainer container, int clickedSlot, WindowClickType clickType, ItemStack itemStack) {
        actor.sendMessage("You clicked on the third-stated page element part!");

        RegularMenuItem<?, ?> stateItem = pageElement.getElementPattern().selectStateItem(actor);
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

    static {
        PERSONS = new Person[] {
                new Person("Kostya", "SoKnight", 16),
                new Person("Artem", "KlepsYT", 19),
                new Person("Vadim", "Morozzko", 23)
        };
    }

}
