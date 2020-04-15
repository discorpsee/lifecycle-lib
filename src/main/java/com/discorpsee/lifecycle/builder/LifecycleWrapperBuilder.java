package com.discorpsee.lifecycle.builder;

import com.discorpsee.lifecycle.HasState;

public interface LifecycleWrapperBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> {

    LifecycleBuilder<T, S, E> and();

}
