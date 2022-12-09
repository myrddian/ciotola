package ciotola.actor;

public interface ThenFunctionFuture<T> {
    void call(T value);
}
