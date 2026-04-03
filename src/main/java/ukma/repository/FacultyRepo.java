package ukma.repository;


import ukma.domain.Faculty;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class FacultyRepo implements Repository<Faculty, Integer>{
    private Map<Integer, Faculty> storage = new HashMap<>();
    private final Path FILE_NAME;

    public FacultyRepo(String filepath) {
        this.FILE_NAME = Path.of(filepath);
        Object loadedData = DataStorage.loadData(FILE_NAME);
        if (loadedData != null) {
            storage = (Map<Integer, Faculty>) loadedData;
            int maxId = 0;
            for (int id : storage.keySet()) {
                if (id > maxId) maxId = id;
            }
            Faculty.setNextId(maxId);
        }
    }

    public FacultyRepo() {
        this("files/faculties.ser");
    }

    private void saveToFile() {
        DataStorage.saveData(FILE_NAME, storage);
    }

    @Override
    public void store(Faculty f) {
        if (f == null) throw new IllegalArgumentException("Faculty can't be a null value");
        storage.put(f.getId(), f);
        saveToFile();
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
        saveToFile();
    }

    @Override
    public List<Faculty> search(Predicate<Faculty> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
