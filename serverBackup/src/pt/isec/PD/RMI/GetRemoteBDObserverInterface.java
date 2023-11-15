package pt.isec.PD.RMI;

import java.io.IOException;
import java.rmi.RemoteException;

public interface GetRemoteBDObserverInterface extends java.rmi.Remote {
    void notifyNewOperationConcluded(String msg) throws java.rmi.RemoteException;
    void writeFileChunk(byte[] fileChunk, int nbytes) throws RemoteException, IOException;

}
