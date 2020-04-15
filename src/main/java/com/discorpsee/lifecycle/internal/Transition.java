package com.discorpsee.lifecycle.internal;

import com.discorpsee.lifecycle.HasState;

import java.util.Map;

public interface Transition<T extends HasState<S>, S, E> {

    S from();

    E event();

    void perform(T object, Map<String, Object> context);

}
