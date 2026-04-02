import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.Department;
import ukma.model.Faculty;
import ukma.model.Teacher;
import ukma.model.enums.Degree;
import ukma.model.exception.DepartmentNotFoundException;
import ukma.model.exception.TeacherNotFoundException;
import ukma.model.utils.RegistryManager;

import java.io.File;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherAndDepartmentTests {
    private RegistryManager manager;
    private Faculty testFaculty;
    private Department testDepartment;
    private Teacher testTeacher;

    @BeforeEach
    public void setUp() {
        manager = new RegistryManager(true);
        testFaculty = new Faculty("Факультет Інформатики", "ФІ", null, "fi@ukma.edu.ua", "0441234567");
        manager.addFaculty(testFaculty);
        testDepartment = new Department("Кафедра математики", testFaculty, null, "Корпус 1");
        manager.addDepartment(testDepartment);


        testTeacher = new Teacher(
                "Петро", "Яременко", "Іванович",
                LocalDate.of(2004, 10, 15),
                "p.yaremenko@ukma.edu.ua", "0671234567",
                Degree.MASTER, "Викладач", "Кандидат наук",
                LocalDate.of(2010, 9, 1),
                1.0, testDepartment
        );
        manager.storeEmail(testTeacher.getEmail());
        manager.addTeacher(testTeacher);
    }

    @AfterEach
    public void cleanup() {
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
    public void getDepartmentTest() {
        Department found = manager.getDepartmentById(testDepartment.getId());
        assertNotNull(found);
        assertEquals("Кафедра математики", found.getName());
    }

    @Test
    public void getNonExistentDepartmentTest() {
        assertThrows(DepartmentNotFoundException.class, () -> {
            manager.getDepartmentById(999);
        });
    }

    @Test
    public void deleteDepartmentTest() {
        int expectedId = testDepartment.getId();
        manager.deleteDepartment(expectedId);
        assertThrows(DepartmentNotFoundException.class, () -> {
            manager.getDepartmentById(expectedId);
        });
    }

    @Test
    public void getTeacherTest() {
        Teacher found = manager.getTeacherById(testTeacher.getId());
        assertNotNull(found);
        assertEquals("Яременко", found.getLastName());
        assertEquals("p.yaremenko@ukma.edu.ua", found.getEmail());
    }

    @Test
    public void getNonExistentTeacherTest() {
        assertThrows(TeacherNotFoundException.class, () -> {
            manager.getTeacherById(999);
        });
    }

    @Test
    public void deleteTeacherTest() {
        long expectedId = testTeacher.getId();
        manager.deleteTeacher(expectedId);

        assertThrows(TeacherNotFoundException.class, () -> {
            manager.getTeacherById(expectedId);
        });
    }

    @Test
    public void addTeacherWithDuplicateEmailTest() {
        Teacher cloneEmailTeacher = new Teacher(
                "Іван", "Іванов", "Іванович",
                LocalDate.of(1985, 5, 20),
                "p.yaremenko@ukma.edu.ua",
                "0990000000",
                Degree.BACHELOR, "Асистент", "Some rank",
                LocalDate.now(), 0.5, testDepartment
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            manager.storeEmail(cloneEmailTeacher.getEmail());
        });

        assertTrue(exception.getMessage().contains("already exists"));
    }

    @Test
    public void updateTeacherTest() {
        Teacher teacherToUpdate = manager.getTeacherById(testTeacher.getId());

        teacherToUpdate.setOccupation("Професор");
        teacherToUpdate.setRate(1.5);
        manager.updateTeacher(teacherToUpdate);

        Teacher updatedTeacher = manager.getTeacherById(testTeacher.getId());
        assertEquals("Професор", updatedTeacher.getOccupation());
        assertEquals(1.5, updatedTeacher.getRate());
    }

}
