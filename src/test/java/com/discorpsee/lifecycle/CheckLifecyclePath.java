package com.discorpsee.lifecycle;

import static com.discorpsee.lifecycle.data.MyEvent.CREATE;
import static com.discorpsee.lifecycle.data.MyEvent.DELETE;
import static com.discorpsee.lifecycle.data.MyEvent.SEND;
import static com.discorpsee.lifecycle.data.MyState.DELETED;
import static com.discorpsee.lifecycle.data.MyState.DELIVERED;
import static com.discorpsee.lifecycle.data.MyState.DRAFT;
import static com.discorpsee.lifecycle.data.MyState.ERROR;
import static com.discorpsee.lifecycle.data.MyState.NEW;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.discorpsee.lifecycle.model.Action;
import com.discorpsee.lifecycle.model.Guard;
import com.discorpsee.lifecycle.data.MyEvent;
import com.discorpsee.lifecycle.data.MyState;
import com.discorpsee.lifecycle.data.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
public class CheckLifecyclePath {

    @Mock
    private Guard<Request> validateGuard;
    @Mock
    private Guard<Request> checkRequestGuard;
    @Mock
    private Guard<Request> checkAttemptCountGuard;
    @Mock
    private Action<Request> logAction;

    @Mock
    private Action<Request> requestSendSuccess;
    @Mock
    private Action<Request> requestSendFail;
    @Mock
    private Action<Request> maxAttemptsAction;
    @Mock
    private Action<Request> maxAttemptsFailAction;
    @Mock
    private Action<Request> errorAction;


    private Lifecycle<Request, MyState, MyEvent> lifecycle;

    @BeforeEach
    public void setUpLifecycle() {
        this.lifecycle = LifecycleTestConfiguration
                .builder()
                .validateGuard(validateGuard)
                .checkRequestGuard(checkRequestGuard)
                .checkAttemptCountGuard(checkAttemptCountGuard)

                .logAction(logAction)
                .requestSendSuccess(requestSendSuccess)
                .requestSendFail(requestSendFail)

                .maxAttemptsAction(maxAttemptsAction)
                .maxAttemptsFailAction(maxAttemptsFailAction)
                .errorAction(errorAction)
                .build()

                .createLifecycle();
    }

    @Test
    public void shouldCreateRequestSuccessfully() {
        Request request = Request.from(DRAFT);

        when(validateGuard.evaluate(request, Map.of())).thenReturn(true);

        lifecycle.sendEvent(request, CREATE);

        assertThat(request.getState()).isEqualTo(NEW);
        verify(logAction).execute(eq(request), anyMap());
    }

    @Test
    public void shouldNotCreateRequestWithFailedGuard() {
        Request request = Request.from(DRAFT);

        when(validateGuard.evaluate(eq(request), anyMap())).thenReturn(false);

        lifecycle.sendEvent(request, CREATE);

        assertThat(request.getState()).isEqualTo(DRAFT);
        verifyNoInteractions(logAction);
    }

    @Test
    public void shouldFailWithWrongCreation() {
        Request request = Request.from(ERROR);
        assertThrows(IllegalArgumentException.class,
                () -> lifecycle.sendEvent(request, CREATE)
        );
    }

    @Test
    public void shouldDeliverSuccess() {
        Request request = Request.from(NEW);

        when(checkRequestGuard.evaluate(eq(request), anyMap())).thenReturn(true);

        lifecycle.sendEvent(request, SEND);

        assertThat(request.getState()).isEqualTo(DELIVERED);

        verify(requestSendSuccess).execute(eq(request), anyMap());
        verifyNoInteractions(requestSendFail, checkAttemptCountGuard, maxAttemptsAction, maxAttemptsFailAction, errorAction);
    }

    @Test
    public void shouldFailDeliverAndCheckMaxAttempts() {
        Request request = Request.from(NEW);

        when(checkRequestGuard.evaluate(eq(request), anyMap())).thenReturn(false);
        when(checkAttemptCountGuard.evaluate(eq(request), anyMap())).thenReturn(true);

        lifecycle.sendEvent(request, SEND);

        assertThat(request.getState()).isEqualTo(NEW);

        verify(requestSendFail).execute(eq(request), anyMap());
        verify(maxAttemptsAction).execute(eq(request), anyMap());
        verifyNoInteractions(requestSendSuccess, maxAttemptsFailAction, errorAction);
    }


    @Test
    public void shouldTransitToErrorWithFailedGuards() {
        Request request = Request.from(NEW);

        when(checkRequestGuard.evaluate(eq(request), anyMap())).thenReturn(false);
        when(checkAttemptCountGuard.evaluate(eq(request), anyMap())).thenReturn(false);

        lifecycle.sendEvent(request, SEND);

        assertThat(request.getState()).isEqualTo(ERROR);

        verify(requestSendFail).execute(eq(request), anyMap());
        verify(maxAttemptsFailAction).execute(eq(request), anyMap());
        verify(errorAction).execute(eq(request), anyMap());
        verifyNoInteractions(requestSendSuccess, maxAttemptsAction);
    }

    @Test
    public void shouldFailWithWrongAction() {
        Request request = Request.from(DRAFT);
        assertThrows(IllegalArgumentException.class,
                () -> lifecycle.sendEvent(request, DELETE)
        );
    }

    @Test
    public void shouldDeleteRequest() {
        Request request = Request.from(NEW);

        lifecycle.sendEvent(request, DELETE);

        assertThat(request.getState()).isEqualTo(DELETED);
    }

    @ParameterizedTest
    @MethodSource("stateToAvailableActionSource")
    public void shouldReturnEventsForDraft(MyState status, Set<MyEvent> availableEvents) {
        Request request = Request.from(status);

        var events = lifecycle.getAvailableEvents(request);
        assertThat(events).isEqualTo(availableEvents);
    }

    static Stream<Arguments> stateToAvailableActionSource() {
        return Stream.of(
                arguments(DRAFT, Set.of(CREATE)),
                arguments(NEW, Set.of(SEND, DELETE))
        );
    }
}
