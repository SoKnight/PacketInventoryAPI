package ru.soknight.packetinventoryapi.storage;

import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import ru.soknight.packetinventoryapi.PacketInventoryAPIPlugin;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.container.data.EnchantmentPosition;
import ru.soknight.packetinventoryapi.container.data.LecternButtonType;
import ru.soknight.packetinventoryapi.container.type.*;
import ru.soknight.packetinventoryapi.event.container.*;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.event.window.WindowClickEvent;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.server.PacketServerCloseWindow;
import ru.soknight.packetinventoryapi.packet.server.PacketServerOpenWindow;
import ru.soknight.packetinventoryapi.packet.server.PacketServerSetSlot;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

public final class SimpleContainerStorage implements ContainerStorage {

    private final Map<String, Container<?, ?>> containers = new ConcurrentHashMap<>();

    @Override
    public void open(Container<?, ?> container) {
        open(container, false);
    }

    public synchronized void open(Container<?, ?> container, boolean reopened) {
        container.sync(() -> {
            containers.put(container.getInventoryHolder().getName(), container);

            sendOpenPacket(container);
            container.onOpen(reopened);
        });
    }

    @Override
    public boolean close(Player player) {
        return close(player, false);
    }
    
    public boolean close(Player player, boolean closedByHolder) {
        Container<?, ?> container = containers.get(player.getName());
        if(container == null) return false;

        close(container, closedByHolder);
        return true;
    }

    @Override
    public void close(Container<?, ?> container) {
        close(container, false);
    }
    
    public synchronized void close(Container<?, ?> container, boolean closedByHolder) {
        container.sync(() -> {
            if(!container.onClose(closedByHolder))
                return;

            sendClosePacket(container);
            containers.remove(container.getInventoryHolder().getName());
        });
    }
    
    public void closeAll() {
        containers.values()
                .parallelStream()
                .peek(this::sendClosePacket)
                .forEach(Container::onClose);
    }
    
    private void sendOpenPacket(Container<?, ?> container) {
        if(container.getInventoryHolder().isOnline())
            PacketAssistant.createServerPacket(PacketServerOpenWindow.class)
                    .windowID(container.getInventoryId())
                    .windowType(container.getContainerType().getTypeID())
                    .windowTitle(container.getTitle())
                    .send(container.getInventoryHolder());
    }
    
    private void sendClosePacket(Container<?, ?> container) {
        if(container.getInventoryHolder().isOnline())
            PacketAssistant.createServerPacket(PacketServerCloseWindow.class)
                    .windowID(container.getInventoryId())
                    .send(container.getInventoryHolder());
    }

    private Future<?> runAsync(Runnable runnable) {
        return PacketInventoryAPIPlugin.getSingleExecutorService().submit(runnable);
    }

    @SuppressWarnings("unchecked")
    public <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> boolean onWindowClick(
            Player actor,
            WindowClickType clickType,
            int clickedSlot,
            ItemStack clickedItem
    ) {
        C container = (C) containers.get(actor.getName());
        if(container == null)
            return false;

        if(!container.isInteractable()) {
            int modeId = clickType.getModeId();
            if(modeId == 1 || modeId == 2 || modeId == 4)
                container.updateContent().pushSync();
            else
                cancelClick(actor, container, clickedSlot, clickedItem);
        }

        runAsync(() -> container.onClick(new WindowClickEvent<>(actor, container, clickedSlot, clickType, clickedItem)));
        return true;
    }

    @SneakyThrows
    public void cancelClick(Player player, Container<?, ?> container, int slot, ItemStack clickedItem) {
        if(slot < 0)
            return;

        PacketAssistant.createServerPacket(PacketServerSetSlot.class)
                .windowID(-1)
                .slot(-1)
                .item(EMPTY_ITEM)
                .send(player);

        if(clickedItem != null && clickedItem.getType() != Material.AIR)
            PacketAssistant.createServerPacket(PacketServerSetSlot.class)
                    .windowID(container.getInventoryId())
                    .slot(slot)
                    .item(clickedItem)
                    .send(player);
    }
    
    public boolean clickedWindowButton(Player player, int payloadId) {
        Container<?, ?> container = containers.get(player.getName());
        if(container == null) return false;
        
        // enchantment table
        if(container instanceof EnchantmentTableContainer) {
            EnchantmentTableContainer table = (EnchantmentTableContainer) container;
            EnchantmentPosition position = EnchantmentPosition.getById(payloadId);
            runAsync(() -> table.onEnchantmentSelected(new EnchantmentSelectEvent(player, table, position)));
        // lectern
        } else if(container instanceof LecternContainer) {
            LecternContainer lectern = (LecternContainer) container;
            if(payloadId > 100) {
                int page = payloadId - 100;
                runAsync(() -> lectern.onPageOpened(new LecternPageOpenEvent(player, lectern, page)));
            } else {
                LecternButtonType buttonType = LecternButtonType.getById(payloadId);
                runAsync(() -> lectern.onButtonClicked(new LecternButtonClickEvent(player, lectern, buttonType)));
            }
        // stonecutter
        } else if(container instanceof StonecutterContainer) {
            StonecutterContainer stonecutter = (StonecutterContainer) container;
            runAsync(() -> stonecutter.onRecipeSelected(new RecipeSelectEvent(player, stonecutter, payloadId)));
        // loom
        } else if(container instanceof LoomContainer) {
            LoomContainer loom = (LoomContainer) container;
            runAsync(() -> loom.onPatternSelected(new PatternSelectEvent(player, loom, payloadId)));
        // anything else - close this inventory
        } else {
            close(container, false);
        }
        
        return true;
    }
    
    public boolean anvilNameChanged(Player player, String customName) {
        Container<?, ?> container = containers.get(player.getName());
        if(container == null) return false;
        
        if(container instanceof AnvilContainer) {
            AnvilContainer anvil = (AnvilContainer) container;
            runAsync(() -> anvil.onItemRename(new AnvilRenameEvent(player, anvil, customName)));
        } else {
            close(container, false);
        }
        
        return true;
    }
    
    public boolean beaconEffectChanged(Player player, int primary, int secondary) {
        Container<?, ?> container = containers.get(player.getName());
        if(container == null) return false;
        
        if(container instanceof BeaconContainer) {
            BeaconContainer beacon = (BeaconContainer) container;
            runAsync(() -> beacon.onEffectChanged(new BeaconEffectChangeEvent(player, beacon, primary, secondary)));
        } else {
            close(container, false);
        }
        
        return true;
    }
    
    public boolean tradeSelected(Player player, int slot) {
        Container<?, ?> container = containers.get(player.getName());
        if(container == null) return false;
        
        if(container instanceof MerchantContainer) {
            MerchantContainer merchant = (MerchantContainer) container;
            runAsync(() -> merchant.onTradeSelected(new TradeSelectEvent(player, merchant, slot)));
        } else {
            close(container, false);
        }
        
        return true;
    }
    
}
