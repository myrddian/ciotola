package ciotola.actor;

public interface BackgroundReadyActor {
    void process();
    boolean isReady();
}
