package ukma.model.utils;

import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.util.Scanner;

public class Menu {
    private Scanner scan = new Scanner(System.in);
    private RegistryManager manager;

    public Menu(RegistryManager manager) {
        this.manager = manager;
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
            System.out.println("Select your option: ");
            int option = scan.nextInt();
            scan.nextLine();
            while (option < 0 || option > 5) {
                System.out.println("Invalid option input! Try again: ");
                option = scan.nextInt();
                scan.nextLine();
            }
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
                int choice = scan.nextInt();
                scan.nextLine();
                while (choice != 1 && choice != 2) {
                    System.out.println("Invalid input! Try again: ");
                    choice = scan.nextInt();
                    scan.nextLine();
                }
                Student result = null;
                if (choice == 1) {
                    System.out.println("Enter an ID of a student you want to get: ");
                    int id = scan.nextInt();
                    scan.nextLine();
                    result = manager.getStudentById(id);
                } else {
                    System.out.println("Enter some name details of a student you want to get: ");
                    String query = scan.nextLine().trim();
                    while (query.isEmpty()) {
                        System.out.println("Input can't be empty! Try again:");
                        query = scan.nextLine().trim();
                    }
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

        System.out.print("First Name: ");
        String firstName = scan.nextLine().trim();

        System.out.print("Last Name: ");
        String lastName = scan.nextLine().trim();

        System.out.print("Father Name (Middle Name): ");
        String middleName = scan.nextLine().trim();

        System.out.print("Birth Date (YYYY-MM-DD): ");
        String birthDate = scan.nextLine().trim();

        System.out.print("Email: ");
        String email = scan.nextLine().trim();

        System.out.print("Phone: ");
        String phone = scan.nextLine().trim();

        System.out.print("Record Book ID (Ticket): ");
        String recordId = scan.nextLine().trim();

        System.out.print("Study Year (1-6): ");
        int year = scan.nextInt();
        scan.nextLine();

        System.out.print("Course Code: ");
        String courseCode = scan.nextLine();

        System.out.print("Admission Year: ");
        int admYear = scan.nextInt();
        scan.nextLine();

        StudyForm studyForm = selectStudyForm();
        StudentStatus status = selectStudentStatus();

        try {
            Student newStudent = new Student(
                    firstName, lastName, middleName, birthDate, email, phone,
                    recordId, year, courseCode, admYear, studyForm, status
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
        System.out.print("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();
        while (choice < 0 || choice > forms.length - 1) {
            System.out.println("Invalid input! Try again: ");
            choice = scan.nextInt();
            scan.nextLine();
        }
        return forms[choice];
    }
    private StudentStatus selectStudentStatus() {
        StudentStatus[] statuses = StudentStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println(i + " - " + statuses[i]);
        }
        System.out.print("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();
        while (choice < 0 || choice > statuses.length - 1) {
            System.out.println("Invalid input! Try again: ");
            choice = scan.nextInt();
            scan.nextLine();
        }
        return statuses[choice];
    }

}
