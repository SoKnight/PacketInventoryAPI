package ru.soknight.packetinventoryapi.annotation.window;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ClickListener {

    int[] value() default {};

    int startSlot() default -1;

    int destSlot() default -1;

    boolean ignoreOutsideClick() default false;

    boolean includeInventory() default false;

    boolean includeHotbar() default false;

}
