package ukma.model.utils;

import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.time.LocalDate;
import java.util.Scanner;

public class Menu {
    private Scanner scan = new Scanner(System.in);
    private RegistryManager manager;
    private ConsoleInput inputValidator;

    public Menu(RegistryManager manager, ConsoleInput inputValidator) {
        this.manager = manager;
        this.inputValidator = inputValidator;
    }

    public void initMenu() {
        while (true) {
            System.out.println("Here are the options for registry manipulations: " + "\n"
                    + "0 - Leave the menu" + "\n"
                    + "1 - Add a student" + "\n"
                    + "2 - Delete a student" + "\n"
                    + "3 - Update a student" + "\n"
                    + "4 - Get a student" + "\n"
                    + "5 - Show all students" + "\n");
            int option = inputValidator.readInt("Select your option: ", 0, 5);
            //scan.nextLine();
            if (option == 1) {
                createAndAddStudent();
            } else if (option == 2) {
                System.out.println("Enter an ID of a student you want to delete: ");
                int id = scan.nextInt();
                scan.nextLine();
                manager.deleteStudent(id);
            } else if (option == 3) {
                manager.updateStudent();
            } else if (option == 4) {
                System.out.println("Would you like to get a student by ID or name info?" + "\n"
                + "1 - by id" + "\n"
                + "2 - by name info" + "\n");
                int choice = inputValidator.readInt("Enter your choice: ", 1, 2);
                Student result = null;
                if (choice == 1) {
                    System.out.println("Enter an ID of a student you want to get: ");
                    int id = scan.nextInt();
                    scan.nextLine();
                    result = manager.getStudentById(id);
                } else {
                    String query = inputValidator.readString("Enter some name details of a student you want to get: ");
                    result = manager.getStudentByNameInfo(query);
                }
                if (result != null) {
                    System.out.println("-- STUDENT FOUND --");
                    System.out.println(result);
                    System.out.println("---------------------");
                }
            } else if (option == 5) {
                manager.showAllStudents();
            } else break;
        }
    }
    private void createAndAddStudent() {
        System.out.println("\n--- ADDING NEW STUDENT ---");

        String firstName = inputValidator.readString("First Name: ");
        String lastName = inputValidator.readString("Last Name: ");
        String fatherName = inputValidator.readString("Father's Name: ");
        LocalDate birthDate = inputValidator.readDate("Enter birth date");
        String email = inputValidator.readString("Email: ");
        while (!email.contains("@")) {
            System.out.println("Incorrect email format!");
            email = inputValidator.readString("Email: ");
        }
        String phone = inputValidator.readString("Phone: ");
        while (phone.length() < 10) {
            System.out.println("Phone number is too short!");
            phone = inputValidator.readString("Phone: ");
        }
        String recordBookId = inputValidator.readString("Record Book ID (Ticket): ");
        int year = inputValidator.readInt("Study Year (1-6): ", 1, 6);
        //scan.nextLine();
        String courseCode = inputValidator.readString("Course Code: ");
        int admYear = inputValidator.readInt("Admission Year: ", 1991, 2025);
        //scan.nextLine();

        StudyForm studyForm = selectStudyForm();
        StudentStatus status = selectStudentStatus();

        try {
            Student newStudent = new Student(
                    firstName, lastName, fatherName, birthDate, email, phone,
                    recordBookId, year, courseCode, admYear, studyForm, status
            );

            manager.addStudent(newStudent);
            System.out.println("Student added successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private StudyForm selectStudyForm() {
        StudyForm[] forms = StudyForm.values();
        for (int i = 0; i < forms.length; i++) {
            System.out.println(i + " - " + forms[i]);
        }
        int choice = inputValidator.readInt("Your choice: ", 0, forms.length - 1);
        return forms[choice];
    }
    private StudentStatus selectStudentStatus() {
        StudentStatus[] statuses = StudentStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println(i + " - " + statuses[i]);
        }
        int choice = inputValidator.readInt("Your choice: ", 0, statuses.length - 1);
        return statuses[choice];
    }

}
