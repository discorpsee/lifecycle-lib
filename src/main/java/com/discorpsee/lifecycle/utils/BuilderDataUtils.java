package com.discorpsee.lifecycle.utils;


import com.discorpsee.lifecycle.internal.Transition;
import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.builder.LifecycleBuilderAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BuilderDataUtils<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> {

    public Map<S, Map<E, Transition<T, S, E>>> transform(List<LifecycleBuilderAdapter<T, S, E>> builders) {
        return builders.stream()
                .map(LifecycleBuilderAdapter::build)
                .collect(Collectors.toMap(
                        Transition::from,
                        createTransitionMap(),
                        mergeMaps()
                ));
    }

    private BinaryOperator<Map<E, Transition<T, S, E>>> mergeMaps() {
        return (m1, m2) -> {
            m2.forEach((k, v) -> {
                if (m1.containsKey(k)) {
                    throw new IllegalArgumentException(String.format("Possible hide for action %s, State and Action must be unique", k));
                }
                m1.put(k, v);
            });
            return m1;
        };
    }

    private Function<Transition<T, S, E>, Map<E, Transition<T, S, E>>> createTransitionMap() {
        return transition -> {
            var map = new HashMap<E, Transition<T, S, E>>();
            map.put(transition.event(), transition);
            return map;
        };
    }
}
