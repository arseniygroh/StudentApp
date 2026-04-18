package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.User;
import ukma.domain.enums.Role;
import ukma.service.validation.PasswordValidator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class AuthorizationService {

    private Map<String, User> users = new HashMap<>();
    private static final String FILE_NAME = "files/users.csv";
    private User currentUser;
    private ApplicationContext context;

    public AuthorizationService(ApplicationContext context) {
        this.context = context;
        loadUsersFromFile();
    }

    private void loadUsersFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                log.info("Users file was successfully created at {}", FILE_NAME);
            } catch (IOException e) {
                log.error("Failed to create users file", e);
            }
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
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
        } catch (IOException e) {
            log.error("Error reading users from file", e);
        }
    }

    public void saveUserToFile(User user) {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
                log.info("Users file was created during saving new user");
            } catch (IOException e) {
                log.error("Failed to create users file during save", e);
            }
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
            String data = user.getEmail() + "," + user.getPassword() + "," + user.getRole() + "," + user.isBlocked() + "\n";
            writer.write(data);
        } catch (IOException e) {
            log.error("Error writing new user to file", e);
        }
    }

    public void updateUsersFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (User user : users.values()) {
                String data = user.getEmail() + "," + user.getPassword() + "," + user.getRole() + "," + user.isBlocked() + "\n";
                writer.write(data);
            }
        } catch (IOException e) {
            log.error("Error updating users file", e);
        }
    }

    public boolean register(String email, String password, Role role) {
        if (users.containsKey(email)) {
            log.warn("Failed registration attempt: Email {} already exists", email);
            return false;
        }

        if (!PasswordValidator.validate(password)) {
            System.out.println("Incorrect password format");
            log.warn("Failed registration attempt: Invalid password format for {}", email);
            return false;
        }

        try {
            context.getEmailRegistry().storeEmail(email);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            log.warn("Failed registration attempt: Email validation failed for {}", email);
            return false;
        }

        User newUser = new User(email, password, role);
        users.put(email, newUser);
        saveUserToFile(newUser);
        log.info("New user successfully registered: {} with role {}", email, role);
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
                log.warn("Blocked user attempted to login: {}", email);
                return false;
            }
            currentUser = user;
            log.info("User logged in successfully: {}", email);
            return true;
        }
        log.warn("Failed login attempt for email: {}", email);
        return false;
    }

    public void logout() {
        if (currentUser != null) {
            log.info("User logged out: {}", currentUser.getEmail());
            currentUser = null;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public boolean isManager() {
        return currentUser != null && currentUser.getRole() == Role.MANAGER;
    }

    public boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == Role.ADMIN;
    }
}