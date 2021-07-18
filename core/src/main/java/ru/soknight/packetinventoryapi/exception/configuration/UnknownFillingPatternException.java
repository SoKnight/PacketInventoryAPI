package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;

@Getter
public class UnknownFillingPatternException extends AbstractItemParseException {

    private final String patternValue;

    public UnknownFillingPatternException(String fileName, String itemKey, String patternValue) {
        super(fileName, itemKey, "unknown filling pattern type '" + patternValue + "'!");
        this.patternValue = patternValue;
    }

}
