package ciotola.actor;

public interface ErrorFunctionFuture {
    void call(ActorException exception);
}
