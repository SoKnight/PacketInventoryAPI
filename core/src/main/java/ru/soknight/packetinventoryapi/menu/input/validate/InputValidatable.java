package ru.soknight.packetinventoryapi.menu.input.validate;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.ContentUpdateRequest;

public interface InputValidatable<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    InputValidatorBus<C, R> getValidatorBus();

    // --- input just has been validated
    default void onInputValidation(ValidationResult<C, R> result) {
        onInputValidation(
                result.getResultType(), 
                result.getViewer(), 
                result.getContainer(), 
                result.getInputData(), 
                result.getLastValidator()
        );
    }

    default void onInputValidation(
            ValidationResultType resultType, 
            Player viewer, 
            C container, 
            String input, 
            InputValidator<C, R> lastValidator
    ) {}

    // --- input has been validated successfully
    default void onInputValidationSucceed(ValidationResult<C, R> result) {
        onInputValidationSucceed(result.getViewer(), result.getContainer(), result.getInputData());
    }

    default void onInputValidationSucceed(Player viewer, C container, String input) {}

    // --- input has been validated unsuccessfully
    default void onInputValidationFailed(ValidationResult<C, R> result) {
        onInputValidationFailed(result.getViewer(), result.getContainer(), result.getInputData());
    }

    default void onInputValidationFailed(Player viewer, C container, String input, InputValidator<C, R> lastValidator) {
        onInputValidationFailed(viewer, container, input);
    }

    default void onInputValidationFailed(Player viewer, C container, String input) {}

}
