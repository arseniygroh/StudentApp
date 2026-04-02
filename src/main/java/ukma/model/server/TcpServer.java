package ukma.model.server;

import ukma.model.Student;
import ukma.model.exception.StudentNotFoundException;
import ukma.model.utils.RegistryManager;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {

    public static void main(String[] args) {
        // Сервер має свій власний менеджер з даними
        RegistryManager manager = new RegistryManager();

        System.out.println("Starting University TCP Server on port 8080...");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                // Чекаємо, поки хтось підключиться
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from " + clientSocket.getInetAddress());

                // Передаємо клієнта в окремий потік (Багатопоточність!)
                new Thread(new ClientHandler(clientSocket, manager)).start();
            }
        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}

// Клас, який обслуговує одного конкретного клієнта
class ClientHandler implements Runnable {
    private Socket socket;
    private RegistryManager manager;

    public ClientHandler(Socket socket, RegistryManager manager) {
        this.socket = socket;
        this.manager = manager;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            out.println("Connected to UKMA Server! Commands: GET_STUDENT <id>, EXIT");

            String inputLine;
            // Читаємо команди від клієнта
            while ((inputLine = in.readLine()) != null) {
                if ("EXIT".equalsIgnoreCase(inputLine)) {
                    out.println("Goodbye!");
                    break;
                }

                // Обробляємо команду пошуку студента
                if (inputLine.startsWith("GET_STUDENT ")) {
                    try {
                        long id = Long.parseLong(inputLine.split(" ")[1]);
                        Student s = manager.getStudentById(id);
                        out.println(s.toShortString()); // Відправляємо рядок таблиці
                    } catch (StudentNotFoundException e) {
                        out.println("Error: " + e.getMessage());
                    } catch (Exception e) {
                        out.println("Invalid ID format.");
                    }
                } else {
                    out.println("Unknown command. Try: GET_STUDENT <id>");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected.");
        }
    }
}