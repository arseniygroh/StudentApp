package ukma.service;

import lombok.Getter;
import ukma.domain.Department;
import ukma.domain.Faculty;
import ukma.domain.Student;
import ukma.domain.Teacher;
import ukma.repository.*;

@Getter
public class ApplicationContext {

    private final StudentService studentService;
    private final TeacherService teacherService;
    private final FacultyService facultyService;
    private final DepartmentService departmentService;
    private final ReportService reportService;
    private final EmailRegistry emailRegistry;

    public ApplicationContext(boolean isTestMode) {
        String dir = isTestMode ? "test_files/" : "files/";

        Repository<Student, Long> studentRepo = new StudentRepo(dir + "students.ser");
        Repository<Teacher, Long> teacherRepo = new TeacherRepo(dir + "teachers.ser");
        Repository<Faculty, Integer> facultyRepo = new FacultyRepo(dir + "faculties.ser");
        Repository<Department, Integer> departmentRepo = new DepartmentRepo(dir + "departments.ser");

        this.emailRegistry = new EmailRegistry();
        studentRepo.getAll().forEach(s -> emailRegistry.storeEmail(s.getEmail()));
        teacherRepo.getAll().forEach(t -> emailRegistry.storeEmail(t.getEmail()));
        facultyRepo.getAll().forEach(f -> emailRegistry.storeEmail(f.getEmail()));

        this.studentService = new StudentService(studentRepo, emailRegistry);
        this.teacherService = new TeacherService(teacherRepo, facultyRepo, departmentRepo, emailRegistry);
        this.facultyService = new FacultyService(facultyRepo, emailRegistry);
        this.departmentService = new DepartmentService(departmentRepo);
        this.reportService = new ReportService(studentRepo, teacherRepo);
    }

    public ApplicationContext() {
        this(false);
    }
}