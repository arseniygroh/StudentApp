package ukma;

import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.service.StudentService;
import ukma.util.ConsoleInput;

import java.time.LocalDate;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        // 1. Ініціалізація інструментів
        ConsoleInput input = new ConsoleInput();
        StudentService service = new StudentService();

        // Додамо одного студента для тесту, щоб база не була пустою
        service.addStudent(new Student(
                "Гліб", "П'ятаченко", "Валерійович",
                LocalDate.of(2005, 5, 20), "glib@ukma.edu.ua", "0991234567",
                "KB123456", 2, "IPZ-22", 2024,
                StudyForm.FULL_TIME, StudentStatus.BUDGET // У тебе в коді енами трохи переплутані місцями, я використав як в конструкторі
        ));

        System.out.println("Вітаємо в DigiUni Registry!");

        while (true) {
            System.out.println("\n--- МЕНЮ ---");
            System.out.println("1. Додати студента");
            System.out.println("2. Знайти студента за ПІБ");
            System.out.println("3. Знайти студентів за курсом");
            System.out.println("4. Показати всіх");
            System.out.println("0. Вихід");

            int choice = input.readInt("Ваш вибір:", 0, 4);

            switch (choice) {
                case 1 -> createStudent(input, service);
                case 2 -> searchByName(input, service);
                case 3 -> searchByCourse(input, service);
                case 4 -> printList(service.getAll());
                case 0 -> {
                    System.out.println("До побачення!");
                    return;
                }
            }
        }
    }

    // Метод для створення студента через консоль
    private static void createStudent(ConsoleInput input, StudentService service) {
        System.out.println("--- Створення студента ---");
        try {
            // Збираємо дані через твій валідатор
            String first = input.readString("Ім'я:");
            String last = input.readString("Прізвище:");
            String father = input.readString("По батькові:");

            // Тут треба додати метод readDate в ConsoleInput (див. нижче), або поки хардкод
            LocalDate birth = LocalDate.of(2004, 1, 1);

            String email = input.readString("Email:");
            String phone = input.readString("Телефон:");
            String ticketId = input.readString("Номер заліковки:");
            int course = input.readInt("Курс (1-6):", 1, 6);
            String group = input.readString("Група (код):");
            int admYear = input.readInt("Рік вступу:", 1990, 2026);

            // Створюємо об'єкт (тут спрацюють всі перевірки в сетерах)
            Student s = new Student(
                    first, last, father, birth, email, phone,
                    ticketId, course, group, admYear,
                    StudyForm.FULL_TIME, StudentStatus.CONTRACT // Поки дефолтні значення для спрощення
            );

            service.addStudent(s);
            System.out.println("Студента успішно додано!");

        } catch (IllegalArgumentException e) {
            System.out.println("Помилка при створенні: " + e.getMessage());
        }
    }

    private static void searchByName(ConsoleInput input, StudentService service) {
        String query = input.readString("Введіть частину імені/прізвища:");
        List<Student> results = service.findByName(query);
        printList(results);
    }

    private static void searchByCourse(ConsoleInput input, StudentService service) {
        int year = input.readInt("Який курс шукаємо?", 1, 6);
        List<Student> results = service.findByCourse(year);
        printList(results);
    }

    private static void printList(List<Student> list) {
        if (list.isEmpty()) {
            System.out.println("Список порожній або нічого не знайдено.");
        } else {
            System.out.println("Знайдено записів: " + list.size());
            for (Student s : list) {
                System.out.println(" - " + s.getFullName() + " (" + s.getCourseCode() + ")");
            }
        }
    }
}