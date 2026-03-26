import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.Department;
import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.exception.StudentNotFoundException;
import ukma.model.utils.RegistryManager;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class StudentRegistryTests {
    private RegistryManager manager;
    private Student testStudent;
    private Faculty testFaculty;
    private Department testDepartment;
    @BeforeEach
    public void initStudent() {
        manager = new RegistryManager();
        testFaculty = new Faculty("Факультет Інформатики", "ФІ", null, "fi@ukma.edu.ua", "0441234567");
        testDepartment = new Department("Кафедра математики", testFaculty, null, "some location");
        manager.addFaculty(testFaculty);

        testStudent = new Student(
                "Ім'я", "Прізвище", "Побатькові",
                LocalDate.of(2005, 5, 20),
                "name@ukma.edu.ua", "0991234567",
                "K-12345", 2, "121", 2023,
                StudyForm.FULL_TIME, StudentStatus.BUDGET, testFaculty, testDepartment
        );
        manager.storeEmail(testStudent.getEmail());
        manager.addStudent(testStudent);
    }

    @Test
    public void getStudentTest() {
        Student found = manager.getStudentById(testStudent.getId());
        assertNotNull(found);
        assertEquals("Ім'я", found.getFirstName());
    }

    @Test
    public void getNonExistentStudentTest() {
        assertThrows(StudentNotFoundException.class, () -> {
            manager.getStudentById(10);
        });
    }

    @Test
    public void addStudentTest() {
        Student studentToAdd = new Student("name", "surname", "fathersname",
                LocalDate.of(2005, 5, 20),
                "name2@ukma.edu.ua", "232738274872",
                "K-23214", 3, "F-3", 2024,
                StudyForm.FULL_TIME, StudentStatus.CONTRACT, testFaculty, testDepartment);
        manager.addStudent(studentToAdd);

        assertEquals(studentToAdd, manager.getStudentById(studentToAdd.getId()));
    }

    @Test
    public void deleteStudentTest() {
        long expectedId = testStudent.getId();
        manager.deleteStudent(expectedId);

        assertThrows(StudentNotFoundException.class, () -> {
            manager.getStudentById(expectedId);
        });
    }

    @Test
    public void addStudentWithDuplicateEmailTest() {
        Student cloneEmailStudent = new Student(
                "Інше", "прізвище", "Побатькові",
                LocalDate.of(2005, 5, 20),
                "name@ukma.edu.ua", "0990000000",
                "K-99999", 1, "121", 2024,
                StudyForm.FULL_TIME, StudentStatus.BUDGET, testFaculty, testDepartment
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.storeEmail(cloneEmailStudent.getEmail());
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

}
