package ru.soknight.packetinventoryapi.menu.input.validate;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.util.Validate;

@Getter
public class ValidationResult<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    private final ValidationResultType resultType;
    private final Player viewer;
    private final C container;
    private final String inputData;
    private final InputValidator<C, R> lastValidator;

    public ValidationResult(
            @NotNull ValidationResultType resultType,
            @NotNull Player viewer,
            @NotNull C container,
            @NotNull String inputData,
            @Nullable InputValidator<C, R> lastValidator
    ) {
        Validate.notNull(resultType, "resultType");
        Validate.notNull(viewer, "viewer");
        Validate.notNull(container, "container");
        Validate.notNull(inputData, "inputData");

        this.resultType = resultType;
        this.viewer = viewer;
        this.container = container;
        this.inputData = inputData;
        this.lastValidator = lastValidator;
    }

    public boolean hasValidatedSuccessfully() {
        return resultType == ValidationResultType.ALLOWED;
    }

    // --- allowed
    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> ValidationResult<C, R> allowed(
            Player viewer,
            C container,
            String inputData,
            InputValidator<C, R> lastValidator
    ) {
        return of(ValidationResultType.ALLOWED, viewer, container, inputData, lastValidator);
    }

    // --- denied
    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> ValidationResult<C, R> denied(
            Player viewer,
            C container,
            String inputData,
            InputValidator<C, R> lastValidator
    ) {
        return of(ValidationResultType.DENIED, viewer, container, inputData, lastValidator);
    }

    // --- from boolean
    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> ValidationResult<C, R> of(
            boolean hasValidatedSuccessfully,
            Player viewer,
            C container,
            String inputData,
            InputValidator<C, R> lastValidator
    ) {
        ValidationResultType resultType = ValidationResultType.fromBoolean(hasValidatedSuccessfully);
        return new ValidationResult<>(resultType, viewer, container, inputData, lastValidator);
    }

    // --- from result type
    public static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> ValidationResult<C, R> of(
            ValidationResultType resultType,
            Player viewer,
            C container,
            String inputData,
            InputValidator<C, R> lastValidator
    ) {
        return new ValidationResult<>(resultType, viewer, container, inputData, lastValidator);
    }

}
