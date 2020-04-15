package com.discorpsee.lifecycle;

import static com.discorpsee.lifecycle.data.MyEvent.CREATE;
import static com.discorpsee.lifecycle.data.MyEvent.DELETE;
import static com.discorpsee.lifecycle.data.MyEvent.SEND;
import static com.discorpsee.lifecycle.data.MyState.DELETED;
import static com.discorpsee.lifecycle.data.MyState.DELIVERED;
import static com.discorpsee.lifecycle.data.MyState.DRAFT;
import static com.discorpsee.lifecycle.data.MyState.ERROR;
import static com.discorpsee.lifecycle.data.MyState.NEW;

import com.discorpsee.lifecycle.builder.LifecycleBuilder;
import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.data.MyEvent;
import com.discorpsee.lifecycle.data.MyState;
import com.discorpsee.lifecycle.data.Request;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Builder
public class LifecycleTestConfiguration {

    private final Guard<Request> validateGuard;
    private final Guard<Request> checkRequestGuard;
    private final Guard<Request> checkAttemptCountGuard;

    private final Action<Request> logAction;

    private final Action<Request> requestSendSuccess;
    private final Action<Request> requestSendFail;

    private final Action<Request> maxAttemptsAction;
    private final Action<Request> maxAttemptsFailAction;

    private final Action<Request> errorAction;

    public Lifecycle<Request, MyState, MyEvent> createLifecycle() {
        var builder = LifecycleBuilder.<Request, MyState, MyEvent>builder();

        builder
                .withTransition()
                .from(DRAFT).to(NEW).event(CREATE)
                .guard(validateGuard)
                .action(logAction)

                .and().withTransition()
                .from(NEW).to(DELETED).event(DELETE)

                .and().withChoice()
                .from(NEW).event(SEND)
                .first(DELIVERED, checkRequestGuard, requestSendSuccess, requestSendFail)
                .then(NEW, checkAttemptCountGuard, maxAttemptsAction, maxAttemptsFailAction)
                .last(ERROR, errorAction);

        return builder.build();
    }
}
