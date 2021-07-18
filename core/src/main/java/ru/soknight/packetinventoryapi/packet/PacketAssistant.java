package ru.soknight.packetinventoryapi.packet;

import com.comphenix.protocol.events.PacketContainer;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import ru.soknight.packetinventoryapi.exception.packet.NoClientPacketConstructorException;
import ru.soknight.packetinventoryapi.exception.packet.NoConstructorFoundException;
import ru.soknight.packetinventoryapi.exception.packet.NoServerPacketConstructorException;
import ru.soknight.packetinventoryapi.nms.ImplementedAs;
import ru.soknight.packetinventoryapi.nms.NMSAssistant;
import ru.soknight.packetinventoryapi.packet.client.*;
import ru.soknight.packetinventoryapi.packet.server.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class PacketAssistant {

    public static final String NMS_VERSION;
    private static final String CLIENT_PACKAGE;
    private static final String SERVER_PACKAGE;

    private static final Map<Class<? extends ClientPacket>, InstanceBuilder> CLIENT_PACKETS;
    private static final Map<Class<? extends ServerPacket>, InstanceBuilder> SERVER_PACKETS;

    static {
        NMS_VERSION = NMSAssistant.getNMSVersion();
        CLIENT_PACKAGE = NMSAssistant.PACKAGE + ".packet.client";
        SERVER_PACKAGE = NMSAssistant.PACKAGE + ".packet.server";

        CLIENT_PACKETS = new HashMap<>();
        SERVER_PACKETS = new HashMap<>();
    }

    public static void init(Plugin plugin) throws NoConstructorFoundException {
        registerClientPacket(PacketClientClickWindow.class);
        registerClientPacket(PacketClientClickWindowButton.class);
        registerClientPacket(PacketClientCloseWindow.class);
        registerClientPacket(PacketClientNameItem.class);
        registerClientPacket(PacketClientSelectTrade.class);
        registerClientPacket(PacketClientSetBeaconEffect.class);

        registerServerPacket(PacketServerCloseWindow.class);
        registerServerPacket(PacketServerOpenWindow.class);
        registerServerPacket(PacketServerSetSlot.class);
        registerServerPacket(PacketServerTradeList.class);
        registerServerPacket(PacketServerWindowItems.class);
        registerServerPacket(PacketServerWindowProperty.class);

        plugin.getLogger().info("Successfully registered 12 packets implementations for your version!");
    }

    private static <P extends ClientPacket> void registerClientPacket(Class<P> packetClass) throws NoClientPacketConstructorException {
        ImplementedAs annotation = packetClass.getAnnotation(ImplementedAs.class);
        if(annotation == null)
            throw new IllegalArgumentException("class '" + packetClass.getName() + "' must be annotated with @ImplementedAs!");

        String value = annotation.value();
        try {
            Class<?> implClass = Class.forName(CLIENT_PACKAGE + "." + value);
            Constructor<?> constructor = implClass.getConstructor(PacketContainer.class, Player.class);
            InstanceBuilder builder = new InstanceBuilder(constructor);
            CLIENT_PACKETS.put(packetClass, builder);
        } catch (Throwable ex) {
            throw new NoClientPacketConstructorException(packetClass);
        }
    }

    private static <P extends ServerPacket> void registerServerPacket(Class<P> packetClass) throws NoServerPacketConstructorException {
        ImplementedAs annotation = packetClass.getAnnotation(ImplementedAs.class);
        if(annotation == null)
            throw new IllegalArgumentException("class '" + packetClass.getName() + "' must be annotated with @ImplementedAs!");

        String value = annotation.value();
        try {
            Class<?> implClass = Class.forName(SERVER_PACKAGE + "." + value);
            Constructor<?> constructor = implClass.getConstructor();
            InstanceBuilder builder = new InstanceBuilder(constructor);
            SERVER_PACKETS.put(packetClass, builder);
        } catch (Throwable ex) {
            throw new NoServerPacketConstructorException(packetClass);
        }
    }

    public static <P extends ClientPacket> P createClientPacket(Class<P> type, PacketContainer container, Player player) {
        InstanceBuilder builder = CLIENT_PACKETS.get(type);
        if(builder == null)
            throw new IllegalArgumentException("client packet '" + type.getSimpleName() + "' isn't registered yet!");

        return builder.createInstance(container, player);
    }

    public static <P extends ServerPacket> P createServerPacket(Class<P> type) {
        InstanceBuilder builder = SERVER_PACKETS.get(type);
        if(builder == null)
            throw new IllegalArgumentException("server packet '" + type.getSimpleName() + "' isn't registered yet!");

        return builder.createInstance();
    }

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    private static final class InstanceBuilder {
        private final Constructor<?> constructor;

        @SuppressWarnings("unchecked")
        private <T extends Packet> T createInstance(Object... args) {
            try {
                return (T) constructor.newInstance(args);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                return null;
            }
        }
    }

}
