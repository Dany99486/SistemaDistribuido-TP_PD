package pt.isec.PD.RMI;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GetRemoteBDObserver extends UnicastRemoteObject implements GetRemoteBDObserverInterface {
        public GetRemoteBDObserver() throws RemoteException {
            super();
        }

        @Override
        public void notifyNewOperationConcluded(String msg) throws RemoteException {
            System.out.println("->" + msg);
            System.out.println();
        }

}
