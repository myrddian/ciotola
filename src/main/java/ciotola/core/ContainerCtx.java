package ciotola.core;

import ciotola.actor.CiotolaDirector;

public interface ContainerCtx {
    int getNumberOfCPUCores();
    String getOS();
    int threadCapacity();
    CiotolaDirector getDirector();

}
