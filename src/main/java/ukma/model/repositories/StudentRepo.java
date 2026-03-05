package ukma.model.repositories;

import ukma.model.Student;

import java.util.*;
import java.util.function.Predicate;

public class StudentRepo implements Repository<Student, Long>{
    private final Map<Long, Student> storage = new HashMap<>();

    @Override
    public void store(Student student) {
        if (student == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(student.getId(), student);
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
    }

    @Override
    public List<Student> search(Predicate<Student> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
