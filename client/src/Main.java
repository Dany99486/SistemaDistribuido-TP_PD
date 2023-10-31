import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Main {
    public static final int MAX_SIZE = 4000;
    public static final int TIMEOUT = 10000;

    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.out.println("Sintaxe: java Cliente serverAddress serverUdpPort");
            return;
        }

        try {
            Socket socket = new Socket(InetAddress.getByName(args[0]), Integer.parseInt(args[1]));

            try (
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
            ) {
                Scanner scanner = new Scanner(System.in);

                System.out.println("Escolha uma opção:");
                System.out.println("1. Autenticar");
                System.out.println("2. Registar");
                int choice = scanner.nextInt();

                String action;
                if (choice == 1) {
                    action = "AUTENTICAR";
                } else if (choice == 2) {
                    action = "REGISTAR";
                } else {
                    System.out.println("Escolha inválida. A sair do programa...");
                    return;
                }

                scanner.nextLine();

                System.out.println("Digite o seu endereço de e-mail: ");
                String email = scanner.nextLine();
                System.out.println("Digite a sua senha: ");
                String password = scanner.nextLine();

                String message = action + " " + email + " " + password;
                out.println(message);
                System.out.println("String enviada: " + message);

                String response = in.readLine();
                System.out.println("Resposta do servidor: " + response);
            } catch (IOException e) {
                e.printStackTrace();
            }

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}