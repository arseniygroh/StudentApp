package ukma.model.repositories;

import ukma.model.Department;

import java.util.*;
import java.util.function.Predicate;

public class DepartmentRepo implements Repository<Department, Integer> {
    private final Map<Integer, Department> storage = new HashMap<>();

    @Override
    public void store(Department department) {
        if (department == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(department.getId(), department);
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
    }

    @Override
    public List<Department> search(Predicate<Department> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}