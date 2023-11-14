package pt.isec.PD.RMI;

import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GetRemoteBDBackupService extends UnicastRemoteObject implements GetRemoteBDBackupInterface {
    FileOutputStream fout = null;

    protected GetRemoteBDBackupService() throws RemoteException {
        super();
    }

    public synchronized void setFout(FileOutputStream fout) {
        this.fout = fout;
    }


    @Override
    public void writeFileChunk(byte[] fileChunk, int nbytes) throws RemoteException, IOException {
        if (fout==null){
            System.out.println("Não existe o ficheiro aberto para escrita!");
            throw new IOException("<CLI> Nao existe ficheiro aberto para escrita!");
        }
        try {
            fout.write(fileChunk, 0, nbytes);
        } catch (IOException ex) {
            System.out.println("Erro ao escrever no ficheiro!");
            throw new IOException("<Backup> Erro ao escrever no ficheiro", ex.getCause());
        }
    }
}
