package ukma;

import ukma.model.Faculty;
import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.utils.ConsoleInput;
import ukma.model.utils.Menu;
import ukma.model.utils.RegistryManager;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        RegistryManager manager = new RegistryManager();
        ConsoleInput inputValidator = new ConsoleInput();
        Menu menu = new Menu(manager, inputValidator);
        Faculty fi = new Faculty("Факультет інформатики", "ФІ", null, "fi@ukma.edu.ua", "+380440000000");
        Faculty fen = new Faculty("Факультет економічних наук", "ФЕН", null, "fen@ukma.edu.ua", "+380442222222");
        manager.addFaculty(fi);
        manager.addFaculty(fen);
        Student s1 = new Student("Арсеній", "Грох", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@.com", "232932934545", "some id", 2, "ipz", 2023, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        Student s2 = new Student("Віталій", "Дида", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@.com", "2329329334343", "some i1", 1, "ipz", 2025, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        Student s3 = new Student("Артем", "Слободян", "Миколайович", LocalDate.of(2007, 01, 10), "sdsodos@.com", "232932932323", "some id2", 3, "ipz", 2022, StudyForm.FULL_TIME, StudentStatus.CONTRACT, fi);
        manager.addStudent(s1);
        manager.addStudent(s2);
        manager.addStudent(s3);

        menu.initMenu();
    }
}
