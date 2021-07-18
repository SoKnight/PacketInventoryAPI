package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketServerCloseWindow")
public interface PacketServerCloseWindow extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.CLOSE_WINDOW;

    PacketServerCloseWindow windowID(int value);

}
