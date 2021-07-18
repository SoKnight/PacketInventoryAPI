package ru.soknight.packetinventoryapi.exception.menu;

import lombok.Getter;

import java.lang.reflect.Method;

@Getter
public class InvalidMethodStructureException extends MenuRegistrationException {

    private static final String MESSAGE_FORMAT = "method %s#%s must receive only event instance parameter! (%s)";

    private final Method method;
    private final Class<?> eventClass;

    public InvalidMethodStructureException(Method method, Class<?> eventClass) {
        super(String.format(MESSAGE_FORMAT,
                method.getDeclaringClass().getSimpleName(),
                method.getName(),
                eventClass.getSimpleName()
        ));

        this.method = method;
        this.eventClass = eventClass;
    }

}
