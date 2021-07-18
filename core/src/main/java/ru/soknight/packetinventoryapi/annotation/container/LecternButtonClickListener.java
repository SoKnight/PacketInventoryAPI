package ru.soknight.packetinventoryapi.annotation.container;

import ru.soknight.packetinventoryapi.container.data.LecternButtonType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LecternButtonClickListener {

    LecternButtonType[] value() default {};

}
