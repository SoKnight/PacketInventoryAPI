package ru.soknight.packetinventoryapi.exception.packet;

import lombok.Getter;
import ru.soknight.packetinventoryapi.packet.Packet;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public abstract class NoConstructorFoundException extends Exception {

    private final Class<? extends Packet> packetClass;
    private final Class<?>[] constructorArgs;

    protected NoConstructorFoundException(Class<? extends Packet> packetClass, Class<?>... constructorArgs) {
        super(String.format(
                "Cannot find a packet constructor %s#(%s)!",
                packetClass.getSimpleName(), argsAsString(constructorArgs)
        ));
        this.packetClass = packetClass;
        this.constructorArgs = constructorArgs;
    }

    private static String argsAsString(Class<?>... constructorArgs) {
        if(constructorArgs == null || constructorArgs.length == 0)
            return "";

        return Arrays.stream(constructorArgs)
                .map(Class::getSimpleName)
                .collect(Collectors.joining(", "));
    }

}
