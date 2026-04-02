package ukma.model.server;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class TcpClient {
    public static void main(String[] args) {
        System.out.println("Connecting to server...");

        try (
                // Підключаємось до сервера (localhost = на цьому ж комп'ютері)
                Socket socket = new Socket("localhost", 8080);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner console = new Scanner(System.in)
        ) {
            // Читаємо привітання від сервера
            System.out.println("Server: " + in.readLine());

            while (true) {
                System.out.print("Enter command: ");
                String command = console.nextLine();

                // Відправляємо на сервер
                out.println(command);

                // Отримуємо відповідь
                String response = in.readLine();
                System.out.println("Server response: " + response);

                if ("EXIT".equalsIgnoreCase(command)) {
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Could not connect to the server. Make sure TcpServer is running.");
        }
    }
}