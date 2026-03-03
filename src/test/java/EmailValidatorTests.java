import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import ukma.model.utils.EmailValidator;

public class EmailValidatorTests {
    @Test
    public void testValidEmail() {
        boolean isValid = EmailValidator.validate("arsenii.hrokh@ukma.edu.ua");
        assertTrue(isValid, "Correctly formatted email must pass");
    }

    @Test
    public void testValidEmailWithNumbersAndPlus() {
        boolean isValid = EmailValidator.validate("student+2025@gmail.com");
        assertTrue(isValid, "Email with numbers and + sign must be valid");
    }

    @Test
    public void testInvalidEmailWithoutAtSymbol() {
        boolean isValid = EmailValidator.validate("arsenii.hrokhgmail.com");
        assertFalse(isValid, "Email without @ must be invalid");
    }

    @Test
    public void testInvalidEmailWithoutDomain() {
        boolean isValid = EmailValidator.validate("student@ukma");
        assertFalse(isValid, "Email without domain is invalid");
    }

    @Test
    public void testNullEmail() {
        boolean isValid = EmailValidator.validate(null);
        assertFalse(isValid, "When null is passed to the method, it must return false");
    }

    @Test
    public void testEmptyString() {
        boolean isValid = EmailValidator.validate("");
        assertFalse(isValid, "Empty string is an invalid email format");
    }
}
