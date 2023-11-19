package pt.isec.PD.RMI;

import pt.isec.PD.Model.BD;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;

public class GetRemoteBDObserver extends UnicastRemoteObject implements GetRemoteBDObserverInterface {
        FileOutputStream fout = null;
        private final String localFilePath;
        public GetRemoteBDObserver(String localFilePath) throws RemoteException {
            super();
            this.localFilePath = localFilePath;
        }

        @Override
        public void notifyNewOperationConcluded(String msg) throws RemoteException {
            System.out.println("->" + msg + "\n");
            updateBD(msg);
            new BD(localFilePath).incVersion();
        }
        private void updateBD(String query){
            String url = "jdbc:sqlite:"+localFilePath;
            try{
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    System.out.println("Conexão com a base de dados estabelecida.");
                else {
                    System.out.println("Conexão com a base de dados não foi estabelecida.");
                }

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                System.out.println("Executando query: " + query);
                int resultSet = preparedStatement.executeUpdate();

                if (resultSet <= 0)
                    System.out.println("Erro ao atualizar a BD");

                connection.close();

            } catch (SQLException e) {
                System.out.println("Erro ao atualizar a BD: " + e.getMessage());
            }
        }
        public synchronized void setFout(FileOutputStream fout) {
            this.fout = fout;

        }
        public synchronized void closeFout() {
            try {
                fout.close();
            } catch (IOException e) {
                System.out.println("Erro ao fechar o ficheiro!");
            }

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
