package com.discorpsee.lifecycle.extension;

import com.discorpsee.lifecycle.model.Action;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ComposedAction<T> implements Action<T> {

    private final Action<T> left;
    private final Action<T> right;

    @Override
    public void execute(T object, Map<String, Object> context) {
        left.execute(object, context);
        right.execute(object, context);
    }
}
