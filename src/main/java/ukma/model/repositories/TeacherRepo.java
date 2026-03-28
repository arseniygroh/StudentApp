package ukma.model.repositories;

import ukma.model.Person;

import ukma.model.Teacher;
import ukma.model.utils.DataStorage;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class TeacherRepo implements Repository<Teacher, Long>{
    private Map<Long, Teacher> storage = new HashMap<>();
    private final Path FILE_NAME = Path.of("files/teachers.ser");


    public TeacherRepo() {
        Object loadedData = DataStorage.loadData(FILE_NAME);
        if (loadedData != null) {
            storage = (Map<Long, Teacher>) loadedData;
            long maxId = 0;
            for (Long id : storage.keySet()) {
                if (id > maxId) maxId = id;
            }
            Person.setNextId(maxId);
        }
    }

    private void saveToFile() {
        DataStorage.saveData(FILE_NAME, storage);
    }

    @Override
    public void store(Teacher teacher) {
        if (teacher == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(teacher.getId(), teacher);
        saveToFile();
    }

    @Override
    public Optional<Teacher> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Teacher> getAll() {
        List<Teacher> teachers = new ArrayList<>();
        storage.forEach((id, t) -> teachers.add(t));
        return teachers;
    }

    @Override
    public void deleteById(Long id) {
        if (!storage.containsKey(id)) throw new IllegalArgumentException("Teacher with id " + id + " is not found(");
        storage.remove(id);
        saveToFile();
    }

    @Override
    public List<Teacher> search(Predicate<Teacher> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
