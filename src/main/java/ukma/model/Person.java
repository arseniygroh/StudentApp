package ukma.model;


import java.time.LocalDate;

public class Person {
    private final long id;
    private static long idCounter = 1;
    private String firstName;
    private String lastName;
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
        if (email == null || !email.contains("@")) {
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
    public long getId() {
        return this.id;
    }
    public String getFirstName() {
        return this.firstName;
    }
    public String getLastName() {
        return this.lastName;
    }
    public String getFatherName() {
        return this.fatherName;
    }
    public String getEmail() {
        return this.email;
    }
    public String getPhoneNumber() {
        return this.phoneNumber;
    }
    public LocalDate getBirthDate() {
        return this.birthDate;
    }

    public String toString() {
        return this.firstName + " " + this.lastName + " " + this.fatherName + " was born in " + this.birthDate.getYear() + ". " + "\n"
            + "Email: " + this.email + "\n"
            + "Phone number: " + this.phoneNumber + "\n"
            + "ID: " + this.getId();
    }
}