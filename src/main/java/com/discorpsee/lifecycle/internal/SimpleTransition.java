package com.discorpsee.lifecycle.internal;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Builder
public class SimpleTransition<T extends HasState<S>, S, E> implements Transition<T, S, E> {

    private final S from;
    private final S to;
    private final E event;
    private final Guard<T> guard;
    private final Action<T> action;

    @Override
    public S from() {
        return from;
    }

    @Override
    public E event() {
        return event;
    }

    @Override
    public void perform(T object, Map<String, Object> context) {
        boolean success = invokeGuard(object, context);
        if (success) {
            object.setState(to);
            invokeAction(object, context);
        }
    }

    private boolean invokeGuard(T object, Map<String, Object> context) {
        return guard == null || guard.evaluate(object, context);
    }

    private void invokeAction(T object, Map<String, Object> context) {
        if (action != null) {
            action.execute(object, context);
        }
    }
}
