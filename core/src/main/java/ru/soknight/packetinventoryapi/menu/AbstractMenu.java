package ru.soknight.packetinventoryapi.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.annotation.window.ClickListener;
import ru.soknight.packetinventoryapi.annotation.window.CloseListener;
import ru.soknight.packetinventoryapi.annotation.window.OpenListener;
import ru.soknight.packetinventoryapi.configuration.MenuLoader;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemStructure;
import ru.soknight.packetinventoryapi.configuration.parse.ParsedDataBundle;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.holder.DataHolder;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.event.window.WindowOpenEvent;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractMenuParseException;
import ru.soknight.packetinventoryapi.exception.configuration.AbstractResourceException;
import ru.soknight.packetinventoryapi.exception.configuration.NoMenuIOException;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.exception.menu.MenuRegisteredAlreadyException;
import ru.soknight.packetinventoryapi.exception.menu.RegistrationDeniedException;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.listener.event.window.WindowClickListener;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;
import ru.soknight.packetinventoryapi.menu.item.MenuItem;
import ru.soknight.packetinventoryapi.menu.item.RegularMenuItem;
import ru.soknight.packetinventoryapi.menu.item.StateSelector;
import ru.soknight.packetinventoryapi.menu.item.StateableMenuItem;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
public abstract class AbstractMenu<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements Menu<C, R> {

    protected final String name;
    protected final Plugin providingPlugin;

    protected final PublicWrapper<C, R> container;
    protected final Map<String, MenuItem> menuItems;
    protected final ConfigurationItemStructure<Menu<C, R>> itemStructure;
    private MenuItem filler;

    protected AbstractMenu(PublicWrapper<C, R> container, String name, Plugin providingPlugin) {
        this.name = name;
        this.providingPlugin = providingPlugin;

        this.container = container;
        this.menuItems = new LinkedHashMap<>();
        this.itemStructure = new ConfigurationItemStructure<>(this);

        container.setEventListener(event -> PacketInventoryAPIPlugin.getApiInstance()
                .menuRegistry()
                .fireEvent(this, event));

        container.getOriginal()
                .appendReplacerFirst(PlaceholderReplacer.PLAYER_NAME)
                .setExtraDataProvider(this::provideExtraData);
    }

    protected AbstractMenu(C container, String name, Plugin providingPlugin) {
        this(PublicWrapper.wrap(container), name, providingPlugin);
    }

    protected void load(boolean resetContent) throws AbstractMenuParseException, AbstractResourceException, NoMenuIOException {
        MenuLoader.load(this, resetContent);
    }

    protected void load(String resourcePath, String outputFile, boolean resetContent) throws AbstractMenuParseException, AbstractResourceException {
        MenuLoader.load(this, resourcePath, outputFile, resetContent);
    }

    protected void provideExtraData(ContentUpdateRequest<C, R> updateRequest) {
        Player holder = updateRequest.getContainer().getInventoryHolder();
        if(holder == null)
            return;

        // --- provide stateable menu items
        for(MenuItem menuItem : menuItems.values()) {
            if(menuItem instanceof StateableMenuItem) {
                StateableMenuItem stateableItem = (StateableMenuItem) menuItem;
                RegularMenuItem<?, ?> stateItem = stateableItem.getItemFor(holder);
                if(stateItem != null) {
                    updateRequest.insert(stateItem, true);
                }
            }
        }
    }

    @Override
    public void updateParsedData(ParsedDataBundle parsedData) {
        this.menuItems.clear();
        this.menuItems.putAll(parsedData.getContent());

        this.itemStructure.fillItemFields(parsedData.getContent());

        this.filler = parsedData.getFiller();
    }

    public void register() throws
            InvalidMethodStructureException,
            RegistrationDeniedException,
            MenuRegisteredAlreadyException
    {
        PacketInventoryAPIPlugin.getApiInstance().menuRegistry().register(this);
    }

    public void unregister() {
        PacketInventoryAPIPlugin.getApiInstance().menuRegistry().unregister(this);
    }

    @Override
    public @NotNull C getOriginalContainer() {
        return container.getOriginal();
    }

    @Override
    public @NotNull DataHolder getDataHolder(Player player) {
        return container.getDataHolder(player);
    }

    @Override
    public @NotNull Map<String, MenuItem> getMenuItems() {
        return Collections.unmodifiableMap(menuItems);
    }

    @Override
    public @Nullable MenuItem getMenuItem(String id) {
        return menuItems.get(id);
    }

    @Override
    public @NotNull ConfigurationItemStructure<Menu<C, R>> configurationStructure() {
        return itemStructure;
    }

    protected void setStateSelector(String itemId, StateSelector stateSelector) {
        MenuItem menuItem = getMenuItem(itemId);
        if(menuItem != null && menuItem.isStateable())
            menuItem.asStateableItem().setStateSelector(stateSelector);
    }

    protected void setClickListener(String itemId, WindowClickListener<C, R> clickListener) {
        MenuItem menuItem = getMenuItem(itemId);
        if(menuItem != null && menuItem.isRegular())
            menuItem.asRegularItem().setClickListener(clickListener);
    }

    protected void setClickListener(String itemId, String stateId, WindowClickListener<C, R> clickListener) {
        MenuItem menuItem = getMenuItem(itemId);
        if(menuItem != null && menuItem.isStateable()) {
            RegularMenuItem<?, ?> stateItem = menuItem.asStateableItem().getStateItem(stateId);
            if(stateItem != null)
                stateItem.setClickListener(clickListener);
        }
    }

    public R updateContent() {
        return container.getOriginal().updateContent();
    }

    public R updateContent(Player viewer) {
        return container.getView(viewer).updateContent();
    }

    public R updateItem(RegularMenuItem<?, ?> menuItem) {
        return updateContent().insert(menuItem, true);
    }

    public R updateItem(RegularMenuItem<?, ?> menuItem, Player viewer) {
        return updateContent(viewer).insert(menuItem, true);
    }

    @OpenListener
    public void onOpen(WindowOpenEvent<C, R> event) {
        onOpen(event.getActor(), event.getContainer());
    }

    protected void onOpen(Player actor, C container) {}

    @ClickListener(includeHotbar = true, includeInventory = true)
    public void onClick(WindowClickEvent<C, R> event) {
        Player actor = event.getActor();
        int clickedSlot = event.getClickedSlot();

        for(MenuItem menuItem : menuItems.values()) {
            RegularMenuItem<?, ?> regularItem = menuItem.getItemFor(actor);
            if(regularItem == null)
                continue;

            if(!regularItem.isSetAt(clickedSlot))
                continue;

            WindowClickListener<C, R> clickListener = regularItem.getClickListener();
            if(clickListener == null)
                continue;

            clickListener.handle(event);
            return;
        }

        onClick(actor, event.getContainer(), clickedSlot, event.getClickType(), event.getClickedItem());
    }

    protected void onClick(Player actor, C container, int clickedSlot, WindowClickType clickType, ItemStack clickedItem) {}

    @CloseListener
    public void onClose(WindowCloseEvent<C, R> event) {
        onClose(event.getActor(), event.getContainer());
    }

    protected void onClose(Player actor, C container) {}

    @Override
    public boolean open(Player player) {
        return container.open(player);
    }

    @Override
    public boolean isViewing(Player player) {
        return container.isViewing(player);
    }

    @Override
    public boolean close(Player player) {
        return container.close(player);
    }

    @Override
    public void closeAll() {
        container.closeAll();
    }

}