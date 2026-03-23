package ukma.model.utils;

import ukma.model.*;
import ukma.model.enums.Degree;
import ukma.model.enums.Role;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.exception.*;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Predicate;

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
            if (authService.isAdmin()) {
                System.out.println("Since you are administrator you can also manipulate user's registry: " + "\n"
                        + "0 - leave" + "\n"
                        + "1 - student's registry" + "\n"
                        + "2 - faculty's registy" + "\n"
                        + "3 - teacher's registry" + "\n"
                        + "4 - department's registry" + "\n"
                        + "5 - user's registry" + "\n");
            } else {
                System.out.println("Please, select which registry you want to manipulate: " + "\n"
                        + "0 - leave" + "\n"
                        + "1 - student's registry" + "\n"
                        + "2 - faculty's registy" + "\n"
                        + "3 - teacher's registry" + "\n"
                        + "4 - department's registry" + "\n");
            }
            int mainMenuOption;
            if (authService.isAdmin()) {
                mainMenuOption = inputValidator.readInt("Enter your option", 0, 5);
            } else mainMenuOption = inputValidator.readInt("Enter your option", 0, 4);

            if (mainMenuOption == 1) {
                while (true) {
                    System.out.println("Here are the options for student registry manipulations: " + "\n"
                            + "0 - Leave the menu" + "\n"
                            + "1 - Add a student (manager, admin only)" + "\n"
                            + "2 - Delete a student (manager, admin only)" + "\n"
                            + "3 - Update a student (manager, admin only)" + "\n"
                            + "4 - Get a student" + "\n"
                            + "5 - Show all students" + "\n");
                    int option = inputValidator.readInt("Select your option: ", 0, 5);
                    if (option == 1) {
                        if (authService.isManager() || authService.isAdmin()) createAndAddStudent();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager() || authService.isAdmin()) {
                            System.out.println("Enter an ID of a student you want to delete: ");
                            long id = scan.nextLong();
                            scan.nextLine();
                            manager.deleteStudent(id);
                        } else System.out.println("You don't have a right to do it");

                    } else if (option == 3) {
                        if (authService.isManager() || authService.isAdmin()) handleUpdateStudent();
                        else System.out.println("You don't have a right to do it");

                    } else if (option == 4) {
                        System.out.println("How would you like to get a student?" + "\n"
                                + "1 - by id" + "\n"
                                + "2 - by name info" + "\n"
                                + "3 - by study year" + "\n"
                                + "4 - by course code");
                        int choice = inputValidator.readInt("Enter your choice: ", 1, 4);
                        if (choice == 1) {
                            System.out.println("Enter an ID of a student you want to get: ");
                            long id = scan.nextLong();
                            scan.nextLine();
                            try {
                                Student s = manager.getStudentById(id);
                                System.out.println("-- STUDENT FOUND --");
                                System.out.println(s);
                                System.out.println("---------------------");
                            } catch (ukma.model.exception.StudentNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            List<Student> result = new ArrayList<>();
                            if (choice == 2){
                                String query = inputValidator.readString("Enter some name details of a student you want to get: ");
                                result = manager.getStudentByNameInfo(query);
                            } else if (choice == 3) {
                                int year = inputValidator.readInt("Enter study year: ", 1, 6);
                                result = manager.findByCourse(year);
                            } else {
                                String courseCode = inputValidator.readString("Enter course code: ");
                                result = manager.findByCourseCode(courseCode);
                            }
                            if (!result.isEmpty()) {
                                System.out.println("-- STUDENTS FOUND --");
                                for (Student s : result) {
                                    System.out.println(s);
                                    System.out.println("---------------------");
                                }
                            } else {
                                System.out.println("No students found matching your criteria.");
                            }
                        }
                    } else if (option == 5) {
                        System.out.println("Would you like to get students by ?" + "\n"
                                + "1 - by faculty" + "\n"
                                + "2 - by study year in ascending order" + "\n"
                                + "3 - by department (sorted by study year)" + "\n"
                                + "4 - by department (sorted alphabetically)" + "\n"
                                + "5 - by department (specific study year)"
                        );
                        int choice = inputValidator.readInt("Enter your option", 1, 5);
                        if (choice == 1) {
                            System.out.println("Here are all faculties: ");
                            for (int i = 1; i <= manager.getFaculties().size(); i++) {
                                System.out.println(i + " - " + manager.getFaculties().get(i).getName());
                            }
                            int facultyChoice = inputValidator.readInt("Choose your faculty", 1, Integer.MAX_VALUE);
                            try {
                                Faculty f = manager.getFacultyById(facultyChoice);
                                manager.showAllStudentsInFaculty(f.getShortName());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if (choice == 2) {
                            manager.showAllStudentsSortedByStudyYear();
                        } else if (choice == 3) {
                            Department department = selectDepartment();
                            if (department != null) manager.showAllStudentsInDepartmentSortedByStudyYear(department);
                        } else if (choice == 4) {
                            Department department = selectDepartment();
                            if (department != null) manager.showAllStudentsInDepartmentSortedAlphabetically(department);
                        } else if (choice == 5) {
                            Department department = selectDepartment();
                            int studyYear = inputValidator.readInt("Enter a study year (1-6): ", 1, 6);
                            if (department != null) manager.showAllStudentsOfSameCourseInDepartmentSortedAlphabetically(department, studyYear);
                        }
                    } else break;
                }
            } else if (mainMenuOption == 2) {
                while(true) {
                    System.out.println("Here are the options for faculty registry manipulations: " + "\n"
                            + "0 - leave the menu" + "\n"
                            + "1 - add a new faculty (manager, admin only)" + "\n"
                            + "2 - delete a faculty by id (manager, admin only)" + "\n"
                            + "3 - update a faculty (manager, admin only)" + "\n"
                            + "4 - show faculty or faculties" + "\n");

                    int option = inputValidator.readInt("Enter your option", 0, 4);
                    if (option == 1) {
                        if (authService.isManager() || authService.isAdmin()) createAndAddFaculty();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager() || authService.isAdmin()) {
                            int id = inputValidator.readInt("Enter ID of the faculty you want to delete: ", 1, Integer.MAX_VALUE);
                            manager.deleteFaculty(id);
                        } else System.out.println("You don't have a right to do it");
                    } else if (option == 3) {
                        if (authService.isManager() || authService.isAdmin()) handleUpdateFaculty();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 4) {
                        System.out.println("Here are the options: " + "\n"
                                + "1 - by id" +"\n"
                                + "2 - all (sorted alphabetically)" +"\n");
                        int choice = inputValidator.readInt("Enter your option", 1, 2);
                        if (choice == 1) {
                            int id = inputValidator.readInt("Enter ID of the faculty you want to get: ", 1, Integer.MAX_VALUE);
                            try {
                                System.out.println(manager.getFacultyById(id));
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else manager.showFacultiesAlphabeticallySorted();
                    } else break;
                }
            } else if (mainMenuOption == 3) {
                while(true) {
                    System.out.println("Here are the options for teacher registry manipulations: " + "\n"
                            + "0 - leave the menu" + "\n"
                            + "1 - add a new teacher (manager, admin only)" + "\n"
                            + "2 - delete a teacher by id (manager, admin only)" + "\n"
                            + "3 - get teacher\n"
                            + "4 - show all teachers\n"
                            + "5 - update teacher (manager, admin only)\n"
                    );

                    int option = inputValidator.readInt("Enter your option", 0, 5);
                    if (option == 1) {
                        if (authService.isManager() || authService.isAdmin()) createAndAddTeacher();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager() || authService.isAdmin()) {
                            long id = inputValidator.readInt("Enter ID of the teacher you want to delete: ", 1, Integer.MAX_VALUE);
                            manager.deleteTeacher(id);
                        } else System.out.println("You don't have a right to do it");
                    } else if (option == 3) {
                        System.out.println("How would you like to get a teacher ?" + "\n"
                                + "1 - by id" + "\n"
                                + "2 - by name info" + "\n"
                        );
                        int choice = inputValidator.readInt("Enter your choice: ", 1, 2);
                        if (choice == 1) {
                            long id = inputValidator.readInt("Enter ID of the teacher you want to get: ", 1, Integer.MAX_VALUE);
                            try {
                                System.out.println(manager.getTeacherById(id));
                            } catch (TeacherNotFoundException e) {
                                System.out.println("Error: " + e.getMessage());
                            }
                        } else {
                            String query = inputValidator.readString("Enter some name details of a teacher you want to get: ");
                            List<Teacher> result = manager.getTeachersByNameInfo(query);
                            result.forEach(teacher -> {
                                System.out.println(teacher);
                                System.out.println("================================================");
                            });
                        }
                    } else if (option == 4) {
                        System.out.println("--- TEACHER REPORTS --- \n"
                                + "1 - All teachers (unsorted)\n"
                                + "2 - Faculty teachers (sorted alphabetically)\n"
                                + "3 - Department teachers (sorted alphabetically)\n");
                        int choice = inputValidator.readInt("Enter your option: ", 1, 3);
                        if (choice == 1) {
                            manager.showAllTeachers();
                        } else if (choice == 2) {
                            Faculty faculty = selectFaculty();
                            if (faculty != null) manager.showAllTeachersInFaculty(faculty.getShortName());
                        } else if (choice == 3) {
                            Department d = selectDepartment();
                            if (d != null) manager.showDepartmentTeachersAlphabetically(d);
                        }
                    } else if (option == 5) {
                        if (authService.isManager() || authService.isAdmin()) handleUpdateTeacher();
                        else System.out.println("You don't have a right to do it");
                    } else break;
                }
            } else if (mainMenuOption == 4) {
                while(true) {
                    System.out.println("Here are the options for department registry manipulations: " + "\n"
                            + "0 - leave the menu" + "\n"
                            + "1 - add a new department (manager, admin only)" + "\n"
                            + "2 - delete a department by id (manager, admin only)" + "\n"
                            + "3 - get department by id\n"
                            + "4 - show all departments\n"
                            + "5 - update department (manager, admin only)\n"
                    );

                    int option = inputValidator.readInt("Enter your option", 0, 5);
                    if (option == 1) {
                        if (authService.isManager() || authService.isAdmin()) createAndAddDepartment();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager() || authService.isAdmin()) {
                            int id = inputValidator.readInt("Enter ID of the department you want to delete: ", 1, Integer.MAX_VALUE);
                            manager.deleteDepartment(id);
                        } else System.out.println("You don't have a right to do it");
                    } else if (option == 3) {
                        int id = inputValidator.readInt("Enter ID of the department you want to get: ", 1, Integer.MAX_VALUE);
                        try {
                            System.out.println(manager.getDepartmentById(id));
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else if (option == 4) {
                        manager.showAllDepartments();
                    } else if (option == 5) {
                        if (authService.isManager() || authService.isAdmin()) handleUpdateDepartment();
                        else System.out.println("You don't have a right to do it");
                    } else break;
                }
            }
            if (authService.isAdmin()) {
                if (mainMenuOption == 5) {
                    while (true) {
                        System.out.println("Here are the options for user's registry manipulations: \n"
                                + "0 - leave the menu\n"
                                + "1 - register a new user\n"
                                + "2 - edit an existing user\n"
                        );
                        int option = inputValidator.readInt("Enter your option", 0, 2);
                        if (option == 1) {
                            registerUser();
                        } else if (option == 2) {
                            editUser();
                        } else break;
                    }
                }
            }
            else break;
        }
    }

    private void editUser() {
        Map<String, User> currentUsers = authService.getUsers();
        if (currentUsers.isEmpty()) {
            System.out.println("There are no students to edit");
            return;
        }
        List<User> users = new ArrayList<>(currentUsers.values());
        System.out.println("Available users: ");
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            String status = u.isBlocked() ? "BLOCKED" : "ACTIVE";
            System.out.println(i + " - " + status + " " + u.getEmail() + " " + u.getRole());
        }

        int userIndex = inputValidator.readInt("Enter user index: ", 0, users.size() - 1);

        while (true) {
            User userToUpdate = users.get(userIndex);
            currentUsers.remove(userToUpdate.getEmail());

            System.out.println("Here are the options for editing: \n"
                    + "0 - exit\n"
                    + "1 - email\n"
                    + "2 - password\n"
                    + "3 - role\n"
                    + "4 - change block status (block/unblock)\n"
            );
            int option = inputValidator.readInt("Enter your option: ", 0, 4);

            if (option == 1) {
                String oldEmail = userToUpdate.getEmail();
                String email;
                while (true) {
                    try {
                        email = inputValidator.readEmail("Enter new email: ");
                        manager.storeEmail(email);
                        break;
                    } catch (IllegalArgumentException e) {
                        System.out.println(e.getMessage() + " Try another one!");
                    }
                }
                if (!oldEmail.equals(email)) manager.removeEmail(oldEmail);
                userToUpdate.setEmail(email);

            } else if (option == 2) {
                String password = inputValidator.readString("Enter password: ");
                userToUpdate.setPassword(password);
            } else if (option == 3) {
                Role role = getSelectedRole();
                userToUpdate.setRole(role);

            } else if (option == 4) {
                if (userToUpdate.isBlocked()) {
                    System.out.println("User is currently BLOCKED. Unblocking...");
                    userToUpdate.setBlocked(false);
                } else {
                    System.out.println("User is currently ACTIVE. Blocking...");
                    userToUpdate.setBlocked(true);
                }

            } else {
                currentUsers.put(userToUpdate.getEmail(), userToUpdate);
                break;
            }

            currentUsers.put(userToUpdate.getEmail(), userToUpdate);
            authService.updateUsersFile();

            System.out.println("User successfully updated!");
            int ans = inputValidator.readInt("Would you like to update another field? (yes - 1, no - 0): ", 0, 1);
            if (ans == 0) break;
        }
    }

    private void registerUser() {
        String email = inputValidator.readEmail("Enter user's email:");
        String password = inputValidator.readString("Enter user's password:");
        Role role = getSelectedRole();

        try {
            boolean isAdded = authService.register(email, password, role);
            if (isAdded) System.out.println("User was successfully registred");
            else System.out.println("Such user already exists");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private Role getSelectedRole() {
        System.out.println("Here are available roles: ");
        for (int i = 0; i < Role.values().length; i++) {
            System.out.println(i + " - " + Role.values()[i]);
        }
        int roleIndex = inputValidator.readInt("Enter role's index: ", 0, Role.values().length - 1);
        Role role = Role.values()[roleIndex];
        return role;
    }

    private long selectAvailableTeacherId(List<Teacher> teachers, boolean isDean) {
        System.out.println("Available Teachers:");
        teachers.forEach(teacher -> System.out.println("ID: " + teacher.getId() + " - " + teacher.getFullName()));
        String position = isDean ? "Dean" : "Head";
        return inputValidator.readInt("Enter Teacher ID to be " + position, 1, Integer.MAX_VALUE);
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
                        List<Teacher> availableTeachers = manager.getAvailableTeachersForDean();
                        if (availableTeachers.isEmpty()) {
                            System.out.println("There are no available teachers. Please add more first");
                            return;
                        }
                        long teacherId = selectAvailableTeacherId(availableTeachers, true);
                        try {
                            Teacher dean = manager.getTeacherById(teacherId);
                            facultyToUpdate.setDean(dean);
                            System.out.println("Dean updated!");
                        } catch (Exception e) {
                            System.out.println("Teacher not found. Dean not updated.");
                        }
                    } else if (option == 3) {
                        String oldEmail = facultyToUpdate.getEmail();
                        String email;
                        while (true) {
                            try {
                                email = inputValidator.readEmail("Enter new email: ");
                                manager.storeEmail(email);
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage() + " Try another one!");
                            }
                        }
                        if (!oldEmail.equals(email)) manager.removeEmail(oldEmail);
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
            } catch (FacultyNotFoundException e) {
                System.out.println(e.getMessage());
                continue;
            }

            if (!isRunning) break;
            int ans = inputValidator.readInt("Would you like to update another field? (yes - 1, no - 0): ", 0, 1);
            if (ans == 0) break;
        }
    }

    private void createAndAddFaculty() {
        System.out.println("\n--- ADDING NEW FACULTY ---");

        String name = inputValidator.readString("Enter faculty's name: ");
        String shortName = inputValidator.readString("Enter faculty's short name: ");
        List<Teacher> availableTeachers = manager.getAvailableTeachersForDean();
        if (availableTeachers.isEmpty()) {
            System.out.println("There are no available teachers. Please add more first");
            return;
        }
        long teacherId = selectAvailableTeacherId(availableTeachers, true);
        Teacher dean = null;
        try {
            dean = manager.getTeacherById(teacherId);
        } catch (Exception e) {
            System.out.println("Teacher not found");
        }
        String email;
        while (true) {
            try {
                email = inputValidator.readEmail("Enter email of the faculty");
                manager.storeEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try another one");
            }
        }
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
        String email;
        while (true) {
            try {
                email = inputValidator.readEmail("Enter email of the student");
                manager.storeEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try another one");
            }
        }
        String phone = inputValidator.readString("Phone: ");
        while (phone.length() < 10) {
            System.out.println("Phone number is too short!");
            phone = inputValidator.readString("Phone: ");
        }
        String recordBookId = inputValidator.readString("Record Book ID (Ticket): ");
        int year = inputValidator.readInt("Study Year (1-6): ", 1, 6);
        String courseCode = inputValidator.readString("Course Code: ");
        int admYear = inputValidator.readInt("Admission Year: ", 1991, 2025);

        StudyForm studyForm = selectStudyForm();
        StudentStatus status = selectStudentStatus();
        Faculty faculty = selectFaculty();
        Department department = selectDepartment();

        try {
            Student newStudent = new Student(
                    firstName, lastName, fatherName, birthDate, email, phone,
                    recordBookId, year, courseCode, admYear, studyForm, status, faculty, department
            );

            manager.addStudent(newStudent);
            System.out.println("Student added successfully!");

        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    private void createAndAddTeacher() {
        System.out.println("\n--- ADDING NEW TEACHER ---");
        String firstName = inputValidator.readString("First Name: ");
        String lastName = inputValidator.readString("Last Name: ");
        String fatherName = inputValidator.readString("Father's Name: ");
        LocalDate birthDate = inputValidator.readDate("Enter birth date");
        String email;
        while (true) {
            try {
                email = inputValidator.readEmail("Enter email of the teacher");
                manager.storeEmail(email);
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Try another one");
            }
        }
        String phone = inputValidator.readString("Phone: ");
        while (phone.length() < 10) {
            System.out.println("Phone number is too short!");
            phone = inputValidator.readString("Phone: ");
        }

        System.out.println("Choose degree:");
        Degree[] degrees = Degree.values();
        for (int i = 0; i < degrees.length; i++) {
            System.out.println(i + " - " + degrees[i]);
        }
        int degChoice = inputValidator.readInt("Your choice: ", 0, degrees.length - 1);
        Degree degree = degrees[degChoice];

        String occupation = inputValidator.readString("Occupation: ");
        String academicRank = inputValidator.readString("Academic Rank: ");
        LocalDate hireDate = inputValidator.readDate("Hire Date");

        System.out.println("Rate (e.g. 1.0 or 0.5): ");
        double rate = scan.nextDouble();
        scan.nextLine();
        Department department = selectDepartment();

        try {
            Teacher teacher = new Teacher(firstName, lastName, fatherName, birthDate, email, phone, degree, occupation, academicRank, hireDate, rate, department);
            manager.addTeacher(teacher);
            System.out.println("Teacher added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleUpdateTeacher() {
        boolean isRunning = true;
        while (isRunning) {
            long teacherId = inputValidator.readInt("Enter an id of a teacher you want to find: ", 1, Integer.MAX_VALUE);
            try {
                Teacher teacherToUpdate = manager.getTeacherById(teacherId);
                while (true) {
                    System.out.println("What would you like to update? \n"
                            + "0 - update another teacher\n"
                            + "1 - first name\n"
                            + "2 - last name\n"
                            + "3 - father name\n"
                            + "4 - email address\n"
                            + "5 - phone number\n"
                            + "6 - degree\n"
                            + "7 - occupation\n"
                            + "8 - academic rank\n"
                            + "9 - rate\n"
                            + "10 - department\n"
                            + "Q or q - quit\n");

                    String optionStr = inputValidator.readString("Choose option: ").trim();

                    if (optionStr.equalsIgnoreCase("q")) {
                        isRunning = false;
                        break;
                    }
                    int option = -1;
                    try {
                        option = Integer.parseInt(optionStr);
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a number from 0 to 10 or 'q' to quit");
                        continue;
                    }

                    if (option < 0 || option > 10) continue;

                    if (option == 1) {
                        String name = inputValidator.readString("Enter new first name: ");
                        teacherToUpdate.setFirstName(name);
                    } else if (option == 2) {
                        String lastName = inputValidator.readString("Enter new last name: ");
                        teacherToUpdate.setLastName(lastName);
                    } else if (option == 3) {
                        String fatherName = inputValidator.readString("Enter new father's name: ");
                        teacherToUpdate.setFatherName(fatherName);
                    } else if (option == 4) {
                        String oldEmail = teacherToUpdate.getEmail();
                        String email;
                        while (true) {
                            try {
                                email = inputValidator.readEmail("Enter new email: ");
                                manager.storeEmail(email);
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage() + " Try another one!");
                            }
                        }
                        if (!oldEmail.equals(email)) manager.removeEmail(oldEmail);
                        teacherToUpdate.setEmail(email);
                    } else if (option == 5) {
                        String phone = inputValidator.readString("Enter new phone number: ");
                        while (phone.length() < 10) {
                            System.out.println("Phone number is too short");
                            phone = inputValidator.readString("Enter new phone number: ");
                        }
                        teacherToUpdate.setPhoneNumber(phone);
                    } else if (option == 6) {
                        System.out.println("Choose new degree:");
                        Degree[] degrees = Degree.values();
                        for (int i = 0; i < degrees.length; i++) {
                            System.out.println(i + " - " + degrees[i]);
                        }
                        int degChoice = inputValidator.readInt("Your choice: ", 0, degrees.length - 1);
                        teacherToUpdate.setDegree(degrees[degChoice]);
                    } else if (option == 7) {
                        String occupation = inputValidator.readString("Enter new occupation: ");
                        teacherToUpdate.setOccupation(occupation);
                    } else if (option == 8) {
                        String academicRank = inputValidator.readString("Enter new academic rank: ");
                        teacherToUpdate.setAcademicRank(academicRank);
                    } else if (option == 9) {
                        System.out.println("Enter new rate (e.g. 1.0 or 0.5): ");
                        double rate = scan.nextDouble();
                        scan.nextLine();
                        teacherToUpdate.setRate(rate);
                    } else if (option == 10) {
                        Department department = selectDepartment();
                        if (department != null) {
                            teacherToUpdate.setDepartment(department);
                        }
                    } else {
                        break;
                    }
                    System.out.println("Teacher with ID " + teacherId + " has been successfully updated!");
                }
            } catch (TeacherNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            if (!isRunning) break;
            int ans = inputValidator.readInt("Would you like to update another field? (yes - 1, no - 0): ", 0, 1);
            if (ans == 0) break;
        }
    }

    private void createAndAddDepartment() {
        System.out.println("\n--- ADDING NEW DEPARTMENT ---");
        String name = inputValidator.readString("Department Name: ");
        String location = inputValidator.readString("Location: ");

        Faculty faculty = selectFaculty();
        if (faculty == null) return;

        List<Teacher> availableTeachers = manager.getAvailableTeachersForHead();
        if (availableTeachers.isEmpty()) {
            System.out.println("There are no available teachers. Please add more first");
            return;
        }
        long teacherId = selectAvailableTeacherId(availableTeachers, false);
        Teacher head = null;
        try {
            head = manager.getTeacherById(teacherId);
        } catch (Exception e) {
            System.out.println("Teacher not found");
        }

        try {
            Department dept = new Department(name, faculty, head, location);
            manager.addDepartment(dept);
            System.out.println("Department added successfully!");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }

    public void handleUpdateDepartment() {
        boolean isRunning = true;
        while (isRunning) {
            int deptId = inputValidator.readInt("Enter ID of the department you want to update: ", 1, Integer.MAX_VALUE);
            try {
                Department deptToUpdate = manager.getDepartmentById(deptId);
                while (true) {
                    System.out.println("What would you like to update?\n"
                            + "0 - update another department\n"
                            + "1 - update name\n"
                            + "2 - update location\n"
                            + "3 - update faculty\n"
                            + "4 - update head (Head of Department)\n"
                            + "Q or q - quit\n");

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
                        deptToUpdate.setName(name);
                    } else if (option == 2) {
                        String location = inputValidator.readString("Enter new location: ");
                        deptToUpdate.setLocation(location);
                    } else if (option == 3) {
                        Faculty newFaculty = selectFaculty();
                        if (newFaculty != null) {
                            deptToUpdate.setFaculty(newFaculty);
                            System.out.println("Faculty updated!");
                        }
                    } else if (option == 4) {
                        List<Teacher> availableTeachers = manager.getAvailableTeachersForHead();
                        if (availableTeachers.isEmpty()) {
                            System.out.println("There are no available teachers for Head position. Please add more first");
                            continue;
                        }
                        long teacherId = selectAvailableTeacherId(availableTeachers, false);
                        try {
                            Teacher head = manager.getTeacherById(teacherId);
                            deptToUpdate.setHead(head);
                            System.out.println("Head of department updated!");
                        } catch (Exception e) {
                            System.out.println("Teacher not found. Head not updated.");
                        }
                    } else break;

                    System.out.println("Department with ID " + deptId + " has been successfully updated!");
                }
            } catch (DepartmentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            if (!isRunning) break;
            int ans = inputValidator.readInt("Would you like to update another field? (yes - 1, no - 0): ", 0, 1);
            if (ans == 0) break;
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
            try {
                chosen = manager.getFacultyById(id);
            } catch (Exception e) {
                System.out.println("Faculty with ID " + id + " not found. Try again.");
            }
        }
        return chosen;
    }

    private Department selectDepartment() {
        System.out.println("Available departments:");
        Map<Integer, Department> availableDepartments = manager.getDepartments();

        if (availableDepartments.isEmpty()) {
            System.out.println("There are no available departments");
            return null;
        }

        for (Department d : availableDepartments.values()) {
            System.out.println(d.getId() + " - " + d.getName());
        }

        Department chosen = null;
        while (chosen == null) {
            int id = inputValidator.readInt("Enter Department ID: ", 1, Integer.MAX_VALUE);
            try {
                chosen = manager.getDepartmentById(id);
            } catch (DepartmentNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        return chosen;
    }

    public void handleUpdateStudent() {
        boolean isRunning = true;
        while (isRunning) {
            long studentId = inputValidator.readInt("Enter an id of a student you want to find: ", 1, Integer.MAX_VALUE);
            try {
                Student studentToUpdate = manager.getStudentById(studentId);

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
                            + "9 - study year" + "\n"
                            + "10 - faculty" + "\n"
                            + "11 - department" + "\n"
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
                        System.out.println("Please enter a number from 0 to 11 or 'q' to quit");
                        continue;
                    }

                    if (option < 0 || option > 11) continue;

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
                        String email;
                        String oldEmail = studentToUpdate.getEmail();
                        while (true) {
                            try {
                                email = inputValidator.readEmail("Enter new email of the student");
                                manager.storeEmail(email);
                                break;
                            } catch (IllegalArgumentException e) {
                                System.out.println(e.getMessage() + " Try another one");
                            }
                        }
                        if (!oldEmail.equals(email)) manager.removeEmail(oldEmail);
                        studentToUpdate.setEmail(email);
                    } else if (option == 8) {
                        System.out.println("Enter a new phone number for the student: ");
                        String phone = scan.nextLine();
                        studentToUpdate.setPhoneNumber(phone);
                    } else if (option == 9) {
                        int year = inputValidator.readInt("Enter new study year (1-6): ", 1, 6);
                        studentToUpdate.setStudyYear(year);
                    } else if (option == 10) {
                        Faculty newFaculty = selectFaculty();
                        if (newFaculty != null) {
                            studentToUpdate.setFaculty(newFaculty);
                        }
                    } else if (option == 11) {
                        Department newDept = selectDepartment();
                        if (newDept != null) {
                            studentToUpdate.setDepartment(newDept);
                        }
                    } else {
                        break;
                    }
                    System.out.println("Student with an id = " + studentToUpdate.getId() + " was successfully updated!");
                }
            } catch (StudentNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
                continue;
            }

            if (!isRunning) break;

            int ans = inputValidator.readInt("Would you like to update another field? (yes - 1, no - 0): ", 0, 1);
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