package com.discorpsee.lifecycle.builder;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;

public interface SimpleTransitionBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> extends LifecycleWrapperBuilder<T, S, E> {

    SimpleTransitionBuilder<T, S, E> from(S state);

    SimpleTransitionBuilder<T, S, E> to(S state);

    SimpleTransitionBuilder<T, S, E> event(E event);

    SimpleTransitionBuilder<T, S, E> guard(Guard<T> guard);

    SimpleTransitionBuilder<T, S, E> action(Action<T> action);

}
