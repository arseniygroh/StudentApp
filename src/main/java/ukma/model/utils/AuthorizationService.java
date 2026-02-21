package ukma.model.utils;

import ukma.model.User;
import ukma.model.enums.Role;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationService {
    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "users.csv";
    private User currentUser;

    public AuthorizationService() {
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File was created");
                return;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String email = parts[0].trim();
                    String password = parts[1].trim().toUpperCase();
                    Role role = Role.valueOf(parts[2].trim());
                    users.put(email, new User(email, password, role));
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    private void saveUserToFile(User user) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File was created");
                return;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
            String data = user.getEmail() + ", " + user.getPassword() + ", " + user.getRole() + "\n";
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean register(String email, String password, Role role) {
        if (users.containsKey(email)) {
            return false;
        }
        User newUser = new User(email, password, role);
        users.put(email, newUser);
        saveUserToFile(newUser);
        return true;
    }

    public boolean login(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            currentUser = user;
            return true;
        }
        return false;
    }

    public void logout() {
        currentUser = null;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isManager() {
        if (currentUser == null) {
            return false;
        }
        return currentUser.getRole() == Role.MANAGER;
    }

}
