package ukma;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.enums.Role;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.utils.AuthorizationService;
import ukma.model.utils.ConsoleInput;
import ukma.model.utils.Menu;
import ukma.model.utils.RegistryManager;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        RegistryManager manager = new RegistryManager();
        ConsoleInput inputValidator = new ConsoleInput();
        AuthorizationService authService = new AuthorizationService();

        System.out.println("WELCOME TO OUR APP!");
        System.out.println("Before using it, you have to log in or register a new account");
        while (true) {
            System.out.println("0 - leave the app" + "\n"
                    + "1 - log in" + "\n"
                    + "2 - register");
            int option = inputValidator.readInt("Choose your option", 0, 2);
            if (option == 1) {
                String email = inputValidator.readEmail("Enter your email");
                String password = inputValidator.readString("Enter your password");
                boolean isLoggined = authService.login(email, password);
                if (isLoggined) {
                    System.out.println("You have successfully logged in! Welcome back");
                    break;
                } else {
                    System.out.println("There is no such user or your data was incorrect. Try to create a new account");
                }
            } else if (option == 2) {
                String email = inputValidator.readEmail("Enter your email");
                String password = inputValidator.readString("Enter your password");
                System.out.println("Now, you have to choose your authorization status");
                for (int i = 0; i < Role.values().length; i++) {
                    System.out.println(i + " - " + Role.values()[i]);
                }
                int roleChoice = inputValidator.readInt("Enter your choice", 0, Role.values().length - 1);
                Role role = Role.values()[roleChoice];
                boolean isRegistered = authService.register(email, password, role);
                if (isRegistered) {
                    System.out.println("You have successfully created a brand new account");
                } else {
                    System.out.println("Such user is already created");
                }
            } else break;
        }

        Menu menu = new Menu(manager, inputValidator, authService);
        Faculty fi = new Faculty("Факультет інформатики", "ФІ", null, "fi@ukma.edu.ua", "+380440000000");
        Faculty fen = new Faculty("Факультет економічних наук", "ФЕН", null, "fen@ukma.edu.ua", "+380442222222");
        manager.addFaculty(fi);
        manager.addFaculty(fen);
        Student s1 = new Student("Арсеній", "Грох", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@gmail.com", "232932934545", "some id", 2, "ipz", 2023, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        Student s2 = new Student("Віталій", "Дида", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@gmail.com", "2329329334343", "some i1", 1, "ipz", 2025, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        Student s3 = new Student("Артем", "Слободян", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@gmail.com", "232932932323", "some id2", 3, "ipz", 2022, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        manager.addStudent(s1);
        manager.addStudent(s2);
        manager.addStudent(s3);

        menu.initMenu();
    }
}
