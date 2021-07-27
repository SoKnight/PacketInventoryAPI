package ru.soknight.packetinventoryapi.menu.input.validate;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.Validate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

final class SimpleInputValidatorBus<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> implements InputValidatorBus<C, R> {

    private final List<InputValidator<C, R>> inputValidators;

    SimpleInputValidatorBus() {
        this.inputValidators = new ArrayList<>();
    }

    @Override
    public @NotNull ValidationResult<C, R> validate(@NotNull Player player, @NotNull C container, @NotNull String input) {
        if(inputValidators.isEmpty())
            return ValidationResult.allowed(player, container, input, null);

        ValidationResult<C, R> result = null;
        for(InputValidator<C, R> validator : inputValidators)
            if(!(result = validator.validateDefault(player, container, input)).hasValidatedSuccessfully())
                return result;

        return result;
    }

    @Override
    public @NotNull InputValidatorBus<C, R> appendFirst(@NotNull InputValidator<C, R> validator) {
        Validate.notNull(validator, "validator");
        inputValidators.add(0, validator);
        return this;
    }

    @Override
    public @NotNull InputValidatorBus<C, R> append(@NotNull InputValidator<C, R> validator) {
        Validate.notNull(validator, "validator");
        inputValidators.add(validator);
        return this;
    }

    @Override
    public @NotNull List<InputValidator<C, R>> getValidators() {
        return Collections.unmodifiableList(inputValidators);
    }

    @Override
    public @NotNull InputValidatorBus<C, R> remove(int index) {
        Validate.isTrue(index >= 0, "'index' cannot be lower than 0!");
        if(index < inputValidators.size())
            inputValidators.remove(index);
        return this;
    }

    @Override
    public @NotNull InputValidatorBus<C, R> remove(@NotNull InputValidator<C, R> validator) {
        Validate.notNull(validator, "validator");
        inputValidators.remove(validator);
        return this;
    }

    @Override
    public @NotNull InputValidatorBus<C, R> removeFirst() {
        if(!inputValidators.isEmpty())
            inputValidators.remove(0);
        return this;
    }

    @Override
    public @NotNull InputValidatorBus<C, R> removeLast() {
        if(!inputValidators.isEmpty())
            inputValidators.remove(inputValidators.size() - 1);
        return this;
    }

}
