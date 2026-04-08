package ukma.service.validation;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    public static boolean validate(String password) {
        if (password == null || password.isBlank()) return false;
        Pattern patternPass = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[^,]{8,}$");
        Matcher matcher = patternPass.matcher(password);
        return matcher.matches();
    }
}
