package ukma.repository;

import ukma.domain.Person;
import ukma.domain.Teacher;

public class TeacherRepo extends Repository<Teacher, Long> {

    public TeacherRepo(String filePath) {
        super(filePath);
    }

    public TeacherRepo() {
        super("files/teachers.ser");
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
    protected Long extractId(Teacher entity) {
        return entity.getId();
    }
}