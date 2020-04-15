package com.discorpsee.lifecycle.builder.impl;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.builder.LifecycleBuilder;
import com.discorpsee.lifecycle.builder.SimpleTransitionBuilder;
import com.discorpsee.lifecycle.internal.SimpleTransition;
import com.discorpsee.lifecycle.internal.Transition;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.utils.Assert;
import lombok.ToString;

import java.util.Optional;

@ToString(callSuper = true)
public class DefaultSimpleTransitionBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> extends AbstractTransitionBuilder<T, S, E> implements SimpleTransitionBuilder<T, S, E> {

    private S to;
    private Guard<T> guard;
    private Action<T> action;

    public DefaultSimpleTransitionBuilder(LifecycleBuilder<T, S, E> parent) {
        super(parent);
    }

    @Override
    public SimpleTransitionBuilder<T, S, E> from(S state) {
        Assert.notNull(state, "State cannot be null");
        this.from = state;
        return this;
    }

    @Override
    public SimpleTransitionBuilder<T, S, E> to(S state) {
        Assert.notNull(state, "State cannot be null");
        this.to = state;
        return this;
    }

    @Override
    public SimpleTransitionBuilder<T, S, E> event(E event) {
        Assert.notNull(event, "Event cannot be null");
        this.event = event;
        return this;
    }

    @Override
    public SimpleTransitionBuilder<T, S, E> guard(Guard<T> guard) {
        this.guard = guard;
        return this;
    }

    @Override
    public SimpleTransitionBuilder<T, S, E> action(Action<T> action) {
        this.action = action;
        return this;
    }

    @Override
    public Transition<T, S, E> build() {
        validateState();
        return SimpleTransition.<T, S, E>builder()
                .from(from)
                .to(Optional.ofNullable(to).orElse(from))
                .event(event)
                .guard(guard)
                .action(action)
                .build();
    }

    private void validateState() {
        Assert.notNull(from, () -> String.format("From State cannot be null, builder data = %s", this));
        Assert.notNull(event, () -> String.format("Event cannot be null, builder data = %s", this));
    }

}
