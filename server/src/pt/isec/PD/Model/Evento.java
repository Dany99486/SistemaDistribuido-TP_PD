package pt.isec.PD.Model;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

public class Evento {
    private String canocialPath;
    private String show;
    private final Server.SharedDatabaseLock lock;
    private String msg;

    public String getMsg() {
        return msg;
    }

    public void cleanMsg() {
        this.msg = null;
    }

    public Evento(Server.SharedDatabaseLock lock) {
        this.lock = lock;
    }


    //TODO: Regista um evento
    public  int criaEvento(String nome, String data_inicio, String data_fim, String local, String horaInicio, String horaFim, String[] args, String BDFileName, String[] queryArray) {
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
                /*
                int hora = Calendar.HOUR_OF_DAY;
                int minuto = Calendar.MINUTE;
                int segundo = Calendar.SECOND;
                int dia = Calendar.DAY_OF_MONTH;
                int mes = Calendar.MONTH;
                int ano = Calendar.YEAR;*/

                    String[] data = data_inicio.trim().split("/");
                    int diaInicio = Integer.parseInt(data[0]);
                    int mesInicio = Integer.parseInt(data[1]);
                    int anoInicio = Integer.parseInt(data[2]);
                    data = data_fim.trim().split("/");
                    int diaFim = Integer.parseInt(data[0]);
                    int mesFim = Integer.parseInt(data[1]);
                    int anoFim = Integer.parseInt(data[2]);

                    String[] aux = horaInicio.trim().split(":");
                    int horaI = Integer.parseInt(aux[0]);
                    int minutoI = Integer.parseInt(aux[1]);
                    aux = horaFim.trim().split(":");
                    int horaF = Integer.parseInt(aux[0]);
                    int minutoF = Integer.parseInt(aux[1]);

                    //Converter hora para minutos
                    int horaBegin = horaI * 60 + minutoI;
                    int horaEnd = horaF * 60 + minutoF;

                    // Obter a data e hora atuais
                    LocalDateTime now = LocalDateTime.now();

                    // Converter datas e horas de início e fim do evento para LocalDateTime
                    LocalDateTime inicioEvento = LocalDateTime.of(
                            Integer.parseInt(data_inicio.split("/")[2]), // ano
                            Integer.parseInt(data_inicio.split("/")[1]), // mês
                            Integer.parseInt(data_inicio.split("/")[0]), // dia
                            Integer.parseInt(horaInicio.split(":")[0]), // hora
                            Integer.parseInt(horaInicio.split(":")[1])  // minuto
                    );

                    LocalDateTime fimEvento = LocalDateTime.of(
                            Integer.parseInt(data_fim.split("/")[2]),    // ano
                            Integer.parseInt(data_fim.split("/")[1]),    // mês
                            Integer.parseInt(data_fim.split("/")[0]),    // dia
                            Integer.parseInt(horaFim.split(":")[0]),     // hora
                            Integer.parseInt(horaFim.split(":")[1])      // minuto
                    );

                    // Calcular a diferença em horas
                    long diferencaEmHoras = ChronoUnit.HOURS.between(now, fimEvento);

                    // Se a diferença for negativa, o evento já ocorreu
                    // Caso contrário, a diferença representa o tempo total do evento em horas
                    int validade = (int) (diferencaEmHoras < 0 ? diferencaEmHoras : ChronoUnit.HOURS.between(inicioEvento, fimEvento));

                    //String data_realizada = hora + ":" + minuto + ":" + segundo + " de " + dia + " de " + mes + " de " + ano;

                    String data_realizada = diaInicio + "/" + mesInicio + "/" + anoInicio + " - " + diaFim + "/" + mesFim + "/" + anoFim;

                    String query = "INSERT INTO eventos (nome,local,data,hora_inicio,hora_fim,code_validade) VALUES (?,?,?,?,?,?);";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, nome);
                    preparedStatement.setString(2, local);
                    preparedStatement.setString(3, data_realizada);
                    preparedStatement.setString(4, horaInicio);
                    preparedStatement.setString(5, horaFim);
                    preparedStatement.setString(6, String.valueOf(validade));
                    int resultSet = preparedStatement.executeUpdate();

                    queryArray[0] = query;

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

    //TODO: Edição de um evento
    public int editaEvento(String coluna, String alteracao, String nome, String[] args, String bdFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
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

                    String query = "SELECT eventos.nome FROM eventos " +
                            "LEFT JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "WHERE eventos.nome = '" + nome + "' AND eventos.codigo IS NULL;";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next()) {
                        connection.close();
                        return registed;
                    }

                    query = "UPDATE eventos SET '" + coluna + "' = '" + alteracao + "' " +
                            "WHERE eventos.nome = '" + nome + "' AND eventos.codigo IS NULL;";

                    preparedStatement = connection.prepareStatement(query);
                    int resultSet = preparedStatement.executeUpdate();
                    queryArray[0] = query;

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

    //TODO: Elimina um evento se não existirem presenças
    public  int eliminaEvento(String nome, String[] args, String bdFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
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

                    String query = "SELECT eventos.nome FROM eventos " +
                            "LEFT JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "WHERE eventos.nome = '" + nome + "' AND eventos.codigo IS NULL;";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next()) {
                        connection.close();
                        return registed;
                    }

                    query = "DELETE FROM eventos WHERE nome='" + nome + "';";
                    queryArray[0] = query;
                    preparedStatement = connection.prepareStatement(query);

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

    //TODO: Seleciona um evento atraves de um email de um utilizador
    public  String consultaEventoEmail(String email, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = new StringBuilder();

        try {
            show = url;
            show += "\nConectando à base de dados...";
            synchronized (lock) {
                Connection connection = DriverManager.getConnection(url);
                if (connection != null)
                    show += "\nConexão com a base de dados estabelecida com sucesso.";
                else {
                    show += "\nConexão com a base de dados não foi estabelecida.";
                    return resultado.append("Erro de conexão com a base de dados").toString();
                }

                String query = "SELECT * FROM eventos " +
                        "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                        "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                        "WHERE utilizadores.email = '" + email + "';";
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
            }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        System.out.println(resultado.toString());
        return resultado.toString();
    }

    //TODO: Seleciona um evento atraves de um nome
    public String consultaEvento(String name, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = new StringBuilder();
        System.out.println("AQUI");
        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return resultado.append("Erro de conexão com a base de dados").toString();
                    }

                    String query = "SELECT * FROM utilizadores " +
                            "JOIN presencas ON utilizadores.cartaoCidadao = presencas.idCC " +
                            "JOIN eventos ON presencas.idEvento = eventos.idEvento " +
                            "WHERE eventos.nome = '" + name + "';";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    while (result.next()) {
                        String nome = result.getString("nome");
                        String CC = result.getString("cartaoCidadao");
                        String email = result.getString("email");

                        resultado.append("Nome: ").append(nome).append(" CC: ").append(CC)
                                .append(" Email: ").append(email).append("\n");


                    }

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        System.out.println(resultado.toString());
        return resultado.toString();
    }

    //TODO: Seleciona um evento atraves de um email de um utilizador, e com filtro
    public  String consultaPresencasClienteFiltro(String cc, String filtro, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = new StringBuilder();
        System.out.println("AQUI");
        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return resultado.append("Erro de conexão com a base de dados").toString();
                    }

                    String query;
                    if (filtro.isBlank() || filtro.equalsIgnoreCase("sem_filtro")) {
                        query = "SELECT * FROM eventos " +
                                "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                                "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                                "WHERE utilizadores.cartaoCidadao = '" + cc + "';";
                    } else {
                        query = "SELECT eventos." + filtro + " FROM eventos " +
                                "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                                "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                                "WHERE utilizadores.cartaoCidadao='" + cc + "';";
                    }
                    System.out.println(query);
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    while (result.next()) {
                        if (filtro.isBlank() || filtro.equalsIgnoreCase("sem_filtro")) {

                            String nome = result.getString("nome");
                            String local = result.getString("local");
                            String data = result.getString("data");
                            String hora_inicio = result.getString("hora_inicio");
                            String hora_fim = result.getString("hora_fim");
                            resultado.append("Nome: ").append(nome).append(" Local: ").append(local)
                                    .append(" Data: ").append(data).append(" Hora de inicio: ").append(hora_inicio)
                                    .append(" Hora de fim: ").append(hora_fim).append("\n");
                        } else {
                            String aux = result.getString(filtro);
                            resultado.append(filtro).append(": ").append(aux).append("\n");
                        }

                        System.out.println(resultado.toString());
                    }

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        return resultado.toString();
    }

    //TODO: Seleciona um evento com filtro
    public String consultaEventoFiltro(String campo, String filtro, String[] args, String bdFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + bdFileName;
        StringBuilder resultado = new StringBuilder();

        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return resultado.append("Erro de conexão com a base de dados").toString();
                    }

                    String query;
                    if (filtro.equalsIgnoreCase("sem_filtro"))
                        query = "SELECT * FROM eventos;";
                    else
                        query = "SELECT * FROM eventos " +
                            //"JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "WHERE eventos.'" + campo + "' = '" + filtro + "';";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    while (result.next()) {
                        String nome = result.getString("nome");
                        String local = result.getString("local");
                        String data = result.getString("data");
                        String hora_inicio = result.getString("hora_inicio");
                        String hora_fim = result.getString("hora_fim");
                        String codigo = result.getString("codigo");
                        String code_validade = result.getString("code_validade");
                        resultado.append("Nome: ").append(nome).append(" Local: ").append(local)
                                .append(" Data: ").append(data).append(" Hora de inicio: ").append(hora_inicio)
                                .append(" Hora de fim: ").append(hora_fim)
                                .append(" Codigo: ").append(codigo == null ? "Sem codigo" : codigo)
                                .append(" Validade: ").append(code_validade).append("\n");
                    }

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        return resultado.toString();
    }

    //---Presenças---

    //TODO: Regista presenças num evento
    public int inserePresenca(String evento, String email, String[] args, String BDFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int registed = 0;
        System.out.println("entrou");

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

                    int idEvento = 0;
                    String idCC = "";
                    boolean novaPresenca = false;

                    //TODO:======================
                    String query = "SELECT idEvento FROM eventos WHERE nome = '" + evento + "';";


                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next())
                        return registed;
                    idEvento = result.getInt("idEvento");

                    query = "SELECT cartaoCidadao FROM utilizadores WHERE email = '" + email + "';";

                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();


                    if (!result.next())
                        return registed;

                    idCC = result.getString("cartaoCidadao");


                    query = "INSERT INTO presencas (idEvento,idCC) VALUES (?,?);";
                    queryArray[0] = query;
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setInt(1, idEvento);
                    preparedStatement.setString(2, idCC);

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

    //TODO: Elimina presenças de num evento
    public  int eliminaPresenca(String evento, String email, String[] args, String BDFileName, String[] queryArray) {
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

                    int idEvento;
                    String idCC;

                    //TODO:======================
                    String query = "SELECT idEvento FROM eventos WHERE nome = '" + evento + "';";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next())
                        return registed;
                    idEvento = result.getInt("idEvento");

                    query = "SELECT cartaoCidadao FROM utilizadores WHERE email = '" + email + "';";

                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();


                    if (!result.next())
                        return registed;

                    idCC = result.getString("cartaoCidadao");


                    query = "DELETE FROM presencas WHERE idEvento = '" + idEvento + "' AND idCC = '" + idCC + "';";
                    queryArray[0] = query;
                    preparedStatement = connection.prepareStatement(query);
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

    //TODO: Elimina presenças de num evento
    public  int eliminaPresencaID(int id, String[] args, String BDFileName, String[] queryArray) {
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
                /* Não utilizar (deixar em comentario)
                String idCC;

                String query = "SELECT idCC FROM presencas WHERE id = '" + id + "';";

                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet result = preparedStatement.executeQuery();

                if (!result.next())
                    return registed;
                idCC = result.getString("idCC");

                query = "SELECT cartaoCidadao FROM utilizadores WHERE cartaoCidadao = '" + idCC + "';";

                preparedStatement = connection.prepareStatement(query);
                result = preparedStatement.executeQuery();

                if (!result.next())
                    return registed; */

                String query = "DELETE FROM presencas WHERE id = '" + id + "';";
                queryArray[0] = query;
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

    //TODO: Consulta presenças de num evento
    public  String consultaPresenca(String evento, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        System.out.println("eaef");
        StringBuilder resultado = new StringBuilder();

        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return resultado.append("Erro de conexão com a base de dados").toString();
                    }

                    String query;

                    if (evento.equalsIgnoreCase("sem_filtro"))
                        query = "SELECT presencas.id, codigo, eventos.idEvento, cartaoCidadao, hora_inicio, hora_fim FROM eventos " +
                            "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao;";
                    else
                        query = "SELECT presencas.id, codigo, eventos.idEvento, cartaoCidadao, hora_inicio, hora_fim FROM eventos, utilizadores, presencas " +
                                "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                                "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                                "WHERE eventos.nome = '" + evento + "';";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    while (result.next()) {
                        String id = result.getString("id");
                        String codigo = result.getString("codigo");
                        String idEvento = result.getString("idEvento");
                        String cartaoCidadao = result.getString("cartaoCidadao");
                        String hora_inicio = result.getString("hora_inicio");
                        String hora_fim = result.getString("hora_fim");
                        resultado.append("ID: ").append(id).append(" Codigo: ").append(codigo == null ? "Código inválido" : codigo).append(" idEvento: ").append(idEvento)
                                .append(" CC: ").append(cartaoCidadao).append(" Hora de inicio: ").append(hora_inicio)
                                .append(" Hora de fim: ").append(hora_fim).append("\n");
                    }

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            resultado.append("Erro ao conectar à base de dados");
        }
        return resultado.toString();
    }

    //TODO: Gera código de presenças de num evento
    public int geraCodigo(String evento, int validade, String[] args, String BDFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        int codigo;
        cleanMsg();

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

                    String query = "SELECT * FROM eventos WHERE nome = '" + evento + "';";

                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    String dataInicio, dataFim, horaInicio, horaFim;
                    StringBuilder motivo = new StringBuilder();
                    int idEvent = 0;
                    boolean encontrou = false;

                    while (result.next()) {
                        String data = result.getString("data");
                        dataInicio = data.split(" - ")[0];
                        dataFim = data.split(" - ")[1];
                        horaInicio = result.getString("hora_inicio");
                        horaFim = result.getString("hora_fim");
                        idEvent = result.getInt("idEvento");
                        String validadeT = result.getString("code_validade");

                        if (Integer.parseInt(validadeT) > validade) {
                            Calendar now = Calendar.getInstance();
                            String[] dataA = dataInicio.trim().split("/");
                            int diaInicio = Integer.parseInt(dataA[0]);
                            int mesInicio = Integer.parseInt(dataA[1]);
                            int anoInicio = Integer.parseInt(dataA[2]);
                            dataA = dataFim.trim().split("/");
                            int diaFim = Integer.parseInt(dataA[0]);
                            int mesFim = Integer.parseInt(dataA[1]);
                            int anoFim = Integer.parseInt(dataA[2]);
                            int horaInicioInt = Integer.parseInt(horaInicio.split(":")[0]) * 60 + Integer.parseInt(horaInicio.split(":")[1]);
                            int horaFimInt = Integer.parseInt(horaFim.split(":")[0]) * 60 + Integer.parseInt(horaFim.split(":")[1]);
                            int horaAtual = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

                            if (anoInicio >= now.get(Calendar.YEAR) && anoFim >= now.get(Calendar.YEAR)) {
                                if (mesInicio >= now.get(Calendar.MONTH) && mesFim >= now.get(Calendar.MONTH)) {
                                    if (diaInicio <= now.get(Calendar.DAY_OF_MONTH) && diaFim >= now.get(Calendar.DAY_OF_MONTH)) {
                                        if (diaInicio == now.get(Calendar.DAY_OF_MONTH) && horaAtual <= horaInicioInt ) {
                                            encontrou = true;
                                        } else if(diaFim == now.get(Calendar.DAY_OF_MONTH) && horaAtual <= horaFimInt){
                                            encontrou = true;
                                        } else if(diaInicio < now.get(Calendar.DAY_OF_MONTH) && diaFim > now.get(Calendar.DAY_OF_MONTH)){
                                            encontrou = true;
                                        } else {
                                            motivo.append("Fora da hora de realização do evento");
                                        }
                                    } else {
                                        motivo.append(" Fora da data (dia) de realização do evento ");
                                    }
                                } else {
                                    motivo.append(" Fora da data (mes) de realização do evento ");
                                }
                            } else {
                                motivo.append(" Fora da data (ano) de realização do evento ");
                            }
                        } else {
                            motivo.append(" Código de presença expirado ");
                        }
                        if (encontrou)
                            break;
                    }
                    msg = motivo.toString();

                    if (!encontrou) {
                        connection.close();
                        System.out.println("O evento, " + evento + " tem a seguinte causa: " + motivo);
                        return -404;
                    }

                    query = "SELECT idEvento FROM eventos " +
                            "WHERE eventos.idEvento = '" + idEvent + "';";

                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();

                    if (!result.next()) {
                        connection.close();
                        return -2;
                    }

                    //Gera código de presenças
                    codigo = (int) (Math.random() * 1000000);
                    query = "UPDATE eventos SET codigo = '" + codigo + "' WHERE idEvento = '" + idEvent + "';";
                    queryArray[0] = query;
                    preparedStatement = connection.prepareStatement(query);
                    int resultSet = preparedStatement.executeUpdate();
                    if (resultSet <= 0)
                        return -2;

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return codigo;
    }


    public  int geraCSVClient(String campo, String param, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        StringBuilder resultado = new StringBuilder();
        System.out.println("GERACSVClient");

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

                /*String query = "SELECT utilizadores.nome, cartaoCidadao, data, local, email, hora_inicio, hora_fim FROM eventos, utilizadores, presencas " +
                        "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                        "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                        "WHERE eventos.nome = '"+evento+"';";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet result = preparedStatement.executeQuery();*/

                    String query = "SELECT nome, local, data, hora_inicio, hora_fim FROM eventos " +
                            "WHERE " + campo + "='" + param + "';";
                    System.out.println(query + "\n\n");
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next())
                        return -1;

                    String nome = result.getString("nome");
                    resultado.append("\"Designação\";\"").append(nome).append("\"\n");
                    String local = result.getString("local");
                    resultado.append("\"Local\";\"").append(local).append("\"\n");
                    String data = result.getString("data");
                    resultado.append("\"Data\";\"").append(data).append("\"\n");
                    String horainicio = result.getString("hora_inicio");
                    resultado.append("\"Hora Início\";\"").append(horainicio).append("\"\n");
                    String horafim = result.getString("hora_fim");
                    resultado.append("\"Hora fim\";\"").append(horafim).append("\"\n");


                    System.out.println(resultado);
                    System.out.println("\n\n\n");

                    query = "SELECT utilizadores.nome, utilizadores.cartaoCidadao ,utilizadores.email FROM eventos " +
                            "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                            "WHERE eventos.'" + campo + "' = '" + param + "';";
                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();

                    resultado.append("\n\n");
                    resultado.append("\"Nome\";\"Número identificação\";\"Email\"\n");

                    System.out.println(resultado);
                    System.out.println("\n\n\n");
                    while (result.next()) {
                        String nomeUser = result.getString("nome");
                        String cc = result.getString("cartaoCidadao");
                        String email = result.getString("email");

                        resultado.append("\"" + nomeUser + "\";\"" + cc + "\";\"" + email + "\"\n");
                    }
                    System.out.println("ficheiro:");
                    System.out.println(resultado.toString());
                    connection.close();
                }

            //Gerar arquivo CSV
            try {
                File file = new File("files/CSVClientfile.csv");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(resultado.toString());
                fileWriter.close();

                //canocialPath = file.getCanonicalPath() + File.separator + "presencas.csv";
            } catch (IOException e) {
                System.out.println(e);
                return -2;
            }

        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return 0;
    }
    //TODO: Gera CSV presenças de um determinado evento
    public  int geraCSV2(String evento, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        StringBuilder resultado = new StringBuilder();
        System.out.println("GERACSV2");

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

                /*String query = "SELECT utilizadores.nome, cartaoCidadao, data, local, email, hora_inicio, hora_fim FROM eventos, utilizadores, presencas " +
                        "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                        "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                        "WHERE eventos.nome = '"+evento+"';";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet result = preparedStatement.executeQuery();*/

                    String query = "SELECT nome, local, data, hora_inicio, hora_fim FROM eventos " +
                            "WHERE nome = '" + evento + "';";
                    System.out.println(query + "\n\n");
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next())
                        return -1;

                    String nome = result.getString("nome");
                    resultado.append("\"Designação\";\"").append(nome).append("\"\n");
                    String local = result.getString("local");
                    resultado.append("\"Local\";\"").append(local).append("\"\n");
                    String data = result.getString("data");
                    resultado.append("\"Data\";\"").append(data).append("\"\n");
                    String horainicio = result.getString("hora_inicio");
                    resultado.append("\"Hora Início\";\"").append(horainicio).append("\"\n");
                    String horafim = result.getString("hora_fim");
                    resultado.append("\"Hora fim\";\"").append(horafim).append("\"\n");


                    System.out.println(resultado);
                    System.out.println("\n\n\n");

                    query = "SELECT utilizadores.nome, utilizadores.cartaoCidadao ,utilizadores.email FROM eventos " +
                            "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                            "WHERE eventos.nome = '" + evento + "';";
                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();

                    resultado.append("\n\n");
                    resultado.append("\"Nome\";\"Número identificação\";\"Email\"\n");

                    System.out.println(resultado);
                    System.out.println("\n\n\n");
                    while (result.next()) {
                        String nomeUser = result.getString("nome");
                        String cc = result.getString("cartaoCidadao");
                        String email = result.getString("email");

                        resultado.append("\"" + nomeUser + "\";\"" + cc + "\";\"" + email + "\"\n");
                    }
                    System.out.println("ficheiro:");
                    System.out.println(resultado.toString());
                    connection.close();
                }

            //Gerar arquivo CSV
            try {
                File file = new File("files/CSVfile.csv");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(resultado.toString());
                fileWriter.close();

                //canocialPath = file.getCanonicalPath() + File.separator + "presencas.csv";
            } catch (IOException e) {
                System.out.println(e);
                return -2;
            }

        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return 0;
    }

    //TODO: Gera CSV presenças de um determinado evento
    public  int geraCSV1(String utilizador, String[] args, String BDFileName) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;
        StringBuilder resultado = new StringBuilder();
        System.out.println("GERACSV1");

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

                /*String query = "SELECT utilizadores.nome, cartaoCidadao, data, local, email, hora_inicio, hora_fim FROM eventos, utilizadores, presencas " +
                        "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                        "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                        "WHERE eventos.nome = '"+evento+"';";
                PreparedStatement preparedStatement = connection.prepareStatement(query);
                ResultSet result = preparedStatement.executeQuery();*/

                    String query = "SELECT nome, cartaoCidadao, email FROM utilizadores " +
                            "WHERE email = '" + utilizador + "';";
                    System.out.println(query + "\n\n");
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (!result.next())
                        return -1;

                    String nome = result.getString("nome");
                    String cc = result.getString("cartaoCidadao");
                    String email = result.getString("email");

                    resultado.append("\"Nome\";\"Número identificação\";\"Email\"\n");
                    resultado.append("\"" + nome + "\";\"" + cc + "\";\"" + email + "\"\n");
                    System.out.println(resultado);

                    query = "SELECT eventos.nome, eventos.local , eventos.data, eventos.hora_inicio FROM eventos " +
                            "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                            "WHERE utilizadores.email = '" + utilizador + "';";
                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();

                    resultado.append("\n\n");
                    resultado.append("\"Designação\";\"Local\";\"Data\";\"Hora Início\"\n");


                    while (result.next()) {
                        String nomeevento = result.getString("nome");
                        String local = result.getString("local");
                        String data = result.getString("data");
                        String horainicio = result.getString("hora_inicio");

                        resultado.append("\"").append(nomeevento).append("\";\"").append(local).append("\";\"").append(data).append("\";\"").append(horainicio).append("\"\n");
                    }
                    System.out.println("ficheiro:");
                    System.out.println(resultado.toString());
                    connection.close();
                }

            //Gerar arquivo CSV
            try {
                File file = new File("files/CSV1file.csv");
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(resultado.toString());
                fileWriter.close();

                //canocialPath = file.getCanonicalPath() + File.separator + "presencas.csv";
            } catch (IOException e) {
                System.out.println(e);
                return -2;
            }

        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return -2;
        }
        return 0;
    }

    //TODO: Inserir código de presenças num evento, pelo cliente
    public String insereCodigo(String cc, int code, String[] args, String BDFileName, String[] queryArray) {
        String url = "jdbc:sqlite:" + args[1] + File.separator + BDFileName;

        try {
            show = url;
            show += "\nConectando à base de dados...";
                synchronized (lock) {
                    Connection connection = DriverManager.getConnection(url);
                    if (connection != null)
                        show += "\nConexão com a base de dados estabelecida com sucesso.";
                    else {
                        show += "\nConexão com a base de dados não foi estabelecida.";
                        return "Erro de conexão com a base de dados";
                    }

                    String query = "SELECT * FROM eventos " +
                            "JOIN presencas ON eventos.idEvento = presencas.idEvento " +
                            "JOIN utilizadores ON presencas.idCC = utilizadores.cartaoCidadao " +
                            "WHERE utilizadores.cartaoCidadao = '" + cc + "'" +
                            "AND eventos.codigo = '" + code + "';";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    ResultSet result = preparedStatement.executeQuery();

                    if (result.next()) {
                        connection.close();
                        return "Está inscrito em um evento";
                    }

                    query = "SELECT data, idEvento, hora_inicio, hora_fim, code_validade FROM eventos " +
                            "WHERE codigo = '" + code + "';";
                    preparedStatement = connection.prepareStatement(query);
                    result = preparedStatement.executeQuery();

                    String dataInicio, dataFim, horaInicio, horaFim;
                    int idEvent = 0;
                    boolean regista = false;

                    while (result.next()) {
                        idEvent = result.getInt("idEvento");
                        String data = result.getString("data");
                        dataInicio = data.split(" - ")[0];
                        dataFim = data.split(" - ")[1];
                        horaInicio = result.getString("hora_inicio");
                        horaFim = result.getString("hora_fim");
                        String validadeT = result.getString("code_validade");

                        if (Integer.parseInt(validadeT) > 0) {
                            Calendar now = Calendar.getInstance();
                            String[] dataA = dataInicio.trim().split("/");
                            int diaInicio = Integer.parseInt(dataA[0]);
                            int mesInicio = Integer.parseInt(dataA[1]);
                            int anoInicio = Integer.parseInt(dataA[2]);
                            dataA = dataFim.trim().split("/");
                            int diaFim = Integer.parseInt(dataA[0]);
                            int mesFim = Integer.parseInt(dataA[1]);
                            int anoFim = Integer.parseInt(dataA[2]);

                            int horaInicioInt = Integer.parseInt(horaInicio.split(":")[0]) * 60 + Integer.parseInt(horaInicio.split(":")[1]);
                            int horaFimInt = Integer.parseInt(horaFim.split(":")[0]) * 60 + Integer.parseInt(horaFim.split(":")[1]);
                            int horaAtual = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE);

                            if (anoInicio >= now.get(Calendar.YEAR) && anoFim >= now.get(Calendar.YEAR)) {
                                if (mesInicio >= now.get(Calendar.MONTH) && mesFim >= now.get(Calendar.MONTH)) {
                                    if (diaInicio <= now.get(Calendar.DAY_OF_MONTH) && diaFim >= now.get(Calendar.DAY_OF_MONTH)) {
                                        if (diaInicio == now.get(Calendar.DAY_OF_MONTH) && horaAtual <= horaInicioInt ) {
                                            regista = true;
                                        } else if(diaFim == now.get(Calendar.DAY_OF_MONTH) && horaAtual <= horaFimInt){
                                            regista = true;
                                        } else if(diaInicio < now.get(Calendar.DAY_OF_MONTH) && diaFim > now.get(Calendar.DAY_OF_MONTH)){
                                            regista = true;
                                        } else {
                                            return "Fora da hora de realização do evento";
                                        }
                                    }
                                }
                            }
                        }
                        if (regista)
                            break;
                    }

                    if (regista) {
                        query = "INSERT INTO presencas (idEvento,idCC) VALUES (?,?);";
                        queryArray[0] = query;
                        preparedStatement = connection.prepareStatement(query);
                        preparedStatement.setInt(1, idEvent);
                        preparedStatement.setString(2, cc);

                        int resultSet = preparedStatement.executeUpdate();

                        if (resultSet <= 0) {
                            connection.close();
                            return "Não foi inserida a presença";
                        }
                    } else
                        return "O código já está inválido";

                    connection.close();
                }
        } catch (SQLException e) {
            show += "\nErro ao conectar à base de dados: " + e.getMessage();
            return "Erro ao conectar à base de dados";
        }
        return "Inserido com sucesso";
    }

    public String getCanonicalPathCSV() {
        return canocialPath;
    }

}
