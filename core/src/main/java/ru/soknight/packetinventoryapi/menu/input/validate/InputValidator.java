package ru.soknight.packetinventoryapi.menu.input.validate;

import org.bukkit.entity.Player;
import ru.soknight.packetinventoryapi.container.Container;
import ru.soknight.packetinventoryapi.item.update.content.ContentUpdateRequest;
import ru.soknight.packetinventoryapi.menu.input.predicate.FloatPredicate;

import java.util.function.*;

import static ru.soknight.packetinventoryapi.util.NumberConvertions.*;

@FunctionalInterface
public interface InputValidator<C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> {

    ValidationResultType validate(Player viewer, C container, String input);

    default ValidationResult<C, R> validateDefault(Player viewer, C container, String input) {
        return ValidationResult.of(validate(viewer, container, input), viewer, container, input, this);
    }

    // --- constant validators
    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> alwaysAllowing() {
        return (viewer, container, input) -> ValidationResultType.ALLOWED;
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> alwaysDenying() {
        return (viewer, container, input) -> ValidationResultType.DENIED;
    }

    // --- custom validator creators based on Java predicates
    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> custom(Predicate<String> predicate) {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(predicate.test(input));
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> custom(BiPredicate<Player, String> predicate) {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(predicate.test(viewer, input));
    }

    // --- value is a string (default) and ...
    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> stringNotEmpty() {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(!input.isEmpty());
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> stringLongerThan(int length) {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(input.length() > length);
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> stringShorterThan(int length) {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(input.length() < length);
    }

    // --- value is X and no more
    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isInteger() {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(asInt(input) != null);
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isLong() {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(asLong(input) != null);
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isDouble() {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(asDouble(input) != null);
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isFloat() {
        return (viewer, container, input) -> ValidationResultType.fromBoolean(asFloat(input) != null);
    }

    // --- value is X and ...
    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isIntegerAnd(IntPredicate predicate) {
        return (viewer, container, input) -> {
            Integer value = asInt(input);
            return ValidationResultType.fromBoolean(value != null && predicate.test(value));
        };
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isLongAnd(LongPredicate predicate) {
        return (viewer, container, input) -> {
            Long value = asLong(input);
            return ValidationResultType.fromBoolean(value != null && predicate.test(value));
        };
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isDoubleAnd(DoublePredicate predicate) {
        return (viewer, container, input) -> {
            Double value = asDouble(input);
            return ValidationResultType.fromBoolean(value != null && predicate.test(value));
        };
    }

    static <C extends Container<C, R>, R extends ContentUpdateRequest<C, R>> InputValidator<C, R> isFloatAnd(FloatPredicate predicate) {
        return (viewer, container, input) -> {
            Float value = asFloat(input);
            return ValidationResultType.fromBoolean(value != null && predicate.test(value));
        };
    }

}
