package ukma.model.utils;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.Teacher;
import ukma.model.Department;
import ukma.model.enums.Degree;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.exception.*;

import java.time.LocalDate;
import java.util.*;

public class Menu {
    private final Scanner scan = new Scanner(System.in);
    private final RegistryManager manager;
    private final ConsoleInput inputValidator;
    private final AuthorizationService authService;

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
                    + "2 - faculty's registy" + "\n"
                    + "3 - teacher's registry" + "\n"
                    + "4 - department's registry" + "\n");

            int mainMenuOption = inputValidator.readInt("Enter your option", 0, 4);

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
                    if (option == 1) {
                        if (authService.isManager()) createAndAddStudent();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager()) {
                            System.out.println("Enter an ID of a student you want to delete: ");
                            long id = scan.nextLong();
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
                                if (choice == 2) {
                                    for (Student s : result) {
                                        System.out.println(s);
                                        System.out.println("---------------------");
                                    }
                                } else {
                                    System.out.printf("| %-5s | %-50s | %-25s |%n", "ID", "Student Name", "Course Info");
                                    System.out.println("-".repeat(89));
                                    for (Student s : result) {
                                        System.out.println(s.toShortString());
                                    }
                                }
                            } else {
                                System.out.println("No students found matching your criteria.");
                            }
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
                            try {
                                Faculty f = manager.getFacultyById(facultyChoice);
                                manager.showAllStudentsInFaculty(f.getShortName());
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        } else if (choice == 2) {
                            manager.showAllStudents();
                        }
                    } else break;
                }
            } else if (mainMenuOption == 2) {
                while(true) {
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
                            + "1 - add a new teacher (manager only)" + "\n"
                            + "2 - delete a teacher by id (manager only)" + "\n"
                            + "3 - get teacher by id\n"
                            + "4 - show all teachers\n"
                            + "5 - update teacher (manager only)\n"
                    );

                    int option = inputValidator.readInt("Enter your option", 0, 5);
                    if (option == 1) {
                        if (authService.isManager()) createAndAddTeacher();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager()) {
                            long id = inputValidator.readInt("Enter ID of the teacher you want to delete: ", 1, Integer.MAX_VALUE);
                            manager.deleteTeacher(id);
                        } else System.out.println("You don't have a right to do it");
                    } else if (option == 3) {
                        long id = inputValidator.readInt("Enter ID of the teacher you want to get: ", 1, Integer.MAX_VALUE);
                        try {
                            System.out.println(manager.getTeacherById(id));
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else if (option == 4) {
                        manager.showAllTeachers();
                    } else if (option == 5) {
                        if (authService.isManager()) handleUpdateTeacher();
                        else System.out.println("You don't have a right to do it");
                    } else break;
                }
            } else if (mainMenuOption == 4) {
                while(true) {
                    System.out.println("Here are the options for department registry manipulations: " + "\n"
                            + "0 - leave the menu" + "\n"
                            + "1 - add a new department (manager only)" + "\n"
                            + "2 - delete a department by id (manager only)" + "\n"
                            + "3 - get department by id\n"
                            + "4 - show all departments\n"
                            + "5 - update department (manager only)\n"
                    );

                    int option = inputValidator.readInt("Enter your option", 0, 5);
                    if (option == 1) {
                        if (authService.isManager()) createAndAddDepartment();
                        else System.out.println("You don't have a right to do it");
                    } else if (option == 2) {
                        if (authService.isManager()) {
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
                        if (authService.isManager()) handleUpdateDepartment();
                        else System.out.println("You don't have a right to do it");
                    } else break;
                }
            } else break;
        }
    }

    private long selectAvailableTeacherId(List<Teacher> teachers, boolean isDean) {
        System.out.println("Available Teachers:");
        System.out.printf("| %-5s | %-50s | %-25s |%n", "ID", "Teacher Name", "Occupation");
        System.out.println("-".repeat(89));

        teachers.forEach(teacher -> System.out.println(teacher.toShortString()));

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

        try {
            Teacher teacher = new Teacher(firstName, lastName, fatherName, birthDate, email, phone, degree, occupation, academicRank, hireDate, rate);
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
                        System.out.println("Please enter a number from 0 to 9 or 'q' to quit");
                        continue;
                    }

                    if (option < 0 || option > 9) continue;

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

        System.out.printf("| %-5s | %-50s | %-25s |%n", "ID", "Faculty Name", "Dean");
        System.out.println("-".repeat(89));
        for (Faculty f : availableFaculties.values()) {
            System.out.println(f.toShortString());
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