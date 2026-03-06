package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.Teacher;
import ukma.model.Department;
import ukma.model.exception.*;
import ukma.model.repositories.*;

import java.util.*;

public class RegistryManager {
    private final Repository<Student, Long> studentRepository = new StudentRepo();
    private final Repository<Teacher, Long> teacherRepository = new TeacherRepo();
    private final Repository<Faculty, Integer> facultyRepository = new FacultyRepo();
    private final Repository<Department, Integer> departmentRepository = new DepartmentRepo();

    // ===== FACULTY METHODS ======
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

    public void showFacultiesAlphabeticallySorted() {
        List<Faculty> faculties = facultyRepository.getAll();
        if (faculties.isEmpty()) {
            System.out.println("Repository is empty");
            return;
        }
        faculties.stream()
                .sorted(Comparator.comparing(Faculty::getName))
                .forEach(f -> {
                    System.out.println(f);
                    System.out.println("================================================");
                });
    }

    public Map<Integer, Faculty> getFaculties() {
        List<Faculty> facultyList = facultyRepository.getAll();
        Map<Integer, Faculty> facultyMap = new HashMap<>();
        for (Faculty f : facultyList) {
            facultyMap.put(f.getId(), f);
        }
        return facultyMap;
    }

    // ===== STUDENT METHODS ======
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

    // ===== TEACHER METHODS ======
    public void addTeacher(Teacher teacher) {
        teacherRepository.store(teacher);
    }

    public Teacher getTeacherById(long id) {
        return teacherRepository.getById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher with id " + id + " was not found"));
    }

    public void showAllTeachers() {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teachers repository is empty");
            return;
        }
        teachers.stream()
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(teacher -> {
                    System.out.println(teacher);
                    System.out.println("================================================");
                });
    }

    public void deleteTeacher(long id) {
        teacherRepository.deleteById(id);
        System.out.println("Teacher with id " + id + " was successfully removed");
    }

    // ===== DEPARTMENT METHODS ======
    public void addDepartment(Department department) {
        departmentRepository.store(department);
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.getById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id + " was not found"));
    }

    public void showAllDepartments() {
        List<Department> departments = departmentRepository.getAll();
        if (departments.isEmpty()) {
            System.out.println("Department repository is empty");
            return;
        }
        departments.stream()
                .sorted(Comparator.comparing(Department::getName))
                .forEach(dept -> {
                    System.out.println(dept);
                    System.out.println("================================================");
                });
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
        System.out.println("Department with id " + id + " was successfully removed");
    }
}
