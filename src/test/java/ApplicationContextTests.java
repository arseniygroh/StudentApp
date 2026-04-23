import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.domain.Department;
import ukma.domain.Faculty;
import ukma.domain.Student;
import ukma.domain.Teacher;
import ukma.domain.enums.Degree;
import ukma.domain.enums.StudentStatus;
import ukma.domain.enums.StudyForm;
import ukma.domain.exception.FacultyNotFoundException;
import ukma.domain.exception.StudentNotFoundException;
import ukma.service.ApplicationContext;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationContextTests {

    private ApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new ApplicationContext(true);
    }

    @AfterEach
    public void tearDown() {
        deleteDirectory(new File("test_files"));
    }

    private void deleteDirectory(File file) {
        if (file.exists()) {
            File[] contents = file.listFiles();
            if (contents != null) {
                for (File f : contents) {
                    deleteDirectory(f);
                }
            }
            file.delete();
        }
    }

    @Test
    public void testAddAndGetFaculty() {
        Faculty faculty = new Faculty("Інформатики", "ФІ", null, "fi@ukma.edu.ua", "0441234567");
        context.getFacultyService().addFaculty(faculty);
        Faculty retrieved = context.getFacultyService().getFacultyById(faculty.getId());
        assertEquals("ФІ", retrieved.getShortName());
    }

    @Test
    public void testDeleteFaculty() {
        Faculty faculty = new Faculty("Економічний", "ФЕН", null, "fen@ukma.edu.ua", "0441234567");
        context.getFacultyService().addFaculty(faculty);
        context.getFacultyService().deleteFaculty(faculty.getId());
        assertThrows(FacultyNotFoundException.class, () -> context.getFacultyService().getFacultyById(faculty.getId()));
    }

    @Test
    public void testUniqueEmailEnforcement() {
        Faculty faculty = new Faculty("Правничий", "ФПН", null, "law@ukma.edu.ua", "0441234567");
        context.getEmailRegistry().storeEmail(faculty.getEmail());
        context.getFacultyService().addFaculty(faculty);
        assertThrows(IllegalArgumentException.class, () -> context.getEmailRegistry().storeEmail("law@ukma.edu.ua"));
    }

    @Test
    public void testAddAndFindStudentByName() {
        Student s = new Student("Олег", "Шевченко", "Олегович", LocalDate.of(2003, 1, 1),
                "oleg.sh@ukma.edu.ua", "0991112233", "K-111", 3, "121", 2022,
                StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null);
        context.getStudentService().addStudent(s);
        List<Student> results = context.getStudentService().getStudentByNameInfo("Шевченко");
        assertEquals(1, results.size());
        assertEquals("Олег", results.get(0).getFirstName());
    }

    @Test
    public void testUpdateStudent() {
        Student s = new Student("Марія", "Коваленко", "Іванівна", LocalDate.of(2004, 1, 1),
                "maria@ukma.edu.ua", "0991112233", "K-222", 2, "035", 2023,
                StudyForm.FULL_TIME, StudentStatus.CONTRACT, null, null);
        context.getStudentService().addStudent(s);

        s.setStudyYear(3);
        context.getStudentService().updateStudent(s);

        Student updated = context.getStudentService().getStudentById(s.getId());
        assertEquals(3, updated.getStudyYear());
    }

    @Test
    public void testFindByCourse() {
        context.getStudentService().addStudent(new Student("С1", "П1", "Б1", LocalDate.of(2005,1,1), "s1@ukma.edu.ua", "0990000000", "K1", 1, "121", 2024, StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null));
        context.getStudentService().addStudent(new Student("С2", "П2", "Б2", LocalDate.of(2004,1,1), "s2@ukma.edu.ua", "0990000001", "K2", 2, "121", 2023, StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null));

        List<Student> firstYearStudents = context.getStudentService().findByCourse(1);
        assertEquals(1, firstYearStudents.size());
        assertEquals("С1", firstYearStudents.get(0).getFirstName());
    }

    @Test
    public void testGetAvailableTeachersForHead() {
        Teacher freeTeacher = new Teacher("Вільний", "Викладач", "Петрович", LocalDate.of(1985, 2, 2), "free@ukma.edu.ua", "0990001122", Degree.MASTER, "Асистент", "Асистент", LocalDate.of(2020, 9, 1), 1.0, null);
        Teacher busyTeacher = new Teacher("Зайнятий", "Викладач", "Іванович", LocalDate.of(1980, 2, 2), "busy@ukma.edu.ua", "0990001133", Degree.PHD, "Доцент", "Доцент", LocalDate.of(2015, 9, 1), 1.0, null);

        context.getTeacherService().addTeacher(freeTeacher);
        context.getTeacherService().addTeacher(busyTeacher);

        Department d = new Department("Кафедра", null, busyTeacher, "Корпус 1");
        context.getDepartmentService().addDepartment(d);

        List<Teacher> available = context.getTeacherService().getAvailableTeachersForHead();
        assertEquals(1, available.size());
        assertEquals(freeTeacher.getId(), available.get(0).getId());
    }

    @Test
    public void testGetNonExistentStudentThrowsException() {
        assertThrows(StudentNotFoundException.class, () -> context.getStudentService().getStudentById(999L));
    }
}