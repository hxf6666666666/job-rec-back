package org.example.jobrecback.utils;

import java.util.Objects;
import java.util.function.Function;

@FunctionalInterface
public interface SevenFunction<A, B, C, D, E, F, G, R> {

    R apply(A a, B b, C c, D d, E e, F f, G g);

    default <V> SevenFunction<A, B, C, D, E, F, G, V> andThen(
            Function<? super R, ? extends V> after) {
        Objects.requireNonNull(after);
        return (A a, B b, C c, D d, E e, F f, G g) -> after.apply(apply(a, b, c, d, e, f, g));
    }
}
