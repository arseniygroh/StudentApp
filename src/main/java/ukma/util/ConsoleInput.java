package ukma.util;

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
                System.out.println("Число має бути від " + min + " до " + max);
            } catch (NumberFormatException e) {
                System.out.println("Це не число! Спробуйте ще раз.");
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
            System.out.println("Поле не може бути порожнім!");
        }
    }

    //для дат
    public java.time.LocalDate readDate(String message) {
        while (true) {
            System.out.println(message + " (формат РРРР-ММ-ДД):");
            String line = scanner.nextLine().trim();
            try {
                return java.time.LocalDate.parse(line);
            } catch (java.time.format.DateTimeParseException e) {
                System.out.println("Неправильний формат! Приклад: 2005-09-01");
            }
        }
    }
}