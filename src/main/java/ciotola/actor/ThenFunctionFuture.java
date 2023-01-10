package ciotola.actor;

@FunctionalInterface
public interface ThenFunctionFuture<T> {
    void call(T value);
}
