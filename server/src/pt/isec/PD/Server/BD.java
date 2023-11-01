package pt.isec.PD.Server;

import java.io.File;
import java.sql.*;

public class BD {
    private String show;
    //TODO: Criar base de dados se nao existir
    public String createBDIfNotExists(String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;

        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            show = url;
            show += "\nConectando à base de dados...";
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return show;
            }
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS presencas (" +
                    "id INTEGER PRIMARY KEY, " +
                    "idEvento INTEGER REFERENCES eventos (idEvento), " +
                    "idCC TEXT REFERENCES utilizadores (cartaoCidadao)" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS utilizadores (" +
                    "nome TEXT, " +
                    "cartaoCidadao TEXT PRIMARY KEY, " +
                    "email TEXT UNIQUE, " +
                    "pass TEXT, " +
                    "role TEXT NOT NULL" +
                    ")");
            statement.execute("CREATE TABLE IF NOT EXISTS eventos (" +
                    "idEvento INTEGER PRIMARY KEY, " +
                    "nome TEXT, " +
                    "local TEXT, " +
                    "data TEXT, " +
                    "hora_inicio TEXT, " +
                    "hora_fim TEXT" +
                    ")");
            show += "\nTabelas criadas com sucesso.";

            String query = "SELECT * FROM utilizadores";
            ResultSet resultSet = statement.executeQuery(query);

            StringBuilder stringBuilder = new StringBuilder();
            // Exibe os resultados no console
            while (resultSet.next()) {
                String nome = resultSet.getString("nome");
                String cartaoCidadao = resultSet.getString("cartaoCidadao");
                String email = resultSet.getString("email");
                String pass = resultSet.getString("pass");
                String role = resultSet.getString("role");
                stringBuilder.append("Nome: ").append(nome).append(", Cartão de Cidadão: ").append(cartaoCidadao).append(", Email: ").append(email).append(", Senha: ").append(pass).append(", Função: ").append(role);
                stringBuilder.append("\n");
            }
            show += stringBuilder.toString();
            connection.close();

        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
        }
        return show;
    }

    //TODO: Verifica se utilizador existe
    public boolean checkClientIfExists(String user, String pass, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        boolean exist = false;
        
        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            show = url;
            show += "\nConectando à base de dados...";
            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
            }
            Statement statement = connection.createStatement();
            String query = "SELECT nome, pass FROM utilizadores WHERE nome = '" + user + "' AND pass = '" + pass + "'";
            ResultSet resultSet = statement.executeQuery(query);
            
            exist = resultSet.next();
            
            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
        }
        return exist;
    }
}
