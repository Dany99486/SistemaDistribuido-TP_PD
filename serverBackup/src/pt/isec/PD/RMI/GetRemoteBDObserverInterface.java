package pt.isec.PD.RMI;

public interface GetRemoteBDObserverInterface extends java.rmi.Remote {
    void notifyNewOperationConcluded(String msg) throws java.rmi.RemoteException;
}
