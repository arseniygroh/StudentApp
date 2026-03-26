package ukma.model;

import ukma.model.enums.Degree;
import ukma.model.utils.ShortViewable;

import java.time.LocalDate;
import java.time.Period;


public final class Teacher extends Person implements ShortViewable {
    private Degree degree;
    private String occupation;
    private String academicRank;
    private LocalDate hireDate;
    private Department department;
    private double rate;


    public Teacher() {super();}

    public Teacher(String firstName, String lastName, String fatherName,
                   LocalDate birthDate, String email, String phoneNumber,
                   Degree degree, String occupation, String academicRank,
                   LocalDate hireDate, double rate, Department department) {

        super(firstName, lastName, fatherName, birthDate, email, phoneNumber);

        setDegree(degree);
        setOccupation(occupation);
        setAcademicRank(academicRank);
        setHireDate(hireDate);
        setRate(rate);
        setDepartment(department);
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        if (department == null) {
            throw new IllegalArgumentException("Department can't be null");
        }
        this.department = department;
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
        if (hireDate == null || hireDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("invalid hire date");
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

    public int getExperienceYears() {
        if (hireDate == null) return 0;
        return Period.between(hireDate, LocalDate.now()).getYears();
    }

    @Override
    public String toString() {
        return "TEACHER INFO\n"
                + super.toString() + "\n"
                + "Degree: " + degree + "\n"
                + "Academic Rank: " + academicRank + "\n"
                + "Occupation: " + occupation + "\n"
                + "Hire Date: " + hireDate + "\n"
                + "Rate: " + rate + "\n"
                + "Experience: " + getExperienceYears() + " years";
    }

    @Override
    public String toShortString() {
        String extraInfo = occupation + " (" + degree + ")";
        return String.format("| %-5d | %-50s | %-25s |", getId(), getInitials(), extraInfo);
    }
}
