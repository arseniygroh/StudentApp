package ukma.domain;

import ukma.domain.enums.Role;
import ukma.service.validation.EmailValidator;
import ukma.service.validation.PasswordValidator;

import java.util.Objects;

public class User {
    private String email;
    private String password;
    private Role role;
    private boolean isBlocked;

    public User(String email, String password, Role role) {
        if (!EmailValidator.validate(email)) {
            throw new IllegalArgumentException("invalid email");
        }
        setPassword(password);
        if (role == null) {
            throw new IllegalArgumentException("input can't be null");
        }
        this.email = email;
        this.role = role;
        this.isBlocked = false;
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

    public void setEmail(String email) {
        if (!EmailValidator.validate(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }
    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public void setPassword(String password) {
        if (!PasswordValidator.validate(password)) throw new IllegalArgumentException("Invalid password format");
        this.password = password;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return email.equals(user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email);
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", role=" + role +
                '}';
    }
}
