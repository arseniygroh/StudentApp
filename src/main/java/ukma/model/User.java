package ukma.model;

import ukma.model.enums.Role;
import ukma.model.utils.EmailValidator;

public class User {
    private String email;
    private String password;
    private Role role;

    public User(String email, String password, Role role) {
        if (!EmailValidator.validate(email)) {
            throw new IllegalArgumentException("invalid email");
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
