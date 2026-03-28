package ukma.model.repositories;

import ukma.model.Department;

import ukma.model.utils.DataStorage;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class DepartmentRepo implements Repository<Department, Integer> {
    private Map<Integer, Department> storage = new HashMap<>();
    private final Path FILE_NAME = Path.of("files/departments.ser");

    public DepartmentRepo() {
        Object loadedData = DataStorage.loadData(FILE_NAME);
        if (loadedData != null) {
            storage = (Map<Integer, Department>) loadedData;
            int maxId = 0;
            for (int id : storage.keySet()) {
                if (id > maxId) maxId = id;
            }
            Department.setNextId(maxId);
        }
    }

    private void saveToFile() {
        DataStorage.saveData(FILE_NAME, storage);
    }

    @Override
    public void store(Department department) {
        if (department == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(department.getId(), department);
        saveToFile();
    }

    @Override
    public Optional<Department> getById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Department> getAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void deleteById(Integer id) {
        if (!storage.containsKey(id)) throw new IllegalArgumentException("Department with id " + id + " is not found");
        storage.remove(id);
        saveToFile();
    }

    @Override
    public List<Department> search(Predicate<Department> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}