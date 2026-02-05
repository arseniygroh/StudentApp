package ukma;

import ukma.model.Student;
import ukma.model.enums.StudentStatus;
import ukma.model.enums.StudyForm;
import ukma.model.utils.Menu;
import ukma.model.utils.RegistryManager;

public class Main {
    public static void main(String[] args) {
        RegistryManager manager = new RegistryManager();
        Menu menu = new Menu(manager);
        Student s1 = new Student("Арсеній", "Грох", "Миколайович", "01-10-2007", "sdsodos@.com", "232932934545", "some id", 2, "ipz", 2023, StudyForm.FULL_TIME, StudentStatus.CONTRACT);
        Student s2 = new Student("Віталій", "Дида", "Миколайович", "01-10-2007", "sdsodos@.com", "2329329334343", "some i1", 1, "ipz", 2025, StudyForm.FULL_TIME, StudentStatus.CONTRACT);
        Student s3 = new Student("Артем", "Слободян", "Миколайович", "01-10-2007", "sdsodos@.com", "232932932323", "some id2", 3, "ipz", 2022, StudyForm.FULL_TIME, StudentStatus.CONTRACT);
        manager.addStudent(s1);
        manager.addStudent(s2);
        manager.addStudent(s3);

        menu.initMenu();
    }
}
