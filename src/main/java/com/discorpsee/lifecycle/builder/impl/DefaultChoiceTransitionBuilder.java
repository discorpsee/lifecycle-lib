package com.discorpsee.lifecycle.builder.impl;

import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.builder.ChoiceTransitionBuilder;
import com.discorpsee.lifecycle.builder.LifecycleBuilder;
import com.discorpsee.lifecycle.internal.ChoiceBranch;
import com.discorpsee.lifecycle.internal.ChoiceTransition;
import com.discorpsee.lifecycle.internal.Transition;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.utils.Assert;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@ToString(callSuper = true)
public class DefaultChoiceTransitionBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> extends AbstractTransitionBuilder<T, S, E> implements ChoiceTransitionBuilder<T, S, E> {

    private ChoiceBranch<T, S> first;
    private List<ChoiceBranch<T, S>> branches = new ArrayList<>();
    private ChoiceBranch<T, S> last;

    public DefaultChoiceTransitionBuilder(LifecycleBuilder<T, S, E> parent) {
        super(parent);
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> from(S state) {
        Assert.notNull(state, "State cannot be null");
        this.from = state;
        return this;
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> event(E event) {
        Assert.notNull(event, "Event cannot be null");
        this.event = event;
        return this;
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> first(S state, Guard<T> guard) {
        return first(state, guard, null, null);
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> first(S state, Guard<T> guard, Action<T> success, Action<T> fail) {
        Assert.isNull(first, "First branch duplication");
        Assert.notNull(state, "State cannot be null");
        Assert.notNull(guard, "Guard cannot be null");
        this.first = ChoiceBranch.<T, S>builder()
                .to(state)
                .guard(guard)
                .success(success)
                .fail(fail)
                .build();
        return this;
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> then(S state, Guard<T> guard) {
        return then(state, guard, null, null);
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> then(S state, Guard<T> guard, Action<T> success, Action<T> fail) {
        Assert.notNull(state, "State cannot be null");
        Assert.notNull(guard, "Guard cannot be null");
        this.branches.add(ChoiceBranch.<T, S>builder()
                .to(state)
                .guard(guard)
                .success(success)
                .fail(fail)
                .build()
        );
        return this;
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> last(S state) {
        return last(state, null);
    }

    @Override
    public ChoiceTransitionBuilder<T, S, E> last(S state, Action<T> action) {
        Assert.isNull(last, "Last branch duplication");
        Assert.notNull(state, "State cannot be null");
        this.last = ChoiceBranch.<T, S>builder()
                .to(state)
                .success(action)
                .build();
        return this;
    }

    @Override
    public Transition<T, S, E> build() {
        var choices = collectChoices();
        validateState(choices);
        return new ChoiceTransition<>(this.from, this.event, choices);
    }

    private void validateState(List<ChoiceBranch<T, S>> choices) {
        Assert.notNull(from, () -> String.format("Status does not exist, builder data = %s", this));
        Assert.notNull(event, () -> String.format("Event does not exist, builder data = %s", this));
        Assert.notEmpty(choices, () -> String.format("Transitions do not exist, builder data = %s", this));
    }

    private List<ChoiceBranch<T, S>> collectChoices() {
        List<ChoiceBranch<T, S>> choices = new ArrayList<>();
        if (first != null) {
            choices.add(first);
        }
        choices.addAll(branches);
        if (last != null) {
            choices.add(last);
        }
        return choices;
    }
}
