package ukma.ui.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ukma.domain.Student;
import ukma.domain.exception.StudentNotFoundException;
import ukma.service.ApplicationContext;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServer {
    // Створюємо логер
    private static final Logger logger = LoggerFactory.getLogger(TcpServer.class);

    public static void main(String[] args) {
        // Сервер має свій власний менеджер з даними
        ApplicationContext manager = new ApplicationContext();

        logger.info("University TCP Server is starting on port 8080...");

        try (ServerSocket serverSocket = new ServerSocket(8080)) {
            while (true) {
                // Чекаємо, поки хтось підключиться
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected from " + clientSocket.getInetAddress());

                // Передаємо клієнта в окремий потік (Багатопоточність!)
                new Thread(new ClientHandler(clientSocket, manager)).start();
            }
        } catch (IOException e) {
            logger.error("Server error occurred", e);
        }
    }
}

// Клас, який обслуговує одного конкретного клієнта
class ClientHandler implements Runnable {
    private Socket socket;
    private ApplicationContext manager;

    public ClientHandler(Socket socket, ApplicationContext manager) {
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