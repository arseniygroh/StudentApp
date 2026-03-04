package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;

import java.util.*;

public class RegistryManager {
    private List<Student> students = new ArrayList<>();
    private Map<Integer, Faculty> faculties = new HashMap<>();

    public void addFaculty(Faculty faculty) {
        faculties.put(faculty.getId(), faculty);
    }

    public Faculty getFacultyById(int id) {
        return faculties.get(id);
    }

    public Map<Integer, Faculty> getFaculties() {
        return faculties;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void showAllStudents() {
        students.stream()
                .sorted(Comparator.comparingInt(Student::getStudyYear))
                .forEach(student -> {
                    System.out.println(student);
                    System.out.println("================================================");
                });
    }

    public void showAllStudentsInFaculty(String facultyShortName) {
        students.stream()
                .filter(student -> student.getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
                .forEach(student -> {
                    System.out.println(student);
                    System.out.println("================================================");
                });
    }

    public void deleteStudent(int id) {
        boolean isRemoved = students.removeIf(student -> student.getId() == id);
        if (isRemoved) {
            System.out.println("Student with ID " + id + " was successfully removed.");
        } else {
            System.out.println("Student with ID " + id + " not found.");
        }
    }


    public List<Student> getStudentById(int id) {
        return students.stream()
                .filter(s -> s.getId() == id)
                .toList();
    }

    public List<Student> getStudentByNameInfo(String query) {
        String lowerQuery = query.toLowerCase();
        return students.stream()
                .filter(s -> s.getFullName().toLowerCase().contains(lowerQuery))
                .toList();
    }

    public List<Student> findByCourse(int year) {
        return students.stream()
                .filter(s -> s.getStudyYear() == year)
                .toList();
    }

    // Пошук за групою
    public List<Student> findByCourseCode(String courseCode) {
        return students.stream()
                .filter(s -> s.getCourseCode().equalsIgnoreCase(courseCode.trim()))
                .toList();
    }
}

