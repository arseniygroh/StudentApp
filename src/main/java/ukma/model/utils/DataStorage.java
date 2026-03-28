package ukma.model.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataStorage {
    public static <T> void saveData(Path filename, T data) {

        if (!Files.exists(filename)) {
            try {
                Files.createFile(filename);
                System.out.println("File was successfully created");
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(filename))) {
            out.writeObject(data);
        } catch (IOException e) {
            System.out.println("Error saving data to " + filename.getFileName() + ": " + e.getMessage());
        }
    }

    public static Object loadData(Path filename) {
        if (!Files.exists(filename)) return null;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(filename))) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from " + filename.getFileName() + ": " + e.getMessage());
            return null;
        }
    }
}
