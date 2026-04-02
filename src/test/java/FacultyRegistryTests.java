import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.model.Department;
import ukma.model.Faculty;
import ukma.model.Teacher;
import ukma.model.enums.Degree;
import ukma.model.exception.FacultyNotFoundException;
import ukma.model.utils.RegistryManager;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FacultyRegistryTests {
    private RegistryManager manager;
    private Teacher testDean;
    private Faculty testFaculty;
    private Department testDepartment;

    @BeforeEach
    public void init() {
        manager = new RegistryManager(true);
        testDepartment = new Department("Кафедра математики", testFaculty, null, "some location");
        testDean = new Teacher(
                "Олександр", "Мельник", "Олександрович",
                LocalDate.of(1980, 1, 1),
                "dean@ukma.edu.ua", "0441112233",
                Degree.PHD, "Доцент", "Доцент",
                LocalDate.of(2010, 9, 1), 1.0, testDepartment
        );
        testFaculty = new Faculty("Факультет Інформатики", "ФІ", testDean, "fi_office@ukma.edu.ua", "0449998877");
        manager.addTeacher(testDean);

        manager.addFaculty(testFaculty);
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
    public void getFacultyTest() {
        int expectedId = testFaculty.getId();
        Faculty found = manager.getFacultyById(expectedId);

        assertNotNull(found);
        assertEquals("Факультет Інформатики", found.getName());
        assertEquals("ФІ", found.getShortName());
    }

    @Test
    public void getNonExistingFacultyTest() {
        assertThrows(FacultyNotFoundException.class, () -> {
            manager.getFacultyById(10);
        });
    }

    @Test
    public void testAvailableTeachersForDean() {
        Teacher freeTeacher = new Teacher(
                "Вільний", "Викладач", "Петрович",
                LocalDate.of(1985, 2, 2),
                "free@ukma.edu.ua", "0990001122",
                Degree.MASTER, "Асистент", "Асистент",
                LocalDate.of(2020, 9, 1), 1.0, testDepartment
        );
        manager.addTeacher(freeTeacher);
        List<Teacher> freeTeachers = manager.getAvailableTeachersForDean();

        assertEquals(1, freeTeachers.size());
        assertEquals(freeTeacher.getId(), freeTeachers.getFirst().getId());
        assertTrue(!freeTeachers.contains(testDean));
    }
}
