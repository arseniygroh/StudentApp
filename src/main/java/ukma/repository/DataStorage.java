package ukma.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class DataStorage {
    private static final Logger logger = LoggerFactory.getLogger(DataStorage.class);
    private static final byte[] KEY = "xorEncryption".getBytes();

    public static <T> void saveData(Path filename, T data) {
        try {
            if (filename.getParent() != null) {
                Files.createDirectories(filename.getParent());
            }
        } catch (IOException e) {
            logger.error("Could not create directories for path: {}", filename, e);
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
            logger.debug("Data successfully saved to {}", filename.getFileName()); // debug - щоб не спамити в лог при кожному збереженні
        } catch (IOException e) {
            logger.error("Error saving data to {}", filename.getFileName(), e);
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
            logger.error("Failed to read bytes from file {}", filename, e);
            throw new RuntimeException(e.getMessage());
        }
        for (int i = 0; i < dataRawBytes.length; i++) {
            dataRawBytes[i] = (byte) (dataRawBytes[i] ^ KEY[i % KEY.length]);
        }

        bai = new ByteArrayInputStream(dataRawBytes);

        try (ObjectInputStream in = new ObjectInputStream(bai)) {
            return in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Error loading or decrypting data from {}", filename.getFileName(), e);
            return null;
        }
    }
}