import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ukma.domain.enums.Role;
import ukma.service.ApplicationContext;
import ukma.service.AuthorizationService;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class AuthorizationTests {

    private AuthorizationService authService;
    private ApplicationContext manager;
    private final String testEmail = "test.admin@ukma.edu.ua";
    private final String testPassword = "StrongPassword123";

    @BeforeEach
    public void setUp() {
        manager = new ApplicationContext(true);
        authService = new AuthorizationService(manager);
    }

    @AfterEach
    public void tearDown() {
        File file = new File("files/users.csv");
        if (file.exists()) {
            file.delete();
        }
    }

    @Test
    public void testSuccessfulRegistration() {
        boolean isRegistered = authService.register(testEmail, testPassword, Role.ADMIN);
        assertTrue(isRegistered);
        assertNotNull(authService.getUsers().get(testEmail));
    }

    @Test
    public void testDuplicateRegistrationFails() {
        authService.register(testEmail, testPassword, Role.USER);
        boolean isRegisteredAgain = authService.register(testEmail, "AnotherPass1", Role.MANAGER);
        assertFalse(isRegisteredAgain);
    }

    @Test
    public void testSuccessfulLoginAndLogout() {
        authService.register(testEmail, testPassword, Role.USER);
        boolean isLoggedIn = authService.login(testEmail, testPassword);
        assertTrue(isLoggedIn);
        assertNotNull(authService.getCurrentUser());

        authService.logout();
        assertNull(authService.getCurrentUser());
    }

    @Test
    public void testLoginWithWrongPasswordFails() {
        authService.register(testEmail, testPassword, Role.USER);
        boolean isLoggedIn = authService.login(testEmail, "WrongPass123");
        assertFalse(isLoggedIn);
    }

    @Test
    public void testBlockedUserCannotLogin() {
        authService.register(testEmail, testPassword, Role.USER);
        authService.getUsers().get(testEmail).setBlocked(true);

        boolean isLoggedIn = authService.login(testEmail, testPassword);
        assertFalse(isLoggedIn);
    }

    @Test
    public void testRolePermissionsVerification() {
        authService.register(testEmail, testPassword, Role.MANAGER);
        authService.login(testEmail, testPassword);

        assertTrue(authService.isManager());
        assertFalse(authService.isAdmin());
    }
}