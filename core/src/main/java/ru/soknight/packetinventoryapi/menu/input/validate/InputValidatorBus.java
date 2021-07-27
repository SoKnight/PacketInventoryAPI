package ru.soknight.packetinventoryapi.menu.input.validate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

import java.util.List;

public interface InputValidatorBus<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidatorBus<C, R> create() {
        return new SimpleInputValidatorBus<>();
    }

    @NotNull ValidationResult<C, R> validate(@NotNull Player player, @NotNull C container, @NotNull String input);

    @NotNull InputValidatorBus<C, R> appendFirst(@NotNull InputValidator<C, R> validator);

    @NotNull InputValidatorBus<C, R> append(@NotNull InputValidator<C, R> validator);

    @NotNull List<InputValidator<C, R>> getValidators();

    @NotNull InputValidatorBus<C, R> remove(int index);

    @NotNull InputValidatorBus<C, R> remove(@NotNull InputValidator<C, R> validator);

    @NotNull InputValidatorBus<C, R> removeFirst();

    @NotNull InputValidatorBus<C, R> removeLast();

}
