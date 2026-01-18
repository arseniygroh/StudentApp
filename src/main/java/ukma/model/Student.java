package ukma.model;

import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.time.LocalDate;

public class Student extends Person {
    private String studentRecordBookId;
    private int studyYear;
    private String courseCode;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String fatherName,
                   LocalDate birthDate, String email, String phoneNumber,
                   String studentRecordBookId, int studyYear, String courseCode,
                   int admissionYear, StudyForm studyForm, StudentStatus status) {

        super(firstName, lastName, fatherName, birthDate, email, phoneNumber);
        setStudentTicketId(studentRecordBookId);
        setStudyYear(studyYear);
        setGroupCode(courseCode);

        if (admissionYear <= 0) throw new IllegalArgumentException("invalid year input");
        this.admissionYear = admissionYear;

        if (studyForm == null) throw new IllegalArgumentException("Study form is required");
        this.studyForm = studyForm;

        if (status == null) throw new IllegalArgumentException("Status is required");
        this.status = status;
    }

    public void setStudentTicketId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("invalid input, it can't be empty");
        }
        this.studentRecordBookId = id;
    }

    public void setStudyYear(int year) {
        if (year < 1 || year > 6) {
            throw new IllegalArgumentException("year of study must be between 1 and 6");
        }
        this.studyYear = year;
    }

    public void setGroupCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("invalid input, it can't be empty");
        }
        this.courseCode = code;
    }

    public String getStudentRecordBookId() {
        return this.studentRecordBookId;
    }

    public int getStudyYear() {
        return this.studyYear;
    }

    public String getCourseCode() {
        return this.courseCode;
    }

    public int getAdmissionYear() {
        return this.admissionYear;
    }

    public StudyForm getStudyForm() {
        return this.studyForm;
    }

    public StudentStatus getStatus() {
        return this.status;
    }

    public String toString() {
        return "This is a student! " + "\n"
                + super.toString();
    }
}
