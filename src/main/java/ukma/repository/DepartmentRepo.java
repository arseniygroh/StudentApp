package ukma.repository;

import ukma.domain.Department;

public class DepartmentRepo extends Repository<Department, Integer> {

    public DepartmentRepo(String filePath) {
        super(filePath);
    }

    public DepartmentRepo() {
        super("files/departments.ser");
    }

    @Override
    protected void updateIdCounter() {
        int maxId = 0;
        for (Integer id : storage.keySet()) {
            if (id > maxId) maxId = id;
        }
        Department.setNextId(maxId);
    }

    @Override
    protected Integer extractId(Department entity) {
        return entity.getId();
    }
}