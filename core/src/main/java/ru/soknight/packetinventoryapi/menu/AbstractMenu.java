package ru.soknight.packetinventoryapi.menu;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.window.ClickListener;
import ru.soknight.packetinventoryapi.annotation.window.CloseListener;
import ru.soknight.packetinventoryapi.annotation.window.OpenListener;
import ru.soknight.packetinventoryapi.configuration.MenuLoader;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.event.window.WindowCloseEvent;
import ru.soknight.packetinventoryapi.event.window.WindowOpenEvent;
import ru.soknight.packetinventoryapi.exception.configuration.*;
import ru.soknight.packetinventoryapi.exception.menu.InvalidMethodStructureException;
import ru.soknight.packetinventoryapi.exception.menu.MenuRegisteredAlreadyException;
import ru.soknight.packetinventoryapi.exception.menu.RegistrationDeniedException;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.container.PublicWrapper;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.placeholder.PlaceholderReplacer;

@Getter
public abstract class AbstractMenu<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements Menu<C, R> {

    private final PublicWrapper<C, R> container;
    private final String name;
    private final Plugin providingPlugin;

    protected AbstractMenu(PublicWrapper<C, R> container, String name, Plugin providingPlugin) {
        this.container = container;
        this.name = name;
        this.providingPlugin = providingPlugin;

        container.setEventListener(event -> PacketInventoryAPIPlugin.getApiInstance()
                .menuRegistry()
                .fireEvent(this, event));

        container.getOriginal().appendReplacerFirst(PlaceholderReplacer.PLAYER_NAME);
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

    public R updateContent() {
        return container.getOriginal().updateContent();
    }

    @OpenListener
    public void onOpen(WindowOpenEvent<C, R> event) {
        onOpen(event.getActor(), event.getContainer());
    }

    protected void onOpen(Player actor, C container) {}

    @ClickListener(includeHotbar = true, includeInventory = true)
    public void onClick(WindowClickEvent<C, R> event) {
        onClick(
                event.getActor(),
                event.getContainer(),
                event.getClickedSlot(),
                event.getClickType(),
                event.getClickedItem()
        );
    }

    protected void onClick(
            Player actor,
            C container,
            int clickedSlot,
            WindowClickType clickType,
            ItemStack clickedItem
    ) {}

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