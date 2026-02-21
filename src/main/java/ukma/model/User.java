package ukma.model;

import ukma.model.enums.Role;

public class User {
    private String username;
    private String password;
    private Role role;

    public User(String username, String password, Role role) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        this.username = username;
        this.password = password;
        this.role = role;
    }
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", role=" + role +
                '}';
    }
}
