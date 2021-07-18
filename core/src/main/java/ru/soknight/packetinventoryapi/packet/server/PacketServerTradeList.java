package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import org.bukkit.inventory.MerchantRecipe;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

import java.util.List;

@ImplementedAs("SimplePacketServerTradeList")
public interface PacketServerTradeList extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.OPEN_WINDOW_MERCHANT;

    PacketServerTradeList windowID(int value);

    PacketServerTradeList trades(List<MerchantRecipe> trades);

    PacketServerTradeList villagerLevel(int value);

    PacketServerTradeList experience(int value);

    PacketServerTradeList regularVillager(boolean value);

    PacketServerTradeList canRestock(boolean value);

}
