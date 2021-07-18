package ru.soknight.packetinventoryapi.packet.server;

import com.comphenix.protocol.PacketType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;

@ImplementedAs("SimplePacketServerOpenWindow")
public interface PacketServerOpenWindow extends ServerPacket {

    PacketType PACKET_TYPE = PacketType.Play.Server.OPEN_WINDOW;

    PacketServerOpenWindow windowID(int value);

    PacketServerOpenWindow windowType(int value);

    PacketServerOpenWindow windowTitle(BaseComponent value);

    default PacketServerOpenWindow windowTitle(String text) {
        return windowTitle(new TextComponent(text != null ? text : ""));
    }

    default PacketServerOpenWindow windowTitleTranslatable(String localeKey, Object... args) {
        return windowTitle(new TranslatableComponent(localeKey, args));
    }

}
