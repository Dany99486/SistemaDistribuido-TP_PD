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

                System.out.println("Resposta do servidor: " + response);

                if (response.contains("Erro")){
                    System.out.println("Tente novamente");
                    System.exit(0);
                }

                scanner.reset();
                do {
                    System.out.println();
                    System.out.println("1-Edição dos dados de registo");
                    System.out.println("2-Submissão de código");
                    System.out.println("3-Consulta de presenças");
                    System.out.println("4-Obtenção de um ficheiro csv");

                    scanner.nextLine();

                } while (!scanner.nextLine().equalsIgnoreCase("Logout"));

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
