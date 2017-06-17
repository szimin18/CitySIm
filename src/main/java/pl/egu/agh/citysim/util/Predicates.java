package pl.egu.agh.citysim.util;

import java.util.function.Predicate;

public class Predicates {
    public static <T> Predicate<T> not(final Predicate<T> predicate) {
        return object -> !predicate.test(object);
    }
}
