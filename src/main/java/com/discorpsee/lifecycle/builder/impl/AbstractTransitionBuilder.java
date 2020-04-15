package com.discorpsee.lifecycle.builder.impl;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.builder.LifecycleBuilder;
import com.discorpsee.lifecycle.builder.LifecycleBuilderAdapter;
import com.discorpsee.lifecycle.builder.LifecycleWrapperBuilder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public abstract class AbstractTransitionBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>>
        implements LifecycleWrapperBuilder<T, S, E>, LifecycleBuilderAdapter<T, S, E> {

    @ToString.Exclude
    private final LifecycleBuilder<T, S, E> parent;

    protected S from;
    protected E event;

    @Override
    public LifecycleBuilder<T, S, E> and() {
        return parent;
    }
}
