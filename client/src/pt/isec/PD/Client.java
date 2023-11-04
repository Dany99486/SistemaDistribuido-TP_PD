package pt.isec.PD;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Calendar;
import java.util.Scanner;

public class Client {
    private final String AUTENTICAR = "AUTENTICAR";
    private final String REGISTAR = "REGISTAR";
    private String[] args;
    private String CC;
    private boolean admin = false;

    public Client(String[] args) {
        this.args = args;
    }

    public void client() {
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
                if (choice == 1) {
                    action = AUTENTICAR;
                    scanner.nextLine();

                    System.out.println("Digite o seu endereço de e-mail: ");
                    String email = scanner.nextLine();
                    System.out.println("Digite a sua senha: ");
                    String password = scanner.nextLine();

                     message = action + " " + email + " " + password;




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

                if (response.contains("Erro")){
                    System.out.println("Tente novamente");
                    System.exit(0);
                }

                boolean invalid;

                do {
                    invalid = false;
                    scanner.reset();
                    message = null;
                    System.out.println();
                    System.out.println("1-Edição dos dados de registo");
                    System.out.println("2-Submissão de código");
                    System.out.println("3-Consulta de presenças");
                    System.out.println("4-Obtenção de um ficheiro csv");
                    System.out.println("5-Logout");

                    if (admin) {
                        System.out.println("Privilégios de Admin:");
                        System.out.println("6-Criação de um evento");
                        System.out.println("7-Edição dos dados de um evento");
                        System.out.println("8-Eliminação de um evento");
                        System.out.println("9-Consulta dos eventos criados");
                        System.out.println("10-Geração de um código para registo de presenças");
                        System.out.println("11-Consulta das presenças registadas");
                        System.out.println("12-Obtenção de um ficheiro csv (2)");
                        System.out.println("13-Consulta dos eventos");
                        System.out.println("14-Eliminação de presenças registadas");
                        System.out.println("15-Inserção de presenças");
                    }

                    choice=scanner.nextInt();

                    switch (choice){
                        case 1->{
                            scanner.reset();
                            System.out.println("Deseja alterar:");
                            System.out.println("1-Nome");
                            System.out.println("2-Email");
                            System.out.println("3-Password");
                            choice=scanner.nextInt();
                            String alteracao;
                            scanner.nextLine();

                            switch (choice){
                                case 1->{
                                    System.out.println("Digite o seu novo nome: ");
                                    alteracao=scanner.nextLine();
                                    message="EDICAO nome "+alteracao;
                                }
                                case 2->{
                                    scanner.reset();
                                    System.out.println("Digite o seu novo endereço de e-mail: ");
                                    alteracao=scanner.nextLine();
                                    message="EDICAO email "+alteracao;

                                }
                                case 3->{
                                    scanner.reset();
                                    System.out.println("Digite a sua nova senha: ");
                                    alteracao=scanner.nextLine();
                                    message="EDICAO pass "+alteracao;

                                }
                            }

                        }
                        case 2->{
                            scanner.reset();

                        }
                        case 3->{
                            scanner.reset();

                        }
                        case 4->{
                            scanner.reset();

                        }
                        case 5->{
                            message = "LOGOUT";
                            break;
                        }
                        default ->{
                            System.out.println("Opção inválida");
                            invalid = true;
                        }
                    }
                    if (admin) {
                        switch (choice) {
                            case 6->{

                            }
                            case 7->{

                            }
                            case 8->{

                            }
                            case 9->{

                            }
                            case 10->{

                            }
                            case 11->{

                            }
                            case 12->{

                            }
                            case 13->{

                            }
                            case 14->{

                            }
                            case 15->{

                            }
                            default -> {
                                System.out.println("Opção inválida");
                                invalid = true;
                            }
                        }
                    }

                    if (!invalid) { //Não vale a pena enviar se a opção não exsitir do lado do servidor
                        out.writeObject(message);
                        out.flush();
                        System.out.println("String enviada: " + message);

                        response = (String) in.readObject();

                        System.out.println("Resposta do servidor: " + response);

                        if (response.contains("Erro")) {
                            System.out.println("Tente novamente");
                            System.exit(0);
                        }
                    }
                } while (true);

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
