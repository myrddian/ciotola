package ciotola.actor;

public interface ThenFunctionFuture<T> {
    void call(CiotolaFuture<T> value);
}
