package pt.isec.PD.Server;

import java.io.File;
import java.sql.*;
import java.util.Calendar;

public class Evento {
    private String show;

    //TODO: Regista um evento
    public int criaEvento(String nome, String local, int horaInicio, int horaFim, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int registed = 0;

        try {
            show = url;
            show += "\nConectando à base de dados...";

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return -1;
            }

            int hora = Calendar.HOUR_OF_DAY;
            int minuto = Calendar.MINUTE;
            int segundo = Calendar.SECOND;
            int dia = Calendar.DAY_OF_MONTH;
            int mes = Calendar.MONTH;
            int ano = Calendar.YEAR;

            String data_realizada = hora + ":" + minuto + ":" + segundo + " de " + dia + " de " + mes + " de " + ano;

            String query = "INSERT INTO eventos (nome,local,data,hora_inicio,hora_fim) VALUES (?,?,?,?,?);";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, nome);
            preparedStatement.setString(2, local);
            preparedStatement.setString(3, data_realizada);
            preparedStatement.setString(4, String.valueOf(horaInicio));
            preparedStatement.setString(5, String.valueOf(horaFim));
            int resultSet = preparedStatement.executeUpdate();

            if (resultSet > 0)
                registed = 1;

            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            registed = -2;
        }
        return registed;
    }

    //TODO: Edição de um evento
    public int editaEvento(String coluna, String alteracao, String nome, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        int registed = 0;

        try {
            show = url;
            show += "\nConectando à base de dados...";

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return -1;
            }

            //Falta adicionar: Só pode ser alterado se nao existir presenças
            String query = "UPDATE eventos SET '"+coluna+"'='"+alteracao+"' WHERE nome='"+nome+"';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);

            int resultSet = preparedStatement.executeUpdate();

            if (resultSet > 0)
                registed = 1;

            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            registed = -2;
        }
        return registed;
    }

    //TODO: Elimina um evento se não existirem presenças
    public int eliminaEvento(String nome, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        int registed = 0;

        try {
            show = url;
            show += "\nConectando à base de dados...";

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return -1;
            }

            String query = "SELECT eventos.nome FROM eventos " +
                    "LEFT JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                    "WHERE eventos.nome = '"+nome+"';";
            System.out.println(query);
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {
                connection.close();
                return registed;
            }

            query = "DELETE FROM eventos WHERE nome='"+nome+"';";
            preparedStatement = connection.prepareStatement(query);

            int resultSet = preparedStatement.executeUpdate();

            if (resultSet > 0)
                registed = 1;

            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            registed = -2;
        }
        return registed;
    }

    //TODO: Seleciona um evento atraves de um email de um utilizador
    public String consultaEvento(String email, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = null;

        try {
            show = url;
            show += "\nConectando à base de dados...";

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return resultado.append("Erro de conexão com a base de dados").toString();
            }

            String query = "SELECT * FROM eventos " +
                    "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                    "JOIN utilizadores ON eventos.idCC = utilizadores.cartaoCidadao " +
                    "WHERE utilizadores.email = '"+email+"';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String nome = result.getString("nome");
                String local = result.getString("local");
                String data = result.getString("data");
                String hora_inicio = result.getString("hora_inicio");
                String hora_fim = result.getString("hora_fim");
                resultado.append("Nome: ").append(nome).append(" Local: ").append(local)
                        .append(" Data: ").append(data).append(" Hora de inicio: ").append(hora_inicio)
                        .append(" Hora de fim: ").append(hora_fim).append("\n");
            }

            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        return resultado.toString();
    }

    //TODO: Seleciona um evento com filtro
    public String consultaEventoFiltro(String campo, String filtro, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = null;

        try {
            show = url;
            show += "\nConectando à base de dados...";

            Connection connection = DriverManager.getConnection(url);
            if (connection != null)
                show += "\nConexão com a base de dados estabelecida com sucesso.";
            else {
                show += "\nConexão com a base de dados não foi estabelecida.";
                return resultado.append("Erro de conexão com a base de dados").toString();
            }

            String query = "SELECT * FROM eventos " +
                    "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                    "WHERE eventos.'"+campo+"' = '"+filtro+"';";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet result = preparedStatement.executeQuery();

            while (result.next()) {
                String nome = result.getString("nome");
                String local = result.getString("local");
                String data = result.getString("data");
                String hora_inicio = result.getString("hora_inicio");
                String hora_fim = result.getString("hora_fim");
                resultado.append("Nome: ").append(nome).append(" Local: ").append(local)
                        .append(" Data: ").append(data).append(" Hora de inicio: ").append(hora_inicio)
                        .append(" Hora de fim: ").append(hora_fim).append("\n");
            }

            connection.close();
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        return resultado.toString();
    }
}
