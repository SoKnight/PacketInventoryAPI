package ru.soknight.packetinventoryapi.listener;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.event.type.WindowClickType;
import ru.soknight.packetinventoryapi.lib.packetwrapper.play.server.WrapperPlayServerOpenWindow;
import ru.soknight.packetinventoryapi.lib.packetwrapper.play.server.WrapperPlayServerWindowProperty;
import ru.soknight.packetinventoryapi.packet.PacketAssistant;
import ru.soknight.packetinventoryapi.packet.client.*;
import ru.soknight.packetinventoryapi.storage.SimpleContainerStorage;

// TODO TRANSACTION IS DEPRECATED IN 1.17!
//@SuppressWarnings("deprecation")
public class PacketsListener extends PacketAdapter {

    private final SimpleContainerStorage storage;

    public PacketsListener(Plugin plugin, SimpleContainerStorage storage) {
        super(plugin, ListenerPriority.HIGHEST,
                PacketType.Play.Client.WINDOW_CLICK,    // click window
//                PacketType.Play.Client.TRANSACTION,     // window operation transaction
                PacketType.Play.Client.ENCHANT_ITEM,    // click window button
                PacketType.Play.Client.CLOSE_WINDOW,    // close window
                PacketType.Play.Client.ITEM_NAME,       // anvil rename
                PacketType.Play.Client.TR_SEL,          // trade select
                PacketType.Play.Client.BEACON,          // beacon effect change
                PacketType.Play.Server.OPEN_WINDOW,     // open window
                PacketType.Play.Server.WINDOW_DATA      // window properties
        );

        this.storage = storage;
        
        ProtocolLibrary.getProtocolManager().addPacketListener(this);
    }
    
    public void unregister() {
        ProtocolLibrary.getProtocolManager().removePacketListener(this);
    }
    
    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketType type = event.getPacketType();
        PacketContainer packet = event.getPacket();
        Player player = event.getPlayer();

        boolean cancelled = false;
        
        // click window
        if(type == PacketType.Play.Client.WINDOW_CLICK)
            cancelled = onClickWindow(PacketAssistant.createClientPacket(PacketClientClickWindow.class, packet, player));
        
        // window operation transaction
//        else if(type == PacketType.Play.Client.TRANSACTION)
//            cancelled = onTransaction(new WrapperPlayClientTransaction(packet), player);
        
        // click window buton
        else if(type == PacketType.Play.Client.ENCHANT_ITEM)
            cancelled = onClickWindowButton(PacketAssistant.createClientPacket(PacketClientClickWindowButton.class, packet, player));
        
        // close window
        else if(type == PacketType.Play.Client.CLOSE_WINDOW)
            cancelled = onCloseWindow(PacketAssistant.createClientPacket(PacketClientCloseWindow.class, packet, player));
        
        // anvil rename
        else if(type == PacketType.Play.Client.ITEM_NAME)
            cancelled = onAnvilRename(PacketAssistant.createClientPacket(PacketClientNameItem.class, packet, player));
        
        // trade select
        else if(type == PacketType.Play.Client.TR_SEL)
            cancelled = onTradeSelect(PacketAssistant.createClientPacket(PacketClientSelectTrade.class, packet, player));
        
        // beacon effect change
        else if(type == PacketType.Play.Client.BEACON)
            cancelled = onBeacon(PacketAssistant.createClientPacket(PacketClientSetBeaconEffect.class, packet, player));
        
        event.setCancelled(cancelled);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketType type = event.getPacketType();

        if(type == PacketType.Play.Server.OPEN_WINDOW) {
            WrapperPlayServerOpenWindow packet = new WrapperPlayServerOpenWindow(event.getPacket());
            System.out.printf(
                    "Opening window #%d type #%d with title: %s%n",
                    packet.getWindowId(),
                    packet.getWindowType(),
                    packet.getWindowTitle().getJson()
            );
        }

        if(type == PacketType.Play.Server.WINDOW_DATA) {
            WrapperPlayServerWindowProperty packet = new WrapperPlayServerWindowProperty(event.getPacket());
            System.out.printf(
                    "Sending window property #%d with value %d for window #%d%n",
                    packet.getProperty(),
                    packet.getValue(),
                    packet.getWindowId()
            );
        }
    }

    private boolean onClickWindow(PacketClientClickWindow packet) {
        WindowClickType clickType = packet.getClickType();
        int clickedSlot = packet.getSlot();
        ItemStack clickedItem = packet.getClickedItem();
        return storage.onWindowClick(packet.getPlayer(), clickType, clickedSlot, clickedItem);
    }
    
//    private boolean onTransaction(WrapperPlayClientTransaction packet, Player player) {
//        int windowId = packet.getWindowId();
//        int action = packet.getActionNumber();
//        boolean accepted = packet.getAccepted();
//        System.out.println("Transaction #" + action + " for window #" + windowId + ": " + accepted);
//        // TODO invoke ContainerStorage#handleTransaction
//        return true;
//    }
    
    private boolean onClickWindowButton(PacketClientClickWindowButton packet) {
        return storage.clickedWindowButton(packet.getPlayer(), packet.getButtonID());
    }
    
    private boolean onCloseWindow(PacketClientCloseWindow packet) {
        return storage.close(packet.getPlayer(), true);
    }
    
    private boolean onAnvilRename(PacketClientNameItem packet) {
        String customName = packet.getItemName();
        return storage.anvilNameChanged(packet.getPlayer(), customName);
    }

    private boolean onBeacon(PacketClientSetBeaconEffect packet) {
        int primaryEffect = packet.getPrimaryEffectID();
        int secondaryEffect = packet.getSecondaryEffectID();
        return storage.beaconEffectChanged(packet.getPlayer(), primaryEffect, secondaryEffect);
    }
    
    private boolean onTradeSelect(PacketClientSelectTrade packet) {
        int slot = packet.getSelectedSlot();
        return storage.tradeSelected(packet.getPlayer(), slot);
    }
    
}
