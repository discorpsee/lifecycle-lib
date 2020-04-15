package com.discorpsee.lifecycle.extension;

import com.discorpsee.lifecycle.model.Action;
import lombok.RequiredArgsConstructor;

import java.util.Collection;
import java.util.Map;

@RequiredArgsConstructor
public class Actions<T> implements Action<T> {

    private final Collection<Action<T>> actions;

    @Override
    public void execute(T object, Map<String, Object> context) {
        actions.forEach(action -> action.execute(object, context));
    }

}
