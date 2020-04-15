package com.discorpsee.lifecycle;

import java.util.Map;
import java.util.Set;

public interface Lifecycle<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> {

    Set<E> getAvailableEvents(T object);

    default void sendEvent(T object, E event) {
        sendEvent(object, event, Map.of());
    }

    void sendEvent(T object, E event, Map<String, Object> context);
}
