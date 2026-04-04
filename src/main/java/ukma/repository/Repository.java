package ukma.repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Repository<T, ID> {
    void store(T data);
    Optional<T> getById(ID id);
    List<T> getAll();
    void deleteById(ID id);
    List<T> search(Predicate<T> criteria);
}
