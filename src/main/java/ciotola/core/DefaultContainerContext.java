package ciotola.core;

import ciotola.actor.CiotolaDirector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class DefaultContainerContext implements ContainerCtx{

    private final Logger logger = LoggerFactory.getLogger(DefaultContainerContext.class);
    private CiotolaDirector director;
    private int cpuCoreCount = 0;

    public DefaultContainerContext() {
        director = new CiotolaDirector(getNumberOfCPUCores());
    }

    @Override
    public int getNumberOfCPUCores() {
        if (cpuCoreCount != 0 ) {
            return cpuCoreCount;
        }
        OSValidator osValidator = new OSValidator();
        String command = "";
        if (osValidator.isMac()) {
            logger.debug("System is running Mac OS");
            command = "sysctl -n machdep.cpu.core_count";
        } else if (osValidator.isUnix()) {
            logger.debug("System is running a Linux/Unix variant - trying lscpu");
            command = "lscpu";
        } else if (osValidator.isWindows()) {
            logger.debug("System is running Microsoft Windows");
            command = "cmd /C WMIC CPU Get /Format:List";
        }
        Process process = null;
        int numberOfCores = 0;
        int sockets = 0;
        try {
            if (osValidator.isMac()) {
                String[] cmd = {"/bin/sh", "-c", command};
                process = Runtime.getRuntime().exec(cmd);
            } else {
                process = Runtime.getRuntime().exec(command);
            }
        } catch (IOException e) {
            logger.error("Unable to determine physical processor layout - returning default", e);
            return threadCapacity();
        }

        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                if (osValidator.isMac()) {
                    numberOfCores = line.length() > 0 ? Integer.parseInt(line) : 0;
                } else if (osValidator.isUnix()) {
                    if (line.contains("Core(s) per socket:")) {
                        numberOfCores = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
                    }
                    if (line.contains("Socket(s):")) {
                        sockets = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
                    }
                } else if (osValidator.isWindows()) {
                    if (line.contains("NumberOfCores")) {
                        numberOfCores = Integer.parseInt(line.split("=")[1]);
                    }
                }
            }
        } catch (IOException e) {
            logger.error("Unable to determine physical processor layout - returning default", e);
            cpuCoreCount = threadCapacity();
        }
        if (osValidator.isUnix()) {
            cpuCoreCount = numberOfCores * sockets;
        } else {
            cpuCoreCount = numberOfCores;
        }
        return cpuCoreCount;
    }

    @Override
    public String getOS() {
        return System.getProperty("os.name").toLowerCase();
    }

    @Override
    public int threadCapacity() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override
    public CiotolaDirector getDirector() {
        return director;
    }

    @Override
    public List<ServiceCtx> getServiceCtxList() {
        return null;
    }

    @Override
    public void registerFactory(ServiceCtxFactory factory) {

    }

    @Override
    public void resolveServices(Object objectScan) {

    }
}
