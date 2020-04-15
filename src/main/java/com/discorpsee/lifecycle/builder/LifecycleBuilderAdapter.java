package com.discorpsee.lifecycle.builder;

import com.discorpsee.lifecycle.internal.Transition;
import com.discorpsee.lifecycle.HasState;

public interface LifecycleBuilderAdapter<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> {

    Transition<T, S, E> build();

}
