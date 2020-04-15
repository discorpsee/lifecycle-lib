package com.discorpsee.lifecycle.model;

import static com.discorpsee.lifecycle.extension.CompositionType.AND;
import static com.discorpsee.lifecycle.extension.CompositionType.OR;

import com.discorpsee.lifecycle.extension.ComposedGuard;
import com.discorpsee.lifecycle.extension.NegatedGuard;

import java.util.Map;

public interface Guard<T> {

    boolean evaluate(T object, Map<String, Object> context);

    default Guard<T> and(Guard<T> other) {
        return new ComposedGuard<T>(this, other, AND);
    }

    default Guard<T> or(Guard<T> other) {
        return new ComposedGuard<T>(this, other, OR);
    }

    default Guard<T> not() {
        return new NegatedGuard<>(this);
    }
}
