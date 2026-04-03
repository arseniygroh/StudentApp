package ukma;

import ukma.domain.enums.Role;
import ukma.service.AuthorizationService;
import ukma.ui.cli.ConsoleInput;
import ukma.domain.model.Menu;
import ukma.service.RegistryManager;

public class Main {
    public static void main(String[] args) {
        RegistryManager manager = new RegistryManager();
        ConsoleInput inputValidator = new ConsoleInput();
        AuthorizationService authService = new AuthorizationService(manager);
        boolean isRunningProgram = true;

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
                String password = inputValidator.readPassword("Enter your password");
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
            } else {
                authService.logout();
                isRunningProgram = false;
                break;
            }
        }
        if (isRunningProgram) {
            Menu menu = new Menu(manager, inputValidator, authService);
            menu.initMenu();
        }
    }
}
