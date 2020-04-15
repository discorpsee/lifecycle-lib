package com.discorpsee.lifecycle.builder;

import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.HasState;

public interface ChoiceTransitionBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> extends LifecycleWrapperBuilder<T, S, E> {

    ChoiceTransitionBuilder<T, S, E> from(S state);

    ChoiceTransitionBuilder<T, S, E> event(E e);

    ChoiceTransitionBuilder<T, S, E> first(S state, Guard<T> guard);

    ChoiceTransitionBuilder<T, S, E> first(S state, Guard<T> guard, Action<T> success, Action<T> fail);

    ChoiceTransitionBuilder<T, S, E> then(S state, Guard<T> guard);

    ChoiceTransitionBuilder<T, S, E> then(S state, Guard<T> guard, Action<T> success, Action<T> fail);

    ChoiceTransitionBuilder<T, S, E> last(S state);

    ChoiceTransitionBuilder<T, S, E> last(S state, Action<T> action);

}
