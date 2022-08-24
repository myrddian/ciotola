package ciotola.core;

public interface ServiceCtx<T> {
    boolean startUp();
    boolean shutDown();
    boolean run();
    String serviceName();
    int getId();
    T self();
}
