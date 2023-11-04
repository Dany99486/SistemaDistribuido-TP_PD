package pt.isec.PD.ServerBackup;

import java.io.Serializable;

public class Heartbeat implements Serializable {
    private static final long serialVersionUID = 1L;
    private String registryName;
    private int registryPort;
    private int databaseVersion;

    public Heartbeat(String registryName, int registryPort, int databaseVersion) {
        this.registryName = registryName;
        this.registryPort = registryPort;
        this.databaseVersion = databaseVersion;
    }

    public String getRegistryName() {
        return registryName;
    }

    public int getRegistryPort() {
        return registryPort;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }
}