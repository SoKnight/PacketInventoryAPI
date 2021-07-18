package ru.soknight.packetinventoryapi.exception.configuration;

public class NoMenuIOException extends RuntimeException {

    public NoMenuIOException(String className) {
        super("menu " + className + " must be annotated with MenuIO to invoke MenuLoader#load(M, boolean)!");
    }

}
