package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.repositories.FacultyRepo;
import ukma.model.repositories.Repository;
import ukma.model.repositories.StudentRepo;

import java.util.*;

public class RegistryManager {
    private final Repository<Student, Long> studentRepository = new StudentRepo();
    private final Repository<Faculty, Integer> facultyRepository = new FacultyRepo();


    public void addFaculty(Faculty faculty) {
        facultyRepository.store(faculty);
    }

    public void deleteFaculty(int id) {
        facultyRepository.deleteById(id);
        System.out.println("Faculty with id " + id + " was successfully removed");
    }

    public Faculty getFacultyById(int id) {
        Optional<Faculty> maybeFaculty = facultyRepository.getById(id);
        if (maybeFaculty.isEmpty()) {
            throw new IllegalArgumentException("Faculty with id " + id + " was not found");
        }

        return maybeFaculty.get();
    }

    public Map<Integer, Faculty> getFaculties() {
        List<Faculty> facultyList = facultyRepository.getAll();
        Map<Integer, Faculty> facultyMap = new HashMap<>();
        for (Faculty f : facultyList) {
            facultyMap.put(f.getId(), f);
        }
        return facultyMap;
    }

    public void addStudent(Student student) {
        studentRepository.store(student);
    }

    public void showAllStudents() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        students.stream()
                .sorted(Comparator.comparingInt(Student::getStudyYear))
                .forEach(student -> {
                    System.out.println(student);
                    System.out.println("================================================");
                });
    }

    public void showAllStudentsInFaculty(String facultyShortName) {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("Student repository is empty");
            return;
        }
        students.stream()
                .filter(student -> student.getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
                .sorted(Comparator.comparing(Student::getFullName))
                .forEach(student -> {
                    System.out.println(student);
                    System.out.println("================================================");
                });
    }

    public void deleteStudent(long id) {
        studentRepository.deleteById(id);
        System.out.println("Student with id " + id + " was successfully removed");
    }


    public List<Student> getStudentById(long id) {
        Optional<Student> s = studentRepository.getById(id);
        if (s.isEmpty()) {
            throw new IllegalArgumentException("Student with id " + id + " was not found");
        }
        List<Student> student = List.of(s.get());
        return student;
    }

    public List<Student> getStudentByNameInfo(String query) {
        String lowerQuery = query.toLowerCase();
        return studentRepository.search(s -> s.getFullName().toLowerCase().contains(lowerQuery));
    }

    public List<Student> findByCourse(int year) {
        return studentRepository.search(s -> s.getStudyYear() == year);
    }

    // Пошук за групою
    public List<Student> findByCourseCode(String courseCode) {
        return studentRepository.search(s -> s.getCourseCode().equalsIgnoreCase(courseCode.trim()));
    }
}

