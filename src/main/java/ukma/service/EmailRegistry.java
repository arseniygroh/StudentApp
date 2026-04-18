package ukma.service;

import java.util.HashSet;
import java.util.Set;

public class EmailRegistry {
    private final Set<String> emails = new HashSet<>();

    public void storeEmail(String email) {
        if (emails.contains(email)) {
            throw new IllegalArgumentException("This email already exists");
        }
        emails.add(email);
    }

    public void removeEmail(String email) {
        emails.remove(email);
    }
}