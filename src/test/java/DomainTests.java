import org.junit.jupiter.api.Test;
import ukma.domain.Student;
import ukma.domain.Teacher;
import ukma.domain.enums.Degree;
import ukma.domain.enums.StudentStatus;
import ukma.domain.enums.StudyForm;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DomainTests {

    @Test
    public void testStudentAgeCalculation() {
        Student student = new Student("Тарас", "Тарасенко", "Тарасович", LocalDate.now().minusYears(20),
                "ivan@ukma.edu.ua", "0991234567", "K-123", 2, "121", 2024,
                StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null);
        assertEquals(20, student.getAge());
    }

    @Test
    public void testTeacherExperienceCalculation() {
        Teacher teacher = new Teacher("Петро", "Петренко", "Петрович", LocalDate.of(1980, 1, 1),
                "petro@ukma.edu.ua", "0991234567", Degree.PHD, "Доцент", "Кандидат наук",
                LocalDate.now().minusYears(10), 1.0, null);
        assertEquals(10, teacher.getExperienceYears());
    }

    @Test
    public void testInvalidBirthDateThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student("Тарас", "Тарасенко", "Тарасович", LocalDate.now().plusDays(1),
                        "ivan@ukma.edu.ua", "0991234567", "K-123", 2, "121", 2024,
                        StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null)
        );
    }

    @Test
    public void testInvalidAdmissionYearThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Student("Тарас", "Тарасенко", "Тарасович", LocalDate.of(2005, 1, 1),
                        "ivan@ukma.edu.ua", "0991234567", "K-123", 2, "121", 1990,
                        StudyForm.FULL_TIME, StudentStatus.BUDGET, null, null)
        );
    }

    @Test
    public void testTeacherHireDateBeforeAdulthoodThrowsException() {
        assertThrows(IllegalArgumentException.class, () ->
                new Teacher("Петро", "Петров", "Петрович", LocalDate.of(2010, 1, 1),
                        "petro@ukma.edu.ua", "0991234567", Degree.PHD, "Доцент", "Кандидат наук",
                        LocalDate.of(2020, 1, 1), 1.0, null)
        );
    }
}