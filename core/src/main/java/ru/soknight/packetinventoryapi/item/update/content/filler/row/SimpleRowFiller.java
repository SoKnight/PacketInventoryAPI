package ru.soknight.packetinventoryapi.item.update.content.filler.row;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SimpleRowFiller extends AbstractRowFiller {

    static @NotNull RowFiller.Builder builder() {
        return new SimpleRowFiller().createBuilder();
    }

    @NotNull RowFiller.Builder createBuilder() {
        return new SimpleRowBuilder();
    }

    private final class SimpleRowBuilder extends AbstractRowBuilder {

    }

}
