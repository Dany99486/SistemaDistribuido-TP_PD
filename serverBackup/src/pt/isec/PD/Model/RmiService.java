package pt.isec.PD.Model;

import pt.isec.PD.RMI.GetRemoteBDObserver;
import pt.isec.PD.RMI.GetRemoteBDServiceInterface;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RmiService extends Thread {
    private final String rmiRegistryName;
    private final int rmiRegistryPort;
    private final String localFilePath;
    private int databaseVersion;
    GetRemoteBDObserver observer;
    GetRemoteBDServiceInterface getRemoteFileService;

    public RmiService(String rmiRegistryName, int rmiRegistryPort, String localFilePath, int databaseVersion) {
        this.rmiRegistryName = rmiRegistryName;
        this.rmiRegistryPort = rmiRegistryPort;
        this.localFilePath = localFilePath;
        this.databaseVersion = databaseVersion;
        System.out.println("filepath:"+localFilePath);
    }

    @Override
    public void run() {
        String filePath;
        try{
            filePath = new File(localFilePath).getCanonicalPath();
        }catch(IOException ex){
            System.out.println("Erro E/S - " + ex);
            return;
        }
        try(FileOutputStream localFileOutputStream = new FileOutputStream(filePath)) {

            System.out.println("Launching RMI Receiver");
            String objectUrl = "rmi://localhost:"+rmiRegistryPort+"/"+rmiRegistryName;
            getRemoteFileService = (GetRemoteBDServiceInterface) Naming.lookup(objectUrl);

            System.setProperty("java.rmi.server.hostname", "localhost");

            System.out.println("RMI Receiver launched");
            observer = new GetRemoteBDObserver(localFilePath);
            observer.setFout(localFileOutputStream);
            System.out.println("Observer registado no servidor RMI");
            getRemoteFileService.getFile("serverdatabase.db", observer);
            getRemoteFileService.addObserver(observer);

            observer.closeFout();

            //while (new BD(localFilePath).checkVersion(databaseVersion));
            do {
            }while (true);
            //getRemoteFileService.removeObserver(observer);
            //UnicastRemoteObject.unexportObject(observer, true);

        } catch (MalformedURLException e) {
            System.out.println("Erro a obter o URL do objecto remoto");
        } catch (NotBoundException e) {
            System.out.println("Erro a obter a referencia para o objecto remoto");
        } catch (RemoteException e) {
            System.out.println("Erro de comunicacao com o objecto remoto");
        } catch (IOException e) {
            System.out.println("Erro de E/S");
        }
    }
    public void endService(){
        try {
            getRemoteFileService.removeObserver(observer);
            UnicastRemoteObject.unexportObject(observer, true);
            System.exit(0);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
        System.exit(0);
    }

}
