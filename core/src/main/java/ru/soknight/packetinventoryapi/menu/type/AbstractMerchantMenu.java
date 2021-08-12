package ru.soknight.packetinventoryapi.menu.type;

import net.md_5.bungee.api.chat.BaseComponent;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.annotation.container.TradeSelectListener;
import ru.soknight.packetinventoryapi.container.type.MerchantContainer;
import ru.soknight.packetinventoryapi.event.container.TradeSelectEvent;
import ru.soknight.packetinventoryapi.item.update.MerchantUpdateRequest;
import ru.soknight.packetinventoryapi.menu.AbstractMenu;

public abstract class AbstractMerchantMenu extends AbstractMenu<MerchantContainer, MerchantContainer.MerchantContentUpdateRequest> {

    public AbstractMerchantMenu(String name, Plugin providingPlugin) {
        this(MerchantContainer.createDefault(null), name, providingPlugin);
    }

    public AbstractMerchantMenu(String name, Plugin providingPlugin, String title) {
        this(MerchantContainer.create(null, title), name, providingPlugin);
    }

    public AbstractMerchantMenu(String name, Plugin providingPlugin, BaseComponent title) {
        this(MerchantContainer.create(null, title), name, providingPlugin);
    }

    public AbstractMerchantMenu(MerchantContainer container, String name, Plugin providingPlugin) {
        super(container, name, providingPlugin);
    }

    public MerchantUpdateRequest updateMerchant() {
        return getContainer().getOriginal().updateMerchant();
    }

    @TradeSelectListener
    public void onTradeSelect(TradeSelectEvent event) {
        runAsync(() -> onTradeSelect(
                event.getActor(), 
                event.getContainer(), 
                event.getSelectedSlot()
        ));
    }

    protected void onTradeSelect(
            Player actor, 
            MerchantContainer container,
            int slot
    ) {}

}
