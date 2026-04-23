package ukma.service;

import lombok.extern.slf4j.Slf4j;
import ukma.domain.Student;
import ukma.domain.Teacher;
import ukma.repository.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
public class ReportService {
    private final Repository<Student, Long> studentRepository;
    private final Repository<Teacher, Long> teacherRepository;

    public ReportService(Repository<Student, Long> studentRepository, Repository<Teacher, Long> teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }
    // Звіт 1: Кількість студентів за формою навчання
    public void printStudentCountByStudyForm() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        System.out.println("\nSTUDENT COUNT BY STUDY FORM");
        students.stream()
                .collect(Collectors.groupingBy(Student::getStudyForm, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ukma.domain.ReportEntry(e.getKey().toString(), e.getValue()))
                .forEach(System.out::println);
        System.out.println("-----------------------------------");
    }

    // Звіт 2: Кількість студентів на кожному факультеті
    public void printStudentCountByFaculty() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        System.out.println("\nSTUDENT COUNT BY FACULTY");
        students.stream()
                .filter(s -> s.getFaculty() != null)
                .collect(Collectors.groupingBy(s -> s.getFaculty().getName(), Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ukma.domain.ReportEntry(e.getKey(), e.getValue()))
                .forEach(System.out::println);
        System.out.println("--------------------------------");
    }

    // Звіт 3: Кількість студентів за статусом (Budget/Contract)
    public void printStudentCountByStatus() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        System.out.println("\nSTUDENT COUNT BY STUDENT STATUS");
        students.stream()
                .collect(Collectors.groupingBy(Student::getStatus, Collectors.counting()))
                .entrySet().stream()
                .map(e -> new ukma.domain.ReportEntry(e.getKey().toString(), e.getValue()))
                .forEach(System.out::println);
        System.out.println("-----------------------------------");
    }


    // Звіт 4: Середня ставка всіх викладачів
    public void printAverageTeacherRate() {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("No teachers available for the report.");
            return;
        }
        double avgRate = teachers.stream()
                .mapToDouble(Teacher::getRate)
                .average()
                .orElse(0.0);

        System.out.println("\nAVERAGE TEACHER RATE");
        System.out.printf("Average rate across all teachers: %.2f%n", avgRate);
        System.out.println("----------------------------");
    }

    // Звіт 5: Середній вік студента на факультеті
    public void printAvarageStudentAge(String facultyName) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }

        double avgAge = students.stream()
                .filter(student -> student.getFaculty() != null
                        && facultyName.equalsIgnoreCase(student.getFaculty().getName()))
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);

        System.out.println("\nAVERAGE STUDENT AGE");
        System.out.println("Average age: " + Math.floor(avgAge));
        System.out.println("----------------------------");
    }
}
