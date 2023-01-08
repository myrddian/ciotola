package ciotola.actor;

public interface BackgroundActor {
    void process();
    long getDelay();
    boolean isReady();
}
