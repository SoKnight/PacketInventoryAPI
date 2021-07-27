package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

import java.util.List;

@Getter
public final class RequiredStatesNotFoundException extends AbstractItemParseException {

    private final List<String> notFoundStates;

    public RequiredStatesNotFoundException(String fileName, String itemKey, List<String> notFoundStates) {
        super(fileName, itemKey, "next required states weren't found: " + String.join(", ", notFoundStates));
        this.notFoundStates = notFoundStates;
    }

}
