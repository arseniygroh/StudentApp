package ukma.repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataStorage {
    private static final byte[] KEY = "xorEncryption".getBytes();
    public static <T> void saveData(Path filename, T data) {

        try {
            if (filename.getParent() != null) {
                Files.createDirectories(filename.getParent());
            }
        } catch (IOException e) {
            System.out.println("Could not create directories: " + e.getMessage());
            return;
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        byte[] dataRawBytes;

        try (ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(data);
            dataRawBytes = bos.toByteArray();
            for (int i = 0; i < dataRawBytes.length; i++) {
                dataRawBytes[i] = (byte) (dataRawBytes[i] ^ KEY[i % KEY.length]);
            }
            Files.write(filename, dataRawBytes);
        } catch (IOException e) {
            throw new RuntimeException("Error saving data to " + filename.getFileName() + ": " + e.getMessage());
        }
    }

    public static Object loadData(Path filename) {
        if (!Files.exists(filename)) return null;

        byte[] dataRawBytes = null;
        ByteArrayInputStream bai = null;
        try {
            dataRawBytes = Files.readAllBytes(filename);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        for (int i = 0; i < dataRawBytes.length; i++) {
            dataRawBytes[i] = (byte) (dataRawBytes[i] ^ KEY[i % KEY.length]);
        }

        bai = new ByteArrayInputStream(dataRawBytes);

        try (ObjectInputStream in = new ObjectInputStream(bai)) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data from " + filename.getFileName() + ": " + e.getMessage());
            return null;
        }
    }
}
