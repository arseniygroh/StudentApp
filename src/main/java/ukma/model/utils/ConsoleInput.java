package ukma.model.utils;

import java.util.Scanner;

public class ConsoleInput {
    private Scanner scanner = new Scanner(System.in);

    //для чисел в межах діапазону
    public int readInt(String message, int min, int max) {
        while (true) {
            System.out.println(message);
            String line = scanner.nextLine();
            try {
                int value = Integer.parseInt(line);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Number must be from " + min + " to " + max);
            } catch (NumberFormatException e) {
                System.out.println("It is not a number try again");
            }
        }
    }

    //для стрічок
    public String readString(String message) {
        while (true) {
            System.out.println(message);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input can't be empty");
        }
    }

    //для дат
    public java.time.LocalDate readDate(String message) {
        while (true) {
            System.out.println(message + " (format: YYYY-MM-DD):");
            String line = scanner.nextLine().trim();
            try {
                return java.time.LocalDate.parse(line);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Incorrect format! Example: 2005-09-01");
            }
        }
    }

    public String readEmail(String message) {
        while (true) {
            System.out.println(message + " (don't forget valid email format):");
            String line = scanner.nextLine().trim();
            if (!EmailValidator.validate(line)) {
                System.out.println("Invalid email");
                continue;
            }
            return line;
        }
    }

    public String readPassword(String message) {
        while (true) {
            System.out.println(message + " (at least 1 uppercase, 1 lowercase, 1 digit and length >= 8)");
            String line = scanner.nextLine().trim();
            if (!PasswordValidator.validate(line)) {
                System.out.println("Invalid password");
                continue;
            }
            return line;
        }
    }

}
