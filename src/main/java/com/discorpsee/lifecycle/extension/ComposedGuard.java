package com.discorpsee.lifecycle.extension;

import com.discorpsee.lifecycle.model.Guard;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class ComposedGuard<T> implements Guard<T> {

    private final Guard<T> left;
    private final Guard<T> right;
    private final CompositionType composition;

    @Override
    public boolean evaluate(T object, Map<String, Object> context) {
        return composition.combine(left, right, object, context);
    }
}
