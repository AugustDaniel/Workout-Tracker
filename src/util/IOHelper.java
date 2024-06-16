package util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

public final class IOHelper {

    public static <T> void saveObject(T object, File file) throws Exception {
        ObjectOutputStream output = new ObjectOutputStream(Files.newOutputStream(file.toPath()));
        output.writeObject(object);
        output.close();
    }

    public static Object readObject(File file) throws Exception {
        ObjectInputStream input = new ObjectInputStream(Files.newInputStream(file.toPath()));
        Object object = input.readObject();
        input.close();
        return object;
    }
}
