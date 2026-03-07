package ukma.model.repositories;

import ukma.model.Student;
import ukma.model.Teacher;

import java.util.*;
import java.util.function.Predicate;

public class TeacherRepo implements Repository<Teacher, Long>{
    private final Map<Long, Teacher> storage = new HashMap<>();

    @Override
    public void store(Teacher teacher) {
        if (teacher == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(teacher.getId(), teacher);
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
    }

    @Override
    public List<Teacher> search(Predicate<Teacher> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}
