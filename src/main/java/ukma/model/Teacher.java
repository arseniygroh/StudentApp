package ukma.model;

import ukma.model.enums.Degree;

import java.time.LocalDate;

public class Teacher extends Person {
    private Degree degree;
    private String occupation;
    private String academicRank;
    private LocalDate hireDate;
    private double rate;

    public Teacher(String firstName, String lastName, String fatherName,
                   LocalDate birthDate, String email, String phoneNumber,
                   Degree degree, String occupation, String academicRank,
                   LocalDate hireDate, double rate) {

        super(firstName, lastName, fatherName, birthDate, email, phoneNumber);

        setDegree(degree);
        setOccupation(occupation);
        setAcademicRank(academicRank);
        setHireDate(hireDate);
        setRate(rate);
    }

    public Degree getDegree() {
        return degree;
    }

    public void setDegree(Degree degree) {
        if (degree == null) {
            throw new IllegalArgumentException("invalid input");
        }
        this.degree = degree;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        if (occupation == null || occupation.isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.occupation = occupation;
    }

    public String getAcademicRank() {
        return academicRank;
    }

    public void setAcademicRank(String academicRank) {
        if (academicRank == null || academicRank.isEmpty()) {
            throw new IllegalArgumentException("input can't be empty");
        }
        this.academicRank = academicRank;
    }

    public LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(LocalDate hireDate) {
        if (hireDate == null) {
            throw new IllegalArgumentException("invalid input");
        }
        if (hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("invalid date, it can't be in the future");
        }
        this.hireDate = hireDate;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        if (rate <= 0) {
            throw new IllegalArgumentException("invalid input, it must be positive value");
        }
        this.rate = rate;
    }

    public String toString() {
        return "This is a teacher! " + "\n"
                + super.toString();
    }
}
