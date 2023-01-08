package ciotola.actor;

public interface NotifySourceActor<T> extends SourceActor<T> {
    boolean isReady();
}
