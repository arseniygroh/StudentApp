package ukma.repository;

import ukma.domain.Person;
import ukma.domain.Student;

public class StudentRepo extends Repository<Student, Long> {

    public StudentRepo(String filePath) {
        super(filePath);
    }

    public StudentRepo() {
        super("files/students.ser");
    }

    @Override
    protected void updateIdCounter() {
        long maxId = 0;
        for (Long id : storage.keySet()) {
            if (id > maxId) maxId = id;
        }
        Person.setNextId(maxId);
    }

    @Override
    protected Long extractId(Student entity) {
        return entity.getId();
    }
}