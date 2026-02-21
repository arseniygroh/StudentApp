package ukma.model;

import ukma.model.enums.Degree;

import java.time.LocalDate;

public class Teacher extends Person {
    private Degree degree;
    private String occupation;
    private String academicRank;
    private String hireDate;
    private double rate;


    public Teacher() {super();}

    public Teacher(String firstName, String lastName, String fatherName,
                   LocalDate birthDate, String email, String phoneNumber,
                   Degree degree, String occupation, String academicRank,
                   String hireDate, double rate) {

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

    public String getHireDate() {
        return hireDate;
    }

    public void setHireDate(String hireDate) {
        if (hireDate == null || hireDate.isEmpty()) {
            throw new IllegalArgumentException("invalid input");
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
