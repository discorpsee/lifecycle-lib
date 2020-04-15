package com.discorpsee.lifecycle.extension;

import com.discorpsee.lifecycle.model.Guard;

import java.util.Map;

public enum CompositionType {

    AND {
        <T> boolean combine(Guard<T> left, Guard<T> right, T object, Map<String, Object> context) {
            return left.evaluate(object, context) && right.evaluate(object, context);
        }
    },
    OR {
        <T> boolean combine(Guard<T> left, Guard<T> right, T object, Map<String, Object> context) {
            return left.evaluate(object, context) || right.evaluate(object, context);
        }
    };

    abstract <T> boolean combine(Guard<T> left, Guard<T> right, T object, Map<String, Object> context);
}
