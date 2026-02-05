package ukma.model.utils;

import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;

import java.util.Arrays;
import java.util.Scanner;

public class RegistryManager {
    private Scanner scan = new Scanner(System.in);
    private Student[] students = new Student[20];
    private int studentCount = 0;

    public void addStudent(Student student) {
        if (studentCount < students.length) {
            students[studentCount++] = student;
        } else {
            Student[] studentsExpanded = new Student[students.length * 2];
            studentsExpanded = Arrays.copyOf(students, studentsExpanded.length);
            studentsExpanded[studentCount++] = student;
            students = studentsExpanded;
        }
    }

    public void showAllStudents() {
        for (int i = 0; i < studentCount; i++) {
            System.out.println(students[i]);
            System.out.println("===========================================================");
        }
    }

    public void deleteStudent(int id) {
        int indexToRemove = -1;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove == -1) {
            System.out.println("Student with such ID " + id + " is not found");
            return;
        }
        for (int i = indexToRemove; i < studentCount - 1; i++) {
            students[i] = students[i + 1];
        }
        students[--studentCount] = null;
        System.out.println("Student with an ID " + id + " was successfully removed");
    }


    public Student getStudentById(int id) {
        Student s = null;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getId() == id) {
                s = students[i];
                break;
            }
        }
        if (s == null) System.out.println("Student with " + id + " is no found!");
        return s;
    }

    public Student getStudentByNameInfo(String query) {
        Student s = null;
        for (int i = 0; i < studentCount; i++) {
            if (students[i].getFullName().toLowerCase().contains(query.toLowerCase())) {
                s = students[i];
                break;
            }
        }
        if (s == null) System.out.println("Student with such name info is no found!");
        return s;
    }

    public void updateStudent() {
        while (true) {
            System.out.print("Enter an id of a student you want to update: ");
            int studentId = scan.nextInt();
            scan.nextLine();
            while (studentId < 1) {
                System.out.print("id must greater or equal to 1! Try again: ");
                studentId = scan.nextInt();
                scan.nextLine();
            }
            Student studentToUpdate = null;
            for (int i = 0; i < studentCount; i++) {
                Student s = students[i];
                if (s.getId() == studentId) {
                    studentToUpdate = s;
                    break;
                }
            }
            if (studentToUpdate == null) {
                System.out.println("Student with " + studentId + " is no found! Try another one");
                continue;
            }
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
                        + "8 - phone number" + "\n");

                System.out.print("Choose option: ");
                int option = scan.nextInt();
                scan.nextLine();
                while (option < 0 || option > 8) {
                    System.out.print("Invalid option! Choose form 0 to 8: ");
                    option = scan.nextInt();
                    scan.nextLine();
                }
                if (option == 1) {
                    System.out.print("Enter new name for the student: ");
                    String name = scan.nextLine();
                    studentToUpdate.setFirstName(name);
                } else if (option == 2) {
                    System.out.print("Enter new last name for the student: ");
                    String lastName = scan.nextLine();
                    studentToUpdate.setLastName(lastName);
                } else if (option == 3) {
                    System.out.print("Enter new fathers name for the student: ");
                    String fatherName = scan.nextLine();
                    studentToUpdate.setLastName(fatherName);
                } else if (option == 4) {
                    System.out.print("Enter new course code for the student: ");
                    String courseCode = scan.nextLine();
                    studentToUpdate.setCourseCode(courseCode);
                } else if (option == 5) {
                    updateStudyForm(studentToUpdate);
                } else if (option == 6) {
                    updateStudentStatus(studentToUpdate);
                } else if (option == 7) {
                    System.out.println("Enter a new email address for the student: ");
                    String email = scan.nextLine();
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

