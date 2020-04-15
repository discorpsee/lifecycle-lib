package com.discorpsee.lifecycle.builder;

import com.discorpsee.lifecycle.impl.MapBasedLifecycle;
import com.discorpsee.lifecycle.utils.BuilderDataUtils;
import com.discorpsee.lifecycle.HasState;
import com.discorpsee.lifecycle.Lifecycle;
import com.discorpsee.lifecycle.builder.impl.DefaultChoiceTransitionBuilder;
import com.discorpsee.lifecycle.builder.impl.DefaultSimpleTransitionBuilder;

import java.util.ArrayList;
import java.util.List;

public interface LifecycleBuilder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> {

    SimpleTransitionBuilder<T, S, E> withTransition();

    ChoiceTransitionBuilder<T, S, E> withChoice();

    Lifecycle<T, S, E> build();

    static <T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> LifecycleBuilder<T, S, E> builder() {
        return new Builder<>();
    }

    class Builder<T extends HasState<S>, S extends Enum<S>, E extends Enum<E>> implements LifecycleBuilder<T, S, E> {

        private final List<LifecycleBuilderAdapter<T, S, E>> builders = new ArrayList<>();

        @Override
        public SimpleTransitionBuilder<T, S, E> withTransition() {
            return wrap(new DefaultSimpleTransitionBuilder<>(this));
        }

        @Override
        public ChoiceTransitionBuilder<T, S, E> withChoice() {
            return wrap(new DefaultChoiceTransitionBuilder<>(this));
        }

        @Override
        public Lifecycle<T, S, E> build() {
            var lifecycleData = new BuilderDataUtils<T, S, E>().transform(builders);
            return new MapBasedLifecycle<>(lifecycleData);
        }

        private <B extends LifecycleBuilderAdapter<T, S, E>> B wrap(B builder) {
            builders.add(builder);
            return builder;
        }

    }
}
