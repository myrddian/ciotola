package ciotola.actor;

@FunctionalInterface
public interface ErrorFunctionFuture {
    void call(ActorException exception);
}
