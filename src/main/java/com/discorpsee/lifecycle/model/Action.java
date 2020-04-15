package com.discorpsee.lifecycle.model;

import com.discorpsee.lifecycle.extension.Actions;
import com.discorpsee.lifecycle.extension.ComposedAction;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface Action<T> {

    void execute(T object, Map<String, Object> context);

    default Action<T> and(Action<T> other) {
        return new ComposedAction<>(this, other);
    }

    static <T> Action<T> from(Collection<Action<T>> actions) {
        return new Actions<>(actions);
    }

    @SafeVarargs
    static <T> Action<T> from(Action<T>... actions) {
        return new Actions<>(List.of(actions));
    }
}
