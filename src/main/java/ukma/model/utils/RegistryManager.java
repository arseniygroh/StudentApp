package ukma.model.utils;


import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.Teacher;
import ukma.model.Department;
import ukma.model.enums.StudentStatus;
import ukma.model.exception.*;
import ukma.model.repositories.*;
import java.util.stream.Collectors;
import ukma.model.enums.StudyForm;

import java.util.*;

public class RegistryManager {
    private final Repository<Student, Long> studentRepository = new StudentRepo();
    private final Repository<Teacher, Long> teacherRepository = new TeacherRepo();
    private final Repository<Faculty, Integer> facultyRepository = new FacultyRepo();
    private final Repository<Department, Integer> departmentRepository = new DepartmentRepo();

    private final Set<String> emailRepository = new HashSet<>();

    public RegistryManager() {
        loadEmails();
    }

    private void loadEmails() {
        studentRepository.getAll().forEach(student -> emailRepository.add(student.getEmail()));
        teacherRepository.getAll().forEach(teacher -> emailRepository.add(teacher.getEmail()));
        facultyRepository.getAll().forEach(faculty -> emailRepository.add(faculty.getEmail()));
    }

    public void storeEmail(String email) {
        if (emailRepository.contains(email)) throw new IllegalArgumentException("This email already exists");
        emailRepository.add(email);
    }

    public void removeEmail(String email) {
        emailRepository.remove(email);
    }

    // ===== FACULTY METHODS ======
    public void addFaculty(Faculty faculty) {
        facultyRepository.store(faculty);
    }

    public void updateFaculty(Faculty faculty) {
        facultyRepository.store(faculty);
    }

    public void deleteFaculty(int id) {
        String emailToRemove = getFacultyById(id).getEmail();
        facultyRepository.deleteById(id);
        removeEmail(emailToRemove);
        System.out.println("Faculty with id " + id + " was successfully removed");
    }

    public Faculty getFacultyById(int id) {
        return facultyRepository.getById(id)
                .orElseThrow(() -> new FacultyNotFoundException("Faculty with id " + id + " was not found"));
    }

    public void showFacultiesAlphabeticallySorted() {
        List<Faculty> faculties = facultyRepository.getAll();
        if (faculties.isEmpty()) {
            System.out.println("Repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Faculty Name", "Dean"));
        System.out.println("-".repeat(89));
        faculties.stream()
                .sorted(Comparator.comparing(Faculty::getName))
                .forEach(f -> System.out.println(f.toShortString()));
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
                .filter(student -> student.getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
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
        removeEmail(emailToRemove);
        System.out.println("Student with id " + id + " was successfully removed");
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

    // ===== TEACHER METHODS ======
    public void addTeacher(Teacher teacher) {
        teacherRepository.store(teacher);
    }

    public void updateTeacher(Teacher teacher) {
        teacherRepository.store(teacher);
    }

    public Teacher getTeacherById(long id) {
        return teacherRepository.getById(id)
                .orElseThrow(() -> new TeacherNotFoundException("Teacher with id " + id + " was not found"));
    }

    public List<Teacher> getTeachersByNameInfo(String query) {
        String lowerQuery = query.toLowerCase();
        return teacherRepository.search(teacher -> teacher.getFullName().toLowerCase().contains(lowerQuery));
    }

    public void showAllTeachersInFaculty(String facultyShortName) {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teacher repository is empty");
            return;
        }
        teachers.stream()
                .filter(teacher -> teacher.getDepartment() != null
                        && teacher.getDepartment().getFaculty() != null
                        && teacher.getDepartment().getFaculty().getShortName().equalsIgnoreCase(facultyShortName))
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(teacher -> {
                    System.out.println(teacher);
                    System.out.println("================================================");
                });
    }

    public void showDepartmentTeachersAlphabetically(Department department) {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teacher repository is empty");
            return;
        }

        teachers.stream()
                .filter(teacher -> department.equals(teacher.getDepartment()))
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(t -> {
                    System.out.println(t);
                    System.out.println("================================================");
                });
    }


    public void showAllTeachers() {
        List<Teacher> teachers = teacherRepository.getAll();
        if (teachers.isEmpty()) {
            System.out.println("Teachers repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Teacher Name", "Occupation"));
        System.out.println("-".repeat(89));
        teachers.stream()
                .sorted(Comparator.comparing(Teacher::getFullName))
                .forEach(teacher -> System.out.println(teacher.toShortString()));
    }

    public void deleteTeacher(long id) {
        String emailToRemove = getTeacherById(id).getEmail();
        teacherRepository.deleteById(id);
        removeEmail(emailToRemove);
        System.out.println("Teacher with id " + id + " was successfully removed");
    }

    public List<Teacher> getAvailableTeachersForDean() {
        List<Long> busyTeacherIds = facultyRepository.getAll().stream()
                .map(faculty -> faculty.getDean())
                .filter(teacher -> teacher != null)
                .map(teacher -> teacher.getId())
                .toList();
        List<Teacher> availableTeachers = teacherRepository.getAll().stream()
                .filter(teacher -> !busyTeacherIds.contains(teacher.getId()))
                .toList();
        return availableTeachers;
    }

    public List<Teacher> getAvailableTeachersForHead() {
        List<Long> busyTeacherIds = departmentRepository.getAll().stream()
                .map(department -> department.getHead())
                .filter(teacher -> teacher != null)
                .map(teacher -> teacher.getId())
                .toList();

        List<Teacher> availableTeachers = teacherRepository.getAll().stream()
                .filter(teacher -> !busyTeacherIds.contains(teacher.getId()))
                .toList();
        return availableTeachers;
    }

    // ===== DEPARTMENT METHODS ======
    public void addDepartment(Department department) {
        departmentRepository.store(department);
    }

    public void updateDepartment(Department department) {
        departmentRepository.store(department);
    }

    public Department getDepartmentById(int id) {
        return departmentRepository.getById(id)
                .orElseThrow(() -> new DepartmentNotFoundException("Department with id " + id + " was not found"));
    }

    public Map<Integer, Department> getDepartments() {
        List<Department> departmentList = departmentRepository.getAll();
        Map<Integer, Department> departmentMap = new HashMap<>();
        for (Department d : departmentList) {
            departmentMap.put(d.getId(), d);
        }
        return departmentMap;
    }


    public void showAllDepartments() {
        List<Department> departments = departmentRepository.getAll();
        if (departments.isEmpty()) {
            System.out.println("Department repository is empty");
            return;
        }
        System.out.println(String.format("| %-5s | %-50s | %-25s |", "ID", "Department Name", "Head"));
        System.out.println("-".repeat(89));
        departments.stream()
                .sorted(Comparator.comparing(Department::getName))
                .forEach(dept -> System.out.println(dept.toShortString()));
    }

    public void deleteDepartment(int id) {
        departmentRepository.deleteById(id);
        System.out.println("Department with id " + id + " was successfully removed");
    }

    // REPORTS / STATISTICS (STREAM API)

    // Звіт 1: Кількість студентів за формою навчання (Full time/Expelled/Graduated/Academic leave)
    public void printStudentCountByStudyForm() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        Map<StudyForm, Long> stats = students.stream()
                .collect(Collectors.groupingBy(Student::getStudyForm, Collectors.counting()));

        System.out.println("\nSTUDENT COUNT BY STUDY FORM");
        stats.forEach((form, count) -> System.out.println(form + ": " + count + " students"));
        System.out.println("-----------------------------------");
    }

    // Звіт 2: Кількість студентів на кожному факультеті
    public void printStudentCountByFaculty() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        Map<String, Long> stats = students.stream()
                .filter(s -> s.getFaculty() != null)
                .collect(Collectors.groupingBy(s -> s.getFaculty().getName(), Collectors.counting()));

        System.out.println("\nSTUDENT COUNT BY FACULTY");
        stats.forEach((facultyName, count) -> System.out.println(facultyName + ": " + count + " students"));
        System.out.println("--------------------------------");
    }

    // Звіт 3: Кількість студентів за статусом (Budget/Contract)
    public void printStudentCountByStatus() {
        List<Student> students = studentRepository.getAll();
        if (students.isEmpty()) {
            System.out.println("No students available for the report.");
            return;
        }
        Map<StudentStatus, Long> stats = students.stream()
                .collect(Collectors.groupingBy(Student::getStatus, Collectors.counting()));

        System.out.println("\nSTUDENT COUNT BY STUDENT STATUS");
        stats.forEach((status, count) -> System.out.println(status + ": " + count + " students"));
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
}
