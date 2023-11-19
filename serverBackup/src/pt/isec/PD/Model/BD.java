package pt.isec.PD.Model;

import java.sql.*;

public class BD {

    private final String localFilePath;

    public BD(String localFilePath) {
        this.localFilePath = localFilePath;
    }
    public boolean checkVersion(int databaseVersion) {
        String url = "jdbc:sqlite:" + localFilePath;

        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("Conexão com a base de dados estabelecida.");
            else {
                System.out.println("Conexão com a base de dados não foi estabelecida.");
            }

            Statement statement = connection.createStatement();
            String query = "SELECT versao FROM versao ORDER BY versao DESC;";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                //System.out.println("DataBase Version (local): " + resultSet.getInt("versao"));
                if (resultSet.getInt("versao") == databaseVersion) {
                    //System.out.println("DataBase Version (remote = local): " + databaseVersion);
                    connection.close();
                    return true;
                }
            }
            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao ler a BD: " + e.getMessage());
        }
        return false;
    }

    public boolean incVersion() {
        String url = "jdbc:sqlite:" + localFilePath;
        boolean aux = false;

        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("Conexão com a base de dados estabelecida.");
            else {
                System.out.println("Conexão com a base de dados não foi estabelecida.");
            }

            String query = "SELECT versao FROM versao ORDER BY versao DESC;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery(query);

            if (resultSet.next()) {
                int databaseVersion = resultSet.getInt("versao");

                String query2 = "INSERT INTO versao (versao) VALUES (?);";
                preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setInt(1, databaseVersion + 1);

                int result = preparedStatement.executeUpdate();
                if (result > 0)
                    aux = true;
            }

            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao ler a BD: " + e.getMessage());
        }
        return aux;
    }
}
