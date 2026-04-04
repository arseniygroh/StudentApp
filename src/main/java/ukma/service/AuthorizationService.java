package ukma.service;

import ukma.domain.User;
import ukma.domain.enums.Role;
import ukma.service.validation.PasswordValidator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class AuthorizationService {
    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "files/users.csv";
    private User currentUser;
    private RegistryManager manager;

    public AuthorizationService(RegistryManager manager) {
        this.manager = manager;
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
                System.out.println("File was created");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 3) {
                    String email = parts[0].trim();
                    String password = parts[1].trim();
                    Role role = Role.valueOf(parts[2].trim().toUpperCase());
                    User user = new User(email, password, role);

                    if (parts.length == 4) {
                        user.setBlocked(Boolean.parseBoolean(parts[3].trim()));
                    }
                    users.put(email, user);
                }
            }
            reader.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUserToFile(User user) {
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
            String data = user.getEmail() + "," + user.getPassword() + "," + user.getRole() + "," + user.isBlocked() + "\n";
            writer.write(data);
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUsersFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME));
            for (User user : users.values()) {
                String data = user.getEmail() + "," + user.getPassword() + "," + user.getRole() + "," + user.isBlocked() + "\n";
                writer.write(data);
            }
            writer.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean register(String email, String password, Role role) {
        if (users.containsKey(email)) {
            return false;
        }

        if (!PasswordValidator.validate(password)) {
            System.out.println("Incorrect password format");
            return false;
        }

        try {
           manager.storeEmail(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return false;
        }

        User newUser = new User(email, password, role);
        users.put(email, newUser);
        saveUserToFile(newUser);
        return true;
    }

    public Map<String, User> getUsers() {
        return users;
    }

    public boolean login(String email, String password) {
        User user = users.get(email);
        if (user != null && user.getPassword().equals(password)) {
            if (user.isBlocked()) {
                System.out.println("Error: This account has been blocked by admin");
                return false;
            }
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

    public boolean isAdmin() {
        if (currentUser == null) return false;
        return currentUser.getRole() == Role.ADMIN;
    }

}
