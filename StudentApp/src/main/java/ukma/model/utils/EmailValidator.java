package ukma.model.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    public static boolean validate(String email) {
        if (email == null) return false;

        Pattern patternEmail = Pattern.compile("[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher matcher = patternEmail.matcher(email);
        return matcher.matches();
    }
}
