package pt.isec.PD.Server;

import java.io.IOException;
import java.net.Socket;
import java.sql.*;
import java.util.List;

public class Clients {
    static final int TIMEOUT = 10000; //10 seconds
    static final int TABLE_ENTRY_TIMEOUT = 20000; //20 seconds
    static final String GET_CLIENTS_QUERY = "SELECT * FROM UTILIZADORES";

    private static int getClients(String bdName, List<Socket> clients) {
        String show;
        String clientName;
        int clientPort;
        Socket socketToClient;

        clients.clear();

        String dbURL = "link da base de dados/" + bdName;

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT CURRENT_TIMESTAMP");
            Timestamp currentTimestampInServer = rs.next() ? rs.getTimestamp("current_timestamp"):null;
            rs = stmt.executeQuery(GET_CLIENTS_QUERY);

            while (rs.next()) {
                try {
                    clientName = rs.getString("endereco"); //Adicionar na BD
                    clientPort = rs.getInt("port"); //Adicionar na BD

                    Timestamp timestamp = rs.getTimestamp("timestamp");
                    long elapsedTime = currentTimestampInServer.getTime() - timestamp.getTime();

                    if (elapsedTime > TABLE_ENTRY_TIMEOUT) {
                        //Faz o que tem a fz, exemplo de remover da tabela
                        try (Statement stmt2 = conn.createStatement()) {
                            stmt2.executeUpdate("DELETE FROM UTILIZADORES WHERE ENDERECO = '" + clientName + "' AND PORT = " + clientPort + ";");
                        }
                        continue;
                    }
                    socketToClient = new Socket(clientName, clientPort);
                    socketToClient.setSoTimeout(TIMEOUT);
                    clients.add(socketToClient);

                } catch (IOException e) {
                    show = "Não foi possivel realizar a conexão com o host!\n" + e + "\n";
                }
            }
        } catch (SQLException e) {
            show = e.toString();
        }
        return clients.size();
    }
}
