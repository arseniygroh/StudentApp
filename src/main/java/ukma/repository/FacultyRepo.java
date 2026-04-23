package ukma.repository;

import ukma.domain.Faculty;

public class FacultyRepo extends Repository<Faculty, Integer> {

    public FacultyRepo(String filePath) {
        super(filePath);
    }

    public FacultyRepo() {
        super("files/faculties.ser");
    }

    @Override
    protected void updateIdCounter() {
        int maxId = 0;
        for (Integer id : storage.keySet()) {
            if (id > maxId) maxId = id;
        }
        Faculty.setNextId(maxId);
    }

    @Override
    protected Integer extractId(Faculty entity) {
        return entity.getId();
    }
}