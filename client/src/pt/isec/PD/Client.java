package pt.isec.PD;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private String[] args;
    private boolean admin = false;

    public Client(String[] args) {
        this.args = args;
    }

    public void client() {
        String filename = null;
        String message = null;
        if (args.length != 2) {
            System.out.println("Sintaxe: java Cliente serverAddress serverUdpPort");
            return;
        }

        try {
            Socket socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

            try (
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream())
            ) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Escolha uma opção:");
                System.out.println("1. Autenticar");
                System.out.println("2. Registar");
                int choice = scanner.nextInt();

                String action;
                //Espaço de 10 segundos para o cliente se autenticar
                if (choice == 1) {
                    action = AUTENTICAR;
                    scanner.nextLine();

                    message = action;

                    System.out.println("A autenticar ...");
                    out.writeObject(message);
                    out.flush();

                    System.out.println("Digite o seu endereço de e-mail: ");
                    String email = scanner.nextLine();
                    System.out.println("Digite a sua senha: ");
                    String password = scanner.nextLine();

                    message = email + " " + password;

                } else if (choice == 2) {
                    action = REGISTAR;
                    scanner.nextLine();

                    System.out.println("Digite o seu nome: ");
                    String nome = scanner.nextLine();
                    System.out.println("Digite o numero do seu CC: ");
                    String cc = scanner.nextLine();
                    System.out.println("Digite o seu endereço de e-mail: ");
                    String email = scanner.nextLine();
                    System.out.println("Digite a sua senha: ");
                    String password = scanner.nextLine();

                    message = action + " " + email + " " + password + " " + cc + " " + nome;
                } else {
                    System.out.println("Escolha inválida. A sair do programa...");
                    return;
                }

                out.writeObject(message);
                out.flush();
                System.out.println("String enviada: " + message);

                String response = (String) in.readObject();

                if (response.contains("Admin bem-vindo"))
                    admin = true;

                System.out.println("Resposta do servidor: " + response);

                if (response.contains("Erro")) {
                    System.out.println("Tente novamente");
                    System.exit(0);
                }

                boolean invalid;

                do {
                    invalid = false;
                    scanner.reset();
                    message = null;
                    System.out.println();
                    if (!admin) {
                        System.out.println("1-Edição dos dados de registo");
                        System.out.println("2-Submissão de código");
                        System.out.println("3-Consulta de presenças");
                        System.out.println("4-Obtenção de um ficheiro csv");//csvCliente
                    }
                    System.out.println("5-Logout");

                    if (admin) {
                        System.out.println("Privilégios de Admin:");
                        System.out.println("6-Criação de um evento");
                        System.out.println("7-Edição dos dados de um evento");
                        System.out.println("8-Eliminação de um evento");
                        System.out.println("9-Consulta dos eventos criados"); //ModoFiltro
                        System.out.println("10-Geração de um código para registo de presenças");
                        System.out.println("11-Consulta das presenças registadas");
                        System.out.println("12-Obtenção de um ficheiro csv dos participantes (2)");
                        System.out.println("13-Consulta dos eventos"); //ModoEmail
                        System.out.println("14-Eliminação de presenças registadas");
                        System.out.println("15-Inserção de presenças");
                        System.out.println("16-Obtenção de um ficheiro csv dos eventos do utilizador (3)");
                    }

                    choice=scanner.nextInt();

                    if (choice <= 5 && !admin) {
                        switch (choice) {
                            case 1 -> {
                                scanner.reset();
                                System.out.println("Deseja alterar:");
                                System.out.println("1-Nome");
                                System.out.println("2-Email");
                                System.out.println("3-Password");
                                choice = scanner.nextInt();
                                String alteracao;
                                scanner.nextLine();

                                switch (choice) {
                                    case 1 -> {
                                        System.out.println("Digite o seu novo nome: ");
                                        alteracao = scanner.nextLine();
                                        message = "EDICAO nome " + alteracao;
                                    }
                                    case 2 -> {
                                        scanner.reset();
                                        System.out.println("Digite o seu novo endereço de e-mail: ");
                                        alteracao = scanner.nextLine();
                                        message = "EDICAO email " + alteracao;
                                    }
                                    case 3 -> {
                                        scanner.reset();
                                        System.out.println("Digite a sua nova senha: ");
                                        alteracao = scanner.nextLine();
                                        message = "EDICAO pass " + alteracao;
                                    }
                                }
                            }
                            case 2 -> {
                                scanner.reset();
                                System.out.println("<Código>");
                                String[] aux = scanner.next().trim().split(" ");
                                scanner.nextLine();
                                message = "CODIGO " + aux[0];
                            }
                            case 3 -> {
                                scanner.reset();
                                System.out.println("<Filtro se existir>");
                                String[] aux = scanner.next().trim().split(" ");
                                scanner.nextLine();
                                if (aux[0].equalsIgnoreCase("nenhum"))
                                    message = "CONSULTA sem_filtro";
                                else
                                    message = "CONSULTA " + aux[0];
                            }
                            case 4 -> {
                                filename = "CSVClientfile.csv";
                                Scanner scanneraux = new Scanner(System.in);
                                scanner.reset();
                                System.out.println("<Campo> <palavra a filtrar>");//CSVClient
                                String[] aux = scanneraux.next().trim().split(" ");
                                scanner.nextLine();
                                message = "EVENTO CSV "+aux[0]+" "+aux[1];
                            }
                            case 5 -> {
                                message = "LOGOUT";
                                break;
                            }
                            default -> {
                                System.out.println("Opção inválida");
                                invalid = true;
                            }
                        }
                    }
                    if (choice >= 5 && admin) {
                        String[] aux;
                        scanner.nextLine();
                        switch (choice) {
                            case 5 -> {
                                message = "LOGOUT";
                                break;
                            }
                            case 6->{
                                System.out.println("Digite os dados do evento:");
                                scanner.reset();
                                System.out.println("<Nome> <data inicio> <data fim> <Local> <hora inicio> <hora fim>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "EVENTO "+aux[0]+" "+aux[1]+" "+aux[2]+" "+aux[3]+" "+aux[4]+" "+aux[5];
                            }
                            case 7->{
                                scanner.reset();
                                System.out.println("<Campo da tabela> <alteracao nova> <nome evento>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "EVENTO EDICAO "+aux[0]+" "+aux[1]+" "+aux[2];
                            }
                            case 8->{
                                scanner.reset();
                                System.out.println("<Nome evento>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "EVENTO APAGAR "+ aux[0];
                            }
                            case 9->{
                                scanner.reset();
                                System.out.println("<Campo> <palavra a filtrar>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "EVENTO CONSULTA "+aux[0]+" "+aux[1];
                            }
                            case 10->{
                                scanner.reset();
                                System.out.println("<Nome Evento> <Tempo>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "GERAR "+aux[0]+" "+aux[1];
                            }
                            case 11->{
                                scanner.reset();
                                System.out.println("<Nome evento>");
                                message = "EVENTO CONSULTA "+scanner.nextLine().trim();
                            }
                            case 12->{
                                filename= "CSVfile.csv";
                                scanner.reset();
                                System.out.println("<Nome evento>"); //Gera CSV2
                                message = "EVENTO CSV2 "+scanner.nextLine().trim();
                            }
                            case 13->{
                                scanner.reset();
                                System.out.println("<Email>");
                                message = "EVENTO EMAIL "+scanner.nextLine().trim();
                            }
                            case 14->{
                                scanner.reset();
                                System.out.println("<Nome evento> <Email>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "PRESENCAS APAGAR "+aux[0]+" "+aux[1];
                            }
                            case 15->{
                                scanner.reset();
                                System.out.println("<Nome evento> <Email>");
                                aux = scanner.nextLine().trim().split(" ");
                                message = "PRESENCAS "+aux[0]+" "+aux[1];
                            }
                            case 16->{
                                System.out.println();
                                filename= "CSV1file.csv";
                                scanner.reset();
                                System.out.println("<Email>"); //Gera CSV1
                                message = "EVENTO CSV "+scanner.nextLine().trim();
                            }
                            default -> {
                                System.out.println("Opção inválida");
                                invalid = true;
                            }
                        }
                    }

                    if (!invalid) { //Não vale a pena enviar se a opção não existir do lado do servidor

                        if (response.equalsIgnoreCase("Erro: Timeout")) {
                            System.out.println("Tente novamente, timeout excedido");
                            System.exit(0);
                        }

                        out.writeObject(message);
                        out.flush();
                        System.out.println("String enviada: " + message);

                        if (!(choice==12 || choice==4 || choice==16))
                            response = (String) in.readObject();

                        if (choice==12 || choice==4 || choice==16) {
                            System.out.println("A receber ficheiro...");
                            ReceiveFile receiveFile = new ReceiveFile(socket);
                            if (receiveFile.receiveFile(filename))//TODO: Mudar nome do ficheiro
                                System.out.println("Ficheiro recebido com sucesso");
                            else
                                System.out.println("Erro ao receber ficheiro");
                        } else
                            System.out.println("Resposta do servidor: " + response);

                        if (response.contains("Erro")) {
                            System.out.println("Tente novamente");
                            System.exit(0);
                        }
                    }
                } while (!message.equalsIgnoreCase("LOGOUT"));

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                System.out.println("Erro, dados passados não são reconhecidos " + e);
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
