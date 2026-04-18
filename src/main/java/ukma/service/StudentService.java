package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.Department;
import ukma.domain.Student;
import ukma.domain.exception.StudentNotFoundException;
import ukma.repository.Repository;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class StudentService {
    private final Repository<Student, Long> studentRepository;
    private final EmailRegistry emailRegistry;

    public StudentService(Repository<Student, Long> studentRepository, EmailRegistry emailRegistry) {
        this.studentRepository = studentRepository;
        this.emailRegistry = emailRegistry;
    }

    public void addStudent(Student student) {
        studentRepository.store(student);
    }

    public void showAllStudentsSortedByStudyYear() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.stream()
                .sorted(Comparator.comparingInt(Student::getStudyYear))
                .forEach(student -> System.out.println(student.toShortString()));
    }

    public void showAllStudentsInDepartmentSortedByStudyYear(Department d) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.stream()
                .filter(student -> d.equals(student.getDepartment()))
                .sorted(Comparator.comparingInt(Student::getStudyYear))
                .forEach(student -> System.out.println(student.toShortString()));
    }

    public void showAllStudentsInDepartmentSortedAlphabetically(Department d) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.stream()
                .filter(student -> d.equals(student.getDepartment()))
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(student -> System.out.println(student.toShortString()));
    }

    public void showAllStudentsOfSameCourseInDepartmentSortedAlphabetically(Department d, int studyYear) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.stream()
                .filter(student -> d.equals(student.getDepartment()))
                .filter(student -> student.getStudyYear() == studyYear)
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(student -> System.out.println(student.toShortString()));
    }

    public void showAllStudentsInFaculty(String facultyShortName) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.stream()
                .filter(student -> student.getFaculty() != null
                        && student.getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(student -> System.out.println(student.toShortString()));
    }

    public void showAll() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Student Name", "Course Info"));
        System.out.println("-".repeat(89));
        students.forEach(student -> System.out.println(student.toShortString()));
    }

    public void deleteStudent(long id) {
        String emailToRemove = getStudentById(id).getEmail();
        studentRepository.deleteById(id);
        emailRegistry.removeEmail(emailToRemove);
        log.info("Student with ID {} was successfully removed", id);
    }

    public void updateStudent(Student student) {
        studentRepository.store(student);
    }

    public Student getStudentById(long id) {
        return studentRepository.getById(id)
                .orElseThrow(() -> new StudentNotFoundException("Student with id " + id + " was not found"));
    }

    public List<Student> getStudentByNameInfo(String query) {
        String lowerQuery = query.toLowerCase();
        return studentRepository.search(s -> s.getFullName().toLowerCase().contains(lowerQuery));
    }

    public List<Student> findByCourse(int year) {
        return studentRepository.search(s -> s.getStudyYear() == year);
    }

    public List<Student> findByCourseCode(String courseCode) {
        return studentRepository.search(s -> s.getCourseCode().equalsIgnoreCase(courseCode.trim()));
    }
}