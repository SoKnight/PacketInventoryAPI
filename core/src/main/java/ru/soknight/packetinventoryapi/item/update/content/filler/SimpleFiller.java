package ru.soknight.packetinventoryapi.item.update.content.filler;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class SimpleFiller extends AbstractFiller {

    static @NotNull Filler.Builder builder() {
        return new SimpleFiller().createBuilder();
    }

    @NotNull Filler.Builder createBuilder() {
        return new SimpleBuilder();
    }

    private final class SimpleBuilder extends AbstractBuilder {

    }

}
