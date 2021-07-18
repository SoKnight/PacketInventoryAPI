package ru.soknight.packetinventoryapi.nms.proxy.v1_15_R1.packet.server;

import com.comphenix.protocol.events.PacketContainer;
import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.packet.server.PacketServerTradeList;
import ru.soknight.packetinventoryapi.packet.server.WrappedServerPacket;

import java.util.List;

public class SimplePacketServerTradeList extends WrappedServerPacket implements PacketServerTradeList {

    public SimplePacketServerTradeList() {
        super(PacketServerTradeList.PACKET_TYPE);
    }

    public SimplePacketServerTradeList(PacketContainer container) {
        super(container);
    }

    @Override
    public PacketServerTradeList windowID(int value) {
        handle.getIntegers().write(0, value);
        return this;
    }

    @Override
    public PacketServerTradeList trades(List<MerchantRecipe> trades) {
        handle.getMerchantRecipeLists().write(0, trades);
        return this;
    }

    @Override
    public PacketServerTradeList villagerLevel(int value) {
        handle.getIntegers().write(1, value);
        return this;
    }

    @Override
    public PacketServerTradeList experience(int value) {
        handle.getIntegers().write(2, value);
        return this;
    }

    @Override
    public PacketServerTradeList regularVillager(boolean value) {
        handle.getBooleans().write(0, value);
        return this;
    }

    @Override
    public PacketServerTradeList canRestock(boolean value) {
        handle.getBooleans().write(1, value);
        return this;
    }

}
