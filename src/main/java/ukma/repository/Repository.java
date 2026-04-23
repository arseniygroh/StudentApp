package ukma.repository;

import java.nio.file.Path;
import java.util.*;
import java.util.function.Predicate;

public abstract class Repository<T, ID> {
    protected Map<ID, T> storage = new HashMap<>();
    protected final Path FILE_NAME;

    public Repository(String filePath) {
        this.FILE_NAME = Path.of(filePath);
        loadData();
    }

    @SuppressWarnings("unchecked")
    protected void loadData() {
        Object loadedData = DataStorage.loadData(FILE_NAME);
        if (loadedData != null) {
            storage = (Map<ID, T>) loadedData;
            updateIdCounter();
        }
    }

    protected abstract void updateIdCounter();
    protected abstract ID extractId(T entity);

    protected void saveToFile() {
        DataStorage.saveData(FILE_NAME, storage);
    }

    public void store(T data) {
        if (data == null) throw new IllegalArgumentException("Can't store null value");
        storage.put(extractId(data), data);
        saveToFile();
    }

    public Optional<T> getById(ID id) {
        return Optional.ofNullable(storage.get(id));
    }

    public List<T> getAll() {
        return new ArrayList<>(storage.values());
    }

    public void deleteById(ID id) {
        if (!storage.containsKey(id)) throw new IllegalArgumentException("Entity with id " + id + " is not found");
        storage.remove(id);
        saveToFile();
    }

    public List<T> search(Predicate<T> predicate) {
        return storage.values().stream()
                .filter(predicate)
                .toList();
    }
}