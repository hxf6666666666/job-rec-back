package org.example.jobrecback.utils;

import org.apache.commons.lang3.function.TriFunction;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class ResponseUtils {
    public static <R> ResponseEntity<R> response(Supplier<R> supplier) {
        try {
            R r = supplier.get();
            if (r != null) {
                return new ResponseEntity<>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static <T, R> ResponseEntity<R> response(Function<T, R> function, T t) {
        try {
            R r = function.apply(t);
            if (r != null) {
                return new ResponseEntity<>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static <T> ResponseEntity<String> response(Consumer<T> consumer, T t) {
        try {
            consumer.accept(t);
            return new ResponseEntity<>("操作成功", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("操作失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static <T, R> R response2(Function<T,R> function,T t){
        try {
            return function.apply(t);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T, U, R> ResponseEntity<R> response(BiFunction<T, U, R> function, T x, U y) {
        try {
            R r = function.apply(x, y);
            if (r != null) {
                return new ResponseEntity<>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static <T, U, V, R> ResponseEntity<R> response(TriFunction<T, U, V, R> function, T x, U y, V z) {
        try {
            R r = function.apply(x, y, z);
            if (r != null) {
                return new ResponseEntity<>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    public static <A, B, C, D, E, F, G, R> ResponseEntity<R> response(SevenFunction<A, B, C, D, E, F, G, R> function, A a, B b, C c, D d, E e, F f, G g) {
        try {
            R r = function.apply(a, b, c, d, e, f, g);
            if (r != null) {
                return new ResponseEntity<>(r, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e1) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
