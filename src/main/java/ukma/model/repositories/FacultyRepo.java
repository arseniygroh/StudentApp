package ukma.model.repositories;

import ukma.model.Faculty;

import java.util.*;
import java.util.function.Predicate;

public class FacultyRepo implements Repository<Faculty, Integer>{
    private final Map<Integer, Faculty> storage = new HashMap<>();

    @Override
    public void store(Faculty f) {
        if (f == null) throw new IllegalArgumentException("Faculty can't be a null value");
        storage.put(f.getId(), f);
    }

    @Override
    public Optional<Faculty> getById(Integer id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Faculty> getAll() {
        List<Faculty> faculties = new ArrayList<>();
        storage.values().forEach(f -> faculties.add(f));
        return faculties;
    }

    @Override
    public void deleteById(Integer id) {
        if (!storage.containsKey(id)) throw new IllegalArgumentException("Faculty with id " + id + " is not found(");
        storage.remove(id);
    }

    @Override
    public List<Faculty> search(Predicate<Faculty> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
