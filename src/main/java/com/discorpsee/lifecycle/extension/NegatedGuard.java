package com.discorpsee.lifecycle.extension;

import com.discorpsee.lifecycle.model.Guard;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class NegatedGuard<T> implements Guard<T> {

    private final Guard<T> guard;

    @Override
    public boolean evaluate(T object, Map<String, Object> context) {
        return !guard.evaluate(object, context);
    }
}
