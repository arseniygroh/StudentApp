package ukma.model;

import ukma.model.enums.Role;

public class User {
    private String email;
    private String password;
    private Role role;

    public User(String email, String password, Role role) {
        if (email == null || email.trim().isEmpty() || !email.contains("@")) {
            throw new IllegalArgumentException("input can't be empty");
        }
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        if (role == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        this.email = email;
        this.password = password;
        this.role = role;
    }
    public String getEmail() {
        return email;
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
                "email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
