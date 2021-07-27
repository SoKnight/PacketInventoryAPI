package ru.soknight.packetinventoryapi.exception.configuration;

import lombok.Getter;
import ru.soknight.packetinventoryapi.configuration.item.ConfigurationItemType;

@Getter
public class UnexpectedPatternItemType extends AbstractItemParseException {

    private final ConfigurationItemType patternItemType;

    public UnexpectedPatternItemType(String fileName, String itemKey, ConfigurationItemType patternItemType) {
        super(fileName, itemKey, "unexpected pattern item type: " + patternItemType);
        this.patternItemType = patternItemType;
    }

}
