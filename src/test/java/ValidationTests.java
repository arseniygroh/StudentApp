import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import ukma.domain.University;
import ukma.service.validation.AnnotationValidator;
import ukma.service.validation.EmailValidator;
import ukma.service.validation.PasswordValidator;

import static org.junit.jupiter.api.Assertions.*;

public class ValidationTests {

    @ParameterizedTest
    @ValueSource(strings = {"student.2026@ukma.edu.ua", "admin+test@gmail.com", "user@sub.domain.org"})
    public void testValidEmails(String email) {
        assertTrue(EmailValidator.validate(email));
    }

    @ParameterizedTest
    @ValueSource(strings = {"student@ukma", "admin.gmail.com", "", "   "})
    public void testInvalidEmails(String email) {
        assertFalse(EmailValidator.validate(email));
    }

    @Test
    public void testNullEmail() {
        assertFalse(EmailValidator.validate(null));
    }

    @ParameterizedTest
    @ValueSource(strings = {"StrongPass1", "Valid1234!"})
    public void testValidPasswords(String password) {
        assertTrue(PasswordValidator.validate(password));
    }

    @ParameterizedTest
    @ValueSource(strings = {"weakpass", "Nodigits", "12345678", "Short1!"})
    public void testInvalidPasswords(String password) {
        assertFalse(PasswordValidator.validate(password));
    }

    @Test
    public void testAnnotationValidatorPasses() {
        University validUni = new University("National University of Kyiv-Mohyla Academy", "NaUKMA", "Kyiv", "Skovorody st.");
        assertDoesNotThrow(() -> AnnotationValidator.validate(validUni));
    }

    @Test
    public void testAnnotationValidatorFails() {
        University invalidUni = new University("A", "NaUKMA", "Kyiv", "Skovorody st.");
        assertThrows(IllegalArgumentException.class, () -> AnnotationValidator.validate(invalidUni));
    }
}