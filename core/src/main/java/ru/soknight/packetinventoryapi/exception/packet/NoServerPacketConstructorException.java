package ru.soknight.packetinventoryapi.exception.packet;

import ru.soknight.packetinventoryapi.packet.server.ServerPacket;

public class NoServerPacketConstructorException extends NoConstructorFoundException {

    public NoServerPacketConstructorException(Class<? extends ServerPacket> packetClass) {
        super(packetClass);
    }

}
