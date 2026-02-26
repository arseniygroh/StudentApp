package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Menu {
    private Scanner scan = new Scanner(System.in);
    private RegistryManager manager;
    private ConsoleInput inputValidator;
    private AuthorizationService authService;

    public Menu(RegistryManager manager, ConsoleInput inputValidator, AuthorizationService authService) {
        this.manager = manager;
        this.inputValidator = inputValidator;
        this.authService = authService;
    }

    public void initMenu() {
        while (true) {
            System.out.println("Here are the options for registry manipulations: " + "\n"
                    + "0 - Leave the menu" + "\n"
                    + "1 - Add a student (manager only)" + "\n"
                    + "2 - Delete a student (manager only)" + "\n"
                    + "3 - Update a student (manager only)" + "\n"
                    + "4 - Get a student" + "\n"
                    + "5 - Show all students" + "\n");
            int option = inputValidator.readInt("Select your option: ", 0, 5);
            //scan.nextLine();
            if (option == 1) {
                if (authService.isManager()) createAndAddStudent();
                else System.out.println("You don't have a right to do it");
            } else if (option == 2) {
                if (authService.isManager()) {
                    System.out.println("Enter an ID of a student you want to delete: ");
                    int id = scan.nextInt();
                    scan.nextLine();
                    manager.deleteStudent(id);
                } else System.out.println("You don't have a right to do it");

            } else if (option == 3) {
                if (authService.isManager()) manager.updateStudent();
                else System.out.println("You don't have a right to do it");

            } else if (option == 4) {
                System.out.println("Would you like to get a student by ID or name info?" + "\n"
                + "1 - by id" + "\n"
                + "2 - by name info" + "\n"
                + "3 - by study year" + "\n"
                        + "4 - by course code");
                int choice = inputValidator.readInt("Enter your choice: ", 1, 4);
                List<Student> result = new ArrayList<>();
                if (choice == 1) {
                    System.out.println("Enter an ID of a student you want to get: ");
                    int id = scan.nextInt();
                    scan.nextLine();
                    result = manager.getStudentById(id);
                } else if (choice == 2){
                    String query = inputValidator.readString("Enter some name details of a student you want to get: ");
                    result = manager.getStudentByNameInfo(query);
                } else if (choice == 3) {
                    int year = inputValidator.readInt("Enter study year: ", 1, 6);
                    result = manager.findByCourse(year);
                } else {
                    String courseCode = inputValidator.readString("Enter course code: ");
                    result = manager.findByCourseCode(courseCode);
                }
                if (result != null) {
                    System.out.println((result.size() != 1) ?  "-- STUDENTS FOUND --" : "-- STUDENT FOUND --");
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
        String email = inputValidator.readEmail("Enter your email");
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
        Faculty faculty = selectFaculty();

        try {
            Student newStudent = new Student(
                    firstName, lastName, fatherName, birthDate, email, phone,
                    recordBookId, year, courseCode, admYear, studyForm, status, faculty
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

    private Faculty selectFaculty() {
        System.out.println("Available Faculties:");

        Map<Integer, Faculty> availableFaculties = manager.getFaculties();

        if (availableFaculties.isEmpty()) {
            System.out.println("No faculties available in the registry!");
            return null;
        }

        for (Faculty f : availableFaculties.values()) {
            System.out.println(f.getId() + " - " + f.getName() + " (" + f.getShortName() + ")");
        }

        Faculty chosen = null;
        while (chosen == null) {
            int id = inputValidator.readInt("Enter Faculty ID: ", 1, Integer.MAX_VALUE);
            chosen = manager.getFacultyById(id);
            if (chosen == null) {
                System.out.println("Faculty with ID " + id + " not found. Try again.");
            }
        }
        return chosen;
    }

}
