package com.discorpsee.lifecycle;

public interface HasState<S> {

    S getState();

    void setState(S state);
}
