package pt.isec.PD.RMI;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
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

        public static void main(String[] args) {
            try {
                if (args.length < 2)
                    System.exit(-1);

                String objectUrl = "rmi://" + args[0] + "/server-bd-pd";
                GetRemoteBDServiceInterface getRemoteBDService = (GetRemoteBDServiceInterface) Naming.lookup(objectUrl);

                System.setProperty("java.rmi.server.hostname", args[1]);

                GetRemoteBDObserver observer = new GetRemoteBDObserver();
                System.out.println("Observer registado no servidor RMI");

                getRemoteBDService.addObserver(observer);

                System.out.println("<Enter> para terminar...");
                System.in.read();

                getRemoteBDService.removeObserver(observer);
                UnicastRemoteObject.unexportObject(observer, true);
            } catch (MalformedURLException e) {
                System.out.println("Erro a obter o URL do objeto remoto");
            } catch (NotBoundException e) {
                System.out.println("Erro a obter a referencia para o objeto remoto");
            } catch (RemoteException e) {
                System.out.println("Erro de comunicação com o objeto remoto");
            } catch (IOException e) {
                System.out.println("Erro de E/S");
            }
        }
}
