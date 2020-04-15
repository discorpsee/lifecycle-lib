package com.discorpsee.lifecycle.internal;

import com.discorpsee.lifecycle.HasState;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ChoiceTransition<T extends HasState<S>, S, E> implements Transition<T, S, E> {

    private final S from;
    private final E event;
    private final List<ChoiceBranch<T, S>> transitions;

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
        for (var transition : transitions) {
            var guard = transition.getGuard();
            if (guard != null) {
                if (guard.evaluate(object, context)) {
                    changeState(object, transition, context);
                    return;
                } else {
                    transition.invokeFail(object, context);
                }
            } else {
                changeState(object, transition, context);
                return;
            }
        }
    }

    private void changeState(T object, ChoiceBranch<T, S> transition, Map<String, Object> context) {
        object.setState(transition.getTo());
        transition.invokeSuccess(object, context);
    }
}
