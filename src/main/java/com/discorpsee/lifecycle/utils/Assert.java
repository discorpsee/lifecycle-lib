package com.discorpsee.lifecycle.utils;

import java.util.Collection;
import java.util.function.Supplier;

public class Assert {

    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object object, Supplier<String> messageSupplier) {
        if (object == null) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }

    public static void isNull(Object object, String message) {
        if (object != null) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notEmpty(Collection<?> collection, Supplier<String> messageSupplier) {
        if (collection == null || collection.isEmpty()) {
            throw new IllegalArgumentException(messageSupplier.get());
        }
    }
}
