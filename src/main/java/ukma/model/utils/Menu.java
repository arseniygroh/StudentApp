package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.Teacher;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.time.LocalDate;
import java.util.*;

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
            System.out.println("Please, select which registry you want to manipulate: " + "\n"
            + "0 - leave" + "\n"
            + "1 - student's registry" + "\n"
            + "2 - faculty's registy" + "\n");

            int mainMenuOption = inputValidator.readInt("Enter your option", 0, 2);

            if (mainMenuOption == 1) {
                while (true) {
                    System.out.println("Here are the options for student registry manipulations: " + "\n"
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
                        if (authService.isManager()) handleUpdateStudent();
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
                        System.out.println("Would you like to get students by ?" + "\n"
                                + "1 - by faculty" + "\n"
                                + "2 - by study year in ascending order");
                        int choice = inputValidator.readInt("Enter your option", 1, 2);
                        if (choice == 1) {
                            System.out.println("Here are all faculties: ");
                            for (int i = 1; i <= manager.getFaculties().size(); i++) {
                                System.out.println(i + " - " + manager.getFaculties().get(i).getName());
                            }
                            int facultyChoice = inputValidator.readInt("Choose your faculty", 1, Integer.MAX_VALUE);
                            Faculty f = manager.getFacultyById(facultyChoice);
                            manager.showAllStudentsInFaculty(f.getShortName());
                        } else if (choice == 2) {
                            manager.showAllStudents();
                        }
                    } else break;
                }
            } else if (mainMenuOption == 2) {
                System.out.println("Here are the options for faculty registry manipulations: " + "\n"
                + "0 - leave the menu" + "\n"
                + "1 - add a new faculty (manager only)" + "\n"
                + "2 - delete a faculty by id (manager only)" + "\n"
                + "3 - update a faculty (manager only)" + "\n"
                + "4 - show faculty or faculties" + "\n");

                int option = inputValidator.readInt("Enter your option", 0, 4);
                if (option == 1) {
                    if (authService.isManager()) createAndAddFaculty();
                    else System.out.println("You don't have a right to do it");
                } else if (option == 2) {
                    if (authService.isManager()) {
                        int id = inputValidator.readInt("Enter ID of the faculty you want to delete: ", 1, Integer.MAX_VALUE);
                        manager.deleteFaculty(id);
                    } else System.out.println("You don't have a right to do it");
                } else if (option == 3) {
                    if (authService.isManager()) handleUpdateFaculty();
                    else System.out.println("You don't have a right to do it");
                } else if (option == 4) {
                    System.out.println("Here are the options: " + "\n"
                    + "1 - by id" +"\n"
                    + "2 - all (sorted alphabetically)" +"\n");
                    int choice = inputValidator.readInt("Enter your option", 1, 2);
                    if (choice == 1) {
                        int id = inputValidator.readInt("Enter ID of the faculty you want to get: ", 1, Integer.MAX_VALUE);
                        System.out.println(manager.getFacultyById(id));
                    } else manager.showFacultiesAlphabeticallySorted();
                } else break;
            } else break;
        }
    }

    private void handleUpdateFaculty() {
        boolean isRunning = true;
        while (isRunning) {
            int id = inputValidator.readInt("Enter ID of the faculty you want to update: ", 1, Integer.MAX_VALUE);
            try {
                Faculty facultyToUpdate = manager.getFacultyById(id);
                while (true) {
                    System.out.println("What would you like to update?" + "\n"
                            + "0 - update another faculty" + "\n"
                            + "1 - update name" + "\n"
                            + "2 - update dean" + "\n"
                            + "3 - update email" + "\n"
                            + "4 - update phone" + "\n"
                            + "Q or q - quite" + "\n");

                    String optionStr = inputValidator.readString("Choose option: ").trim();

                    if (optionStr.equalsIgnoreCase("q")) {
                        isRunning = false;
                        break;
                    }
                    int option = -1;
                    try {
                        option = Integer.parseInt(optionStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number from 0 to 4 or 'q' to quit");
                        continue;
                    }

                    if (option < 0 || option > 4) continue;

                    if (option == 1) {
                        String name = inputValidator.readString("Enter new name: ");
                        facultyToUpdate.setName(name);
                        String shortName = inputValidator.readString("Enter short name: ");
                        facultyToUpdate.setShortName(shortName);
                    } else if (option == 2) {
                        facultyToUpdate.setDean(null);
                    } else if (option == 3) {
                        String email = inputValidator.readEmail("Enter new email: ");
                        facultyToUpdate.setEmail(email);
                    } else if (option == 4) {
                        String phone = inputValidator.readString("Enter new phone number: ");
                        while (phone.length() < 10) {
                            System.out.println("Phone number is too short!");
                            phone = inputValidator.readString("Enter new phone number: ");
                        }
                        facultyToUpdate.setPhone(phone);
                    } else break;
                    System.out.println("Faculty with ID " + id + " has been successfully updated");
                }
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                continue;
            }

            if (!isRunning) break;
            System.out.println("Would you like to update another one ? (yes - 1, no - 0): ");
            int ans = scan.nextInt();
            scan.nextLine();
            while (ans != 1 && ans != 0) {
                System.out.println("Invalid input! Try again: ");
                ans = scan.nextInt();
                scan.nextLine();
            }
            if (ans == 0) break;
        }
    }

    private void createAndAddFaculty() {
        System.out.println("\n--- ADDING NEW FACULTY ---");

        String name = inputValidator.readString("Enter faculty's name: ");
        String shortName = inputValidator.readString("Enter faculty's short name: ");
        Teacher dean = null;
        String email = inputValidator.readEmail("Enter email of the faculty");
        String phone = inputValidator.readString("Enter faculty's phone number: ");
        while (phone.length() < 10) {
            System.out.println("Phone number is too short!");
            phone = inputValidator.readString("Enter faculty's phone number: ");
        }

        try {
            Faculty faculty = new Faculty(name, shortName, dean, email, phone);
            manager.addFaculty(faculty);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
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

    public void handleUpdateStudent() {
        boolean isRunning = true;
        while (isRunning) {
            int studentId = inputValidator.readInt("Enter an id of a student you want to find: ", 1, Integer.MAX_VALUE);
            Optional<Student> optionalStudent = Optional.ofNullable(manager.getStudentById(studentId).get(0));
            if (optionalStudent.isPresent()) {
                Student studentToUpdate = optionalStudent.get();
                while (true) {
                    System.out.println("What would you like to update? " + "\n"
                            + "0 - update another student" + "\n"
                            + "1 - first name" + "\n"
                            + "2 - last name" + "\n"
                            + "3 - father name" + "\n"
                            + "4 - course code" + "\n"
                            + "5 - study form" + "\n"
                            + "6 - student status" + "\n"
                            + "7 - email address" + "\n"
                            + "8 - phone number" + "\n"
                            + "Q or q - quit" + "\n");

                    String optionStr = inputValidator.readString("Choose option: ").trim();

                    if (optionStr.equalsIgnoreCase("q")) {
                        isRunning = false;
                        break;
                    }
                    int option = -1;
                    try {
                        option = Integer.parseInt(optionStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number from 0 to 8 or 'q' to quit");
                        continue;
                    }

                    if (option < 0 || option > 8) continue;

                    if (option == 1) {
                        String name = inputValidator.readString("Enter new name for the student");
                        studentToUpdate.setFirstName(name);
                    } else if (option == 2) {
                        String lastName = inputValidator.readString("Enter new last name for the student");
                        studentToUpdate.setLastName(lastName);
                    } else if (option == 3) {
                        String fatherName = inputValidator.readString("Enter new fathers name for the student");
                        studentToUpdate.setFatherName(fatherName);
                    } else if (option == 4) {
                        String courseCode = inputValidator.readString("Enter new course code for the student");
                        studentToUpdate.setCourseCode(courseCode);
                    } else if (option == 5) {
                        updateStudyForm(studentToUpdate);
                    } else if (option == 6) {
                        updateStudentStatus(studentToUpdate);
                    } else if (option == 7) {
                        String email = inputValidator.readEmail("Enter a new email address for the student");
                        studentToUpdate.setEmail(email);
                    } else if (option == 8) {
                        System.out.println("Enter a new phone number for the student: ");
                        String phone = scan.nextLine();
                        studentToUpdate.setPhoneNumber(phone);
                    } else {
                        break;
                    }
                    System.out.println("Student with an id = " + studentToUpdate.getId() + " was successfully updated!");
                }
            } else {
                System.out.println("Student not found!");
            }

            if (!isRunning) break;

            System.out.println("Would you like to update another one ? (yes - 1, no - 0): ");
            int ans = scan.nextInt();
            scan.nextLine();
            while (ans != 1 && ans != 0) {
                System.out.println("Invalid input! Try again: ");
                ans = scan.nextInt();
                scan.nextLine();
            }
            if (ans == 0) break;
        }
    }

    private void updateStudentStatus(Student s) {
        System.out.println("Choose new status");
        StudentStatus[] statuses = StudentStatus.values();
        for (int i = 0; i < statuses.length; i++) {
            System.out.println(i + " - " + statuses[i]);
        }

        System.out.print("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();

        if (choice >= 0 && choice < statuses.length) {
            s.setStatus(statuses[choice]);
            System.out.println("Status updated");
        } else {
            System.out.println("Incorrect choice");
        }
    }

    private void updateStudyForm(Student s) {
        System.out.println("Choose new study form");
        StudyForm[] forms = StudyForm.values();
        for (int i = 0; i < forms.length; i++) {
            System.out.println(i + " - " + forms[i]);
        }
        System.out.print("Your choice: ");
        int choice = scan.nextInt();
        scan.nextLine();

        if (choice >= 0 && choice < forms.length) {
            s.setStudyForm(forms[choice]);
            System.out.println("Study form is updated");
        } else {
            System.out.println("Incorrect choice");
        }
    }
}
