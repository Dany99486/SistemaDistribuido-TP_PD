package pt.isec.PD.RMI;

public interface GetRemoteBDBackupInterface extends java.rmi.Remote {
    void writeFileChunk(byte[] fileChunk, int nbytes) throws java.rmi.RemoteException, java.io.IOException;
}
