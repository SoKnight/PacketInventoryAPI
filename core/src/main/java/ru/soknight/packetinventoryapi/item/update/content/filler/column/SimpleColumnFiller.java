package ru.soknight.packetinventoryapi.item.update.content.filler.column;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SimpleColumnFiller extends AbstractColumnFiller {

    static @NotNull ColumnFiller.Builder builder() {
        return new SimpleColumnFiller().createBuilder();
    }

    @NotNull ColumnFiller.Builder createBuilder() {
        return new SimpleColumnBuilder();
    }

    private final class SimpleColumnBuilder extends AbstractColumnBuilder {

    }

}
