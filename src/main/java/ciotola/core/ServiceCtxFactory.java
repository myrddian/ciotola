package ciotola.core;

public interface ServiceCtxFactory<T,R> {
    ServiceCtx<T,R> create(String id);
    String getServiceName();
}
