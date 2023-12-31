package pt.isec.PD.Model;

import java.io.File;
import java.sql.*;

public class BD {
    private final String ADMIN = "admin";
    private final String USER = "user";
    private String show;
    private String role;
    private String cc;
    private final Server.SharedDatabaseLock lock;

    public BD(Server.SharedDatabaseLock lock) {
        this.lock = lock;
    }


    public String getRole() {
        return role;
    }

    public String getCC() {
        return cc;
    }

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
            statement.execute("CREATE TABLE IF NOT EXISTS versao (" +
                    "id INTEGER PRIMARY KEY, " +
                    "versao INTEGER " +
                    ")");
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
                    "hora_fim TEXT," +
                    "codigo INTEGER," +
                    "code_validade TEXT" +
                    ")");
            show += "\nTabelas criadas com sucesso.";

            String query = "SELECT * FROM versao ORDER BY versao DESC;";
            ResultSet resultSet = statement.executeQuery(query);
            if (resultSet.next())
                show += "\nVersão da base de dados: " + resultSet.getInt("versao");
            else {
                query = "INSERT INTO versao (versao) VALUES (0);";
                boolean v = statement.execute(query);
                if (v)
                    show += "\nVersão da base de dados: 0";
                else
                    show += "\nVersão da base de dados: 0 não inserida";
            }

            query = "SELECT * FROM utilizadores WHERE role='" + ADMIN + "';";
            resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                query = "INSERT INTO utilizadores (nome, pass,cartaoCidadao,email,role) VALUES ('admin','123','admin','admin@isec.pt','admin');";
                boolean v = statement.execute(query);
                if (v)
                    show += "\nUtilizador admin criado com sucesso.";
                else
                    show += "\nUtilizador admin não criado.";
            }

            /*
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
             */
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
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return false;
                }
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM utilizadores WHERE email='" + user + "' AND pass='" + pass + "';";
                System.out.println(query);
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    cc = resultSet.getString("cartaoCidadao");
                    role = resultSet.getString("role");
                    exist = true;
                }

                connection.close();
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
        }

        return exist;
    }

    //TODO: Regista utilizador se nao existir
    public int registClient(String user, String passe, String cc, String email, String[] args, String BDFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int registed = 0;

        try {
            show = url;
            show += "\nConectando à base de dados...";
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return -1;
                }
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM utilizadores WHERE email='" + email + "';";
                ResultSet resultSetS = statement.executeQuery(query);
                if (resultSetS.next())
                    return registed;

                query = "INSERT OR IGNORE INTO utilizadores (nome,pass,cartaoCidadao,email,role) VALUES (?,?,?,?,?);";
                queryArray[0] = query;
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, user);
                preparedStatement.setString(2, passe);
                preparedStatement.setString(3, cc);
                preparedStatement.setString(4, email);
                preparedStatement.setString(5, USER);
                int resultSet = preparedStatement.executeUpdate();

                if (resultSet > 0)
                    registed = 1;

                connection.close();
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            registed = -2;
        }
        return registed;
    }

    public synchronized int editClient(String coluna, String alteracao, String cartaoCC, String[] args, String bdFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        System.out.println(url);
        int registed = 0;

        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return -1;
                    }

                    String query = "UPDATE utilizadores SET '" + coluna + "'='" + alteracao + "' WHERE cartaoCidadao='" + cartaoCC + "';";
                    queryArray[0] = query;
                    System.out.println(query);
                    PreparedStatement preparedStatement = connection.prepareStatement(query);

                    int resultSet = preparedStatement.executeUpdate();

                    if (resultSet > 0)
                        registed = 1;

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            registed = -2;
        }
        return registed;
    }


    //TODO: Devolve os dados do cliente
    public String devolveDados(String x, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        String aux = null;

        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            show = url;
            show += "\nConectando à base de dados...";
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return null;
                }
                Statement statement = connection.createStatement();
                String query = "SELECT * FROM utilizadores WHERE cartaoCidadao='" + x + "';";
                System.out.println(query);
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    aux = resultSet.getString("nome") + " " + resultSet.getString("email") + " " + resultSet.getString("pass");
                    System.out.println(aux);
                }
                connection.close();
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
        }
        return aux;
    }

    //TODO: Versão de Base de dados, aceder ultima versão
    public int pesquisaUltimaVersaoBD(String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int aux = -1;

        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            show = url;
            show += "\nConectando à base de dados...";
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return -1;
                }
                Statement statement = connection.createStatement();
                String query = "SELECT versao FROM versao ORDER BY versao DESC;";
                System.out.println(query);
                ResultSet resultSet = statement.executeQuery(query);
                if (resultSet.next()) {
                    aux = resultSet.getInt("versao");
                    System.out.println("Versao da BD: " + aux);
                }
                connection.close();
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return aux;
    }

    //TODO: Versão de Base de dados, regista nova versao
    public int insereNovaVersaoBD(int lastVersion, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int aux = -3;

        try {
            // Estabelece a conexão com a base de dados ou cria uma nova se não existir
            show = url;
            show += "\nConectando à base de dados...";
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return -1;
                }
                String query = "UPDATE versao SET versao = ? WHERE id = 1;";
                PreparedStatement preparedStatement = connection.prepareStatement(query);

                lastVersion++;
                preparedStatement.setInt(1, lastVersion);
                int resultSet = preparedStatement.executeUpdate();

                if (resultSet > 0)
                    aux = 0;

                connection.close();
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return aux;
    }
}
