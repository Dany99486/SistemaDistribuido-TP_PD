package pt.isec.PD.Model;

import java.sql.*;

public class BD {


    public static boolean checkVersion(int databaseVersion,String localFilePath) {
        String url = "jdbc:sqlite:" + localFilePath;

        try {
            Connection connection = DriverManager.getConnection(url);
           if (connection == null)
               return false;

            Statement statement = connection.createStatement();
            String query = "SELECT versao FROM versao WHERE id=1;";

            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next()) {
                int versao = resultSet.getInt("versao");
                System.out.println("------------versao: " + versao+" databaseVersion: "+databaseVersion);
                if (versao == databaseVersion) {
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

    public static boolean incVersion(int databaseVersion,String localFilePath) {
        String url = "jdbc:sqlite:" + localFilePath;
        boolean aux = false;

        try {
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                System.out.println("Conexão com a base de dados estabelecida.");
            else {
                System.out.println("Conexão com a base de dados não foi estabelecida.");
                System.out.println("Exiting...");
                System.exit(-1);
            }
            System.out.println("AQUUUUUUUUUUUUUUUUUIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIIII");



                System.out.println("atualizing DataBase Version (local) to: " + databaseVersion);
                String query2 = "UPDATE versao SET versao = ? WHERE id=1;";
                PreparedStatement preparedStatement = connection.prepareStatement(query2);
                preparedStatement.setInt(1, databaseVersion);

                int result = preparedStatement.executeUpdate();
                if (result > 0)
                    aux = true;


            connection.close();
        } catch (SQLException e) {
            System.out.println("Erro ao ler a BD: " + e.getMessage());
        }
        return aux;
    }
}
