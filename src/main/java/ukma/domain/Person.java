package ukma.domain;

import lombok.Getter;
import ukma.service.validation.EmailValidator;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import ukma.domain.annotations.Length;
import ukma.service.validation.AnnotationValidator;

@Getter
abstract public sealed class Person implements Serializable permits Student, Teacher {
    @Serial
    private static final long serialVersionUID = 1L;
    private final long id;
    private static long idCounter = 1;
    public static void setNextId(long id) {
        if (id >= idCounter) {
            idCounter = id + 1;
        }
    }

    @Length(min = 2, max = 50)
    private String firstName;

    @Length(min = 2, max = 50)
    private String lastName;

    @Length(min = 2, max = 50)
    private String fatherName;

    private LocalDate birthDate;
    private String email;
    private String phoneNumber;

    public Person () {
        this.id = idCounter++;
    } // Default

    public Person(String firstName, String lastName, String fatherName, LocalDate birthDate, String email, String phoneNumber) {

        this.id = idCounter++;

        setFirstName(firstName);
        setLastName(lastName);
        setFatherName(fatherName);

        if (birthDate == null || birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("invalid birthday input");
        }

        this.birthDate = birthDate;

        setEmail(email);
        setPhoneNumber(phoneNumber);
        AnnotationValidator.validate(this);
    }

    public String getFullName() {
        return firstName + " " + lastName + " " + fatherName;
    }

    public void setFirstName(String firstName) {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.firstName = firstName.trim();
    }

    public void setLastName(String lastName) {
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.lastName = lastName.trim();
    }

    public void setFatherName(String fatherName) {
        if (fatherName == null || fatherName.isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.fatherName = fatherName.trim();
    }

    public void setEmail(String email) {
        if (!EmailValidator.validate(email)) {
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email.trim();
    }

    public void setPhoneNumber(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.length() < 10) {
            throw new IllegalArgumentException("Phone number is too short");
        }
        this.phoneNumber = phoneNumber.trim();
    }

    public String getInitials() {
        return lastName + " " + firstName.charAt(0) + ". " + fatherName.charAt(0) + ".";
    }
  
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        Person that = (Person) o;
        return this.getId() == that.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public String toString() {
        return "ID: " + id + "\n"
                + "Full Name: " + getFullName() + "\n"
                + "Birth Date: " + birthDate.toString() + "\n"
                + "Email: " + email + "\n"
                + "Phone: " + phoneNumber;
    }
}