package ukma.domain;

import ukma.service.validation.EmailValidator;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

public class Faculty implements ShortViewable, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private final int id;
    private static int idCounter = 1;
    public static void setNextId(int id) {
        idCounter = id + 1;
    }

    private String name;
    private String shortName;
    private Teacher dean;
    private String email;
    private String phone;

    public Faculty(String name, String shortName, Teacher dean, String email, String phone) {
        this.id = idCounter++;
        setName(name);
        setShortName(shortName);
        setDean(dean);
        setEmail(email);
        setPhone(phone);
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.name = name;
    }

    public void setShortName(String shortName) {
        if (shortName == null || shortName.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.shortName = shortName;
    }

    public void setDean(Teacher dean) {
        this.dean = dean;
    }

    public void setEmail(String email) {
        if (!EmailValidator.validate(email)) {
            throw new IllegalArgumentException("invalid email");
        }
        this.email = email;
    }

    public void setPhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.phone = phone;
    }

    public String getName() {
        return name;
    }
    public String getShortName() {
        return shortName;
    }
    public Teacher getDean() {
        return dean;
    }
    public String getEmail() {
        return email;
    }
    public String getPhone() {
        return phone;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Faculty that = (Faculty) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "FACULTY INFO\n"
                + "ID: " + id + "\n"
                + "Name: " + name + " (" + shortName + ")\n"
                + "Dean: " + (dean != null ? dean.getFullName() : "None") + "\n"
                + "Email: " + email + "\n"
                + "Phone: " + phone;
    }

    @Override
    public String toShortString() {
        String extraInfo = "Dean: " + (dean != null ? dean.getInitials() : "None");
        return String.format("| %-5d | %-50s | %-25s |", id, name, extraInfo);
    }
}
