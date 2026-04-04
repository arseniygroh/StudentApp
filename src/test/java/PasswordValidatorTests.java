import org.junit.jupiter.api.Test;
import ukma.service.validation.PasswordValidator;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PasswordValidatorTests {
    @Test
    public void testValidPass() {
        boolean isValid = PasswordValidator.validate("Admin1234");
        assertTrue(isValid, "Correctly formatted password must pass");
    }

    @Test
    public void testPassWithoutDigit() {
        boolean isValid = PasswordValidator.validate("AdminIsHere");
        assertFalse(isValid, "Password without digit can't pass");
    }

    @Test
    public void testPassWithoutUppercaseLetter() {
        boolean isValid = PasswordValidator.validate("adminsere123");
        assertFalse(isValid, "Password without uppercase can't pass");
    }

    @Test
    public void testPassWithoutSufficientSymbolAmount() {
        boolean isValid = PasswordValidator.validate("adm32");
        assertFalse(isValid, "Password that is shorter than 8 symbols can't pass");
    }

}
