package pt.isec.PD.RMI;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class GetRemoteBDBackup {
    public static void main(String[] args) {
        String objectUrl, fileName, localFilePath;
        File localDirectory;

        GetRemoteBDBackupService myRemoteService = null;
        GetRemoteBDServiceInterface remoteBDService;

        if(args.length != 3){
            System.out.println("Deve passar na linha de comando: (1) a localização do RMI registry"
                    + "registado o servico; (2) a diretoria local onde pretende guardar "
                    + "o ficheiro obtido; e (3) o ficheiro pretendido!");
            return;
        }

        objectUrl = "rmi://"+args[0]+"/server-bd-pd";
        localDirectory = new File(args[1].trim());
        fileName = args[2].trim();
        System.setProperty("java.rmi.server.hostname", "localhost");

        if(!localDirectory.exists()){
            System.out.println("A diretoria " + localDirectory + " não existe!");
            return;
        }

        if(!localDirectory.isDirectory()){
            System.out.println("O caminho " + localDirectory + " não se refere a uma diretoria!");
            return;
        }
        if(!localDirectory.canWrite()){
            System.out.println("Sem permissoes de escrita na diretoria " + localDirectory);
            return;
        }

        try{
            localFilePath = new File(localDirectory.getPath()+File.separator+fileName).getCanonicalPath();
        }catch(IOException ex){
            System.out.println("Erro E/S - " + ex);
            return;
        }

        try(FileOutputStream localFileOutputStream = new FileOutputStream(localFilePath)){

            System.out.println("Ficheiro " + localFilePath + " criado.");
            remoteBDService = (GetRemoteBDServiceInterface) Naming.lookup(objectUrl);

            /*
             * Lanca o servico local para acesso remoto por parte do servidor.
             */
            myRemoteService = new GetRemoteBDBackupService();

            /*
             * Passa ao servico RMI LOCAL uma referencia para o objecto localFileOutputStream.
             */
            myRemoteService.setFout(localFileOutputStream);

            /*
             * Obtem o ficheiro pretendido, invocando o metodo getFile no servico remoto.
             */
            remoteBDService.getFile(fileName, myRemoteService);

        }catch(RemoteException e){
            System.out.println("Erro remoto - " + e);
        }catch(NotBoundException e){
            System.out.println("Serviço remoto desconhecido - " + e);
        }catch(IOException e){
            System.out.println("Erro E/S - " + e);
        }catch(Exception e){
            System.out.println("Erro - " + e);
        }finally{
            if(myRemoteService != null){
                /*
                 * Retira do servico local a referencia para o objecto localFileOutputStream.
                 */
                myRemoteService.setFout(null);
                /*
                 * Termina o servi�o local.
                 */
                try{
                    UnicastRemoteObject.unexportObject(myRemoteService, true);
                }catch(NoSuchObjectException e){}
            }
        }
    }
}
