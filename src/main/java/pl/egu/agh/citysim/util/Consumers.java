package pl.egu.agh.citysim.util;

import java.util.function.Consumer;

public class Consumers {
    public static <T> Consumer<T> empty() {
        return t -> {
        };
    }
}
