package ukma.domain;

import ukma.domain.enums.StudentStatus;
import ukma.domain.enums.StudyForm;

import java.io.Serial;
import java.time.LocalDate;
import java.time.Period;

public final class Student extends Person implements ShortViewable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String studentRecordBookId;
    private int studyYear;
    private String courseCode;
    private int admissionYear;
    private StudyForm studyForm;
    private StudentStatus status;
    private Faculty faculty;
    private Department department;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String fatherName,
                   LocalDate birthDate, String email, String phoneNumber,
                   String studentRecordBookId, int studyYear, String courseCode,
                   int admissionYear, StudyForm studyForm, StudentStatus status, Faculty faculty, Department department) {

        super(firstName, lastName, fatherName, birthDate, email, phoneNumber);
        setStudentTicketId(studentRecordBookId);
        setStudyYear(studyYear);
        setCourseCode(courseCode);
        setFaculty(faculty);
        setDepartment(department);

        setAdmissionYear(admissionYear);

        if (studyForm == null) throw new IllegalArgumentException("Study form is required");
        this.studyForm = studyForm;

        if (status == null) throw new IllegalArgumentException("Status is required");
        this.status = status;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }


    public Faculty getFaculty() {
        return this.faculty;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setStudentTicketId(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("invalid input, it can't be empty");
        }
        this.studentRecordBookId = id;
    }

    public void setStudyForm(StudyForm form) {
        if (form != null) studyForm = form;
        else throw new IllegalArgumentException("Invalid input");
    }

    public void setStatus(StudentStatus status) {
        if (status != null) this.status = status;
        else throw new IllegalArgumentException("Invalid input");
    }

    public void setStudyYear(int year) {
        if (year < 1 || year > 6) {
            throw new IllegalArgumentException("year of study must be between 1 and 6");
        }
        this.studyYear = year;
    }

    public void setCourseCode(String code) {
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

    public int getAge() {
        return Period.between(getBirthDate(), LocalDate.now()).getYears();
    }

    public void setAdmissionYear(int year) {
        int currentYear = LocalDate.now().getYear();
        if (admissionYear < 1991 || admissionYear > currentYear) {
            throw new IllegalArgumentException("Admission year must be between 1991 and " + currentYear);
        }
        this.admissionYear = year;
    }

    @Override
    public String toString() {
        return "STUDENT INFO\n"
                + super.toString() + "\n"
                + "Record Book ID: " + studentRecordBookId + "\n"
                + "Study Year: " + studyYear + "\n"
                + "Course Code: " + courseCode + "\n"
                + "Admission Year: " + admissionYear + "\n"
                + "Study Form: " + studyForm + "\n"
                + "Status: " + status + "\n"
                + "Faculty: " + (faculty != null ? faculty.getName() : "None") + "\n"
                + "Department: " + (department != null ? department.getName() : "None");
    }

    @Override
    public String toShortString() {
        String extraInfo = courseCode + "-" + studyYear + " (" + studyForm + ")";
        return String.format("| %-5d | %-50s | %-25s |", getId(), getInitials(), extraInfo);
    }
}
