package ru.soknight.packetinventoryapi.integration.skins;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@Getter
@AllArgsConstructor
public final class HeadTextureProperty {

    private final String value;
    private final String signature;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        HeadTextureProperty that = (HeadTextureProperty) o;
        return Objects.equals(value, that.value) && Objects.equals(signature, that.signature);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value, signature);
    }

    @Override
    public @NotNull String toString() {
        return "HeadTextureProperty{" +
                "value='" + value + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

}
