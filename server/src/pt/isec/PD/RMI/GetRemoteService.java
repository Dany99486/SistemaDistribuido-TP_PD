package pt.isec.PD.RMI;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GetRemoteService extends UnicastRemoteObject implements GetRemoteBDServiceInterface {
    public static final int MAX_CHUNCK_SIZE = 10000;
    protected File localDirectory;
    List<GetRemoteBDObserverInterface> observers = new ArrayList<>();

    public GetRemoteService(File localDirectory) throws RemoteException {
        this.localDirectory = localDirectory;
    }

    protected FileInputStream getRequestedBDFileInputStream(String fileName) throws IOException {
        String requestedCanonicalFilePath;
        fileName = fileName.trim();

        requestedCanonicalFilePath = new File(localDirectory+File.separator+fileName).getCanonicalPath();

        if(!requestedCanonicalFilePath.startsWith(localDirectory.getCanonicalPath()+File.separator)){
            System.out.println("Não é permitido aceder ao ficheiro " + requestedCanonicalFilePath + "!");
            System.out.println("A directoria de base não corresponde a " + localDirectory.getCanonicalPath()+"!");
            throw new AccessDeniedException(fileName);
        }

        return new FileInputStream(requestedCanonicalFilePath);
    }

    @Override
    public byte[] getFileChunk(String fileName, long offset) throws RemoteException, IOException {
        byte[] fileChunk = new byte[MAX_CHUNCK_SIZE];
        int nbytes;

        fileName = fileName.trim();

        try(FileInputStream requestedFileInputStream = getRequestedBDFileInputStream(fileName)){
            requestedFileInputStream.skip(offset);
            nbytes = requestedFileInputStream.read(fileChunk);

            if (nbytes == -1)
                return null;

            if (nbytes < fileChunk.length)
                return Arrays.copyOf(fileChunk, nbytes);

            return fileChunk;
        } catch (IOException e) {
            System.out.println("Ocorreu a excepção de E/S: \n\t" + e);
            throw new IOException(fileName, e.getCause());
        }
    }

    @Override
    public void getFile(String fileName) throws IOException {
        /*byte [] fileChunk = new byte[MAX_CHUNCK_SIZE];
        int nbytes;

        fileName = fileName.trim();
        System.out.println("Recebido pedido para: " + fileName);

        try(FileInputStream requestedFileInputStream = getRequestedBDFileInputStream(fileName)){
            while((nbytes = requestedFileInputStream.read(fileChunk)) != -1){
                backupRef.writeFileChunk(fileChunk, nbytes);
            }

            System.out.println("Ficheiro " + new File(localDirectory+File.separator+fileName).getCanonicalPath() +
                    " transferido para o cliente com sucesso.");
            notifyObservers("Ficheiro " + new File(localDirectory+File.separator+fileName).getCanonicalPath() +
                    " transferido para o cliente com sucesso.");
            System.out.println();

        }catch(FileNotFoundException e){   //Subclasse de IOException
            System.out.println("Ocorreu a exceção {" + e + "} ao tentar abrir o ficheiro!");
            throw new FileNotFoundException(fileName);
        }catch(IOException e){
            System.out.println("Ocorreu a exceção de E/S: \n\t" + e);
            throw new IOException(fileName, e.getCause());
        }*/
    }

    @Override
    public void addObserver(GetRemoteBDObserverInterface observer) throws RemoteException {
        synchronized (observers) {
            System.out.println("Adicionando observador");
            if (!observers.contains(observer)) {
                observers.add(observer);
                System.out.println("Existe mais um observador");
            }
        }
    }

    @Override
    public void removeObserver(GetRemoteBDObserverInterface observer) throws RemoteException {
        synchronized (observers) {
            if (observers.contains(observer)) {
                observers.remove(observer);
                System.out.println("Existe menos um observador");
            }
        }
    }

    public void notifyObservers(String msg) {
        System.out.println("Notificando observadores...");
        List<GetRemoteBDObserverInterface> observersToRemove = new ArrayList<>();

        for (GetRemoteBDObserverInterface observer : observers) {
            try {
                observer.notifyNewOperationConcluded(msg);
            } catch (RemoteException ex) {
                observersToRemove.add(observer);
                System.out.println("Menos um observador (observador inacessivel)");
            }
        }
        synchronized (observers){
            observers.removeAll(observersToRemove);
        }
    }
}


