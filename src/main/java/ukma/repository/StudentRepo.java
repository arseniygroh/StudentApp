package ukma.repository;

import ukma.domain.Person;
import ukma.domain.Student;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public class StudentRepo implements Repository<Student, Long>{
    private final Path FILE_NAME;
    private Map<Long, Student> storage = new HashMap<>();

    public StudentRepo(String filePath) {
        this.FILE_NAME = Path.of(filePath);
        Object loadedData = DataStorage.loadData(FILE_NAME);
        if (loadedData != null) {
            storage = (Map<Long, Student>) loadedData;
            long maxId = 0;
            for (Long id : storage.keySet()) {
                if (id > maxId) maxId = id;
            }
            Person.setNextId(maxId);
        }
    }

    public StudentRepo() {
        this("files/students.ser");
    }

    private void saveToFile() {
        DataStorage.saveData(FILE_NAME, storage);
    }

    @Override
    public void store(Student student) {
        if (student == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(student.getId(), student);
        saveToFile();
    }

    @Override
    public Optional<Student> getById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Student> getAll() {
        List<Student> students = new ArrayList<>();
        storage.forEach((id, s) -> students.add(s));
        return students;
    }

    @Override
    public void deleteById(Long id) {
        if (!storage.containsKey(id)) throw new IllegalArgumentException("Student with id " + id + " is not found(");
        storage.remove(id);
        saveToFile();
    }

    @Override
    public List<Student> search(Predicate<Student> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
