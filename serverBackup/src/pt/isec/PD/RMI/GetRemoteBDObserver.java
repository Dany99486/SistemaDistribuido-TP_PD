package pt.isec.PD.RMI;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GetRemoteBDObserver extends UnicastRemoteObject implements GetRemoteBDObserverInterface {
        FileOutputStream fout = null;
        public GetRemoteBDObserver() throws RemoteException {
            super();
        }

        @Override
        public void notifyNewOperationConcluded(String msg) throws RemoteException {
            System.out.println("->" + msg + "\n");
        }
        public synchronized void setFout(FileOutputStream fout) {
            this.fout = fout;
        }
    @Override
    public void writeFileChunk(byte[] fileChunk, int nbytes) throws RemoteException, IOException {
        if (fout==null){
            System.out.println("Nao existe ficheiro aberto para escrita!");
            throw new IOException("<SERV> Nao existe ficheiro aberto para escrita!");
        }
        try {
            fout.write(fileChunk, 0, nbytes);
        } catch (IOException ex) {
            System.out.println("Erro ao escrever no ficheiro!");
            throw new IOException("<SERV> Erro ao escrever no ficheiro", ex.getCause());
        }
    }


}
