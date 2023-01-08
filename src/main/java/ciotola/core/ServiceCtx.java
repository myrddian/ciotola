package ciotola.core;

import ciotola.actor.CiotolaFuture;

public interface ServiceCtx<T,R> {
    String serviceName();
    String getId();
    T self();
    CiotolaFuture<R> run (Object ...values);
}

