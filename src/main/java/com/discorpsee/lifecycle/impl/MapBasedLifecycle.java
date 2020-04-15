package com.discorpsee.lifecycle.impl;

import com.discorpsee.lifecycle.internal.Transition;
import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.Lifecycle;
import lombok.RequiredArgsConstructor;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class MapBasedLifecycle<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> implements Lifecycle<T, S, E> {

    private final Map<S, Map<E, Transition<T, S, E>>> lifecycle;

    @Override
    public Set<E> getAvailableEvents(T object) {
        return transitions(object).keySet();
    }

    @Override
    public void sendEvent(T object, E event, Map<String, Object> context) {
        var transition = transitions(object).get(event);
        if (Objects.isNull(transition)) {
            throw new IllegalArgumentException(String.format("Action %s doesnt exist for %s", event, object));
        }
        transition.perform(object, context);
    }

    private Map<E, Transition<T, S, E>> transitions(T object) {
        S state = object.getState();
        return Optional.ofNullable(lifecycle.get(state))
                .orElseThrow(() -> new IllegalArgumentException(String.format("State %s doesnt exist for %s", state, object)));
    }
}
