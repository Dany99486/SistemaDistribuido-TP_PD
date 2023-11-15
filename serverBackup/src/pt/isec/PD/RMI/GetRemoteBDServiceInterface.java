package pt.isec.PD.RMI;

public interface GetRemoteBDServiceInterface extends java.rmi.Remote {

    public byte [] getFileChunk(String fileName, long offset) throws java.rmi.RemoteException, java.io.IOException;

    void getFile(String fileName, GetRemoteBDObserverInterface cliRef) throws java.rmi.RemoteException,java.io.IOException;

    public void addObserver(GetRemoteBDObserverInterface observer) throws java.rmi.RemoteException;

    public void removeObserver(GetRemoteBDObserverInterface observer) throws java.rmi.RemoteException;
}
