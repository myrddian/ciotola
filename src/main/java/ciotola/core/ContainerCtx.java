package ciotola.core;

import ciotola.actor.CiotolaDirector;

import java.util.List;

public interface ContainerCtx {
    int getNumberOfCPUCores();
    String getOS();
    int threadCapacity();
    CiotolaDirector getDirector();
    List<ServiceCtx> getServiceCtxList();
    void registerFactory(ServiceCtxFactory factory);
    void resolveServices(Object objectScan);
}
