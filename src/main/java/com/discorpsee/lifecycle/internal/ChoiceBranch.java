package com.discorpsee.lifecycle.internal;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
@Data
@Builder
public class ChoiceBranch<T extends HasState<S>, S> {
    private final S to;
    private final Guard<T> guard;
    private final Action<T> success;
    private final Action<T> fail;

    public void invokeSuccess(T object, Map<String, Object> context) {
        if (success != null) {
            success.execute(object, context);
        }
    }

    public void invokeFail(T object, Map<String, Object> context) {
        if (fail != null) {
            fail.execute(object, context);
        }
    }
}
