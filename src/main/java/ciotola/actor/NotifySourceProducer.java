package ciotola.actor;

public interface NotifySourceProducer<T> extends SourceProducer<T>{
    boolean isReady();
}
