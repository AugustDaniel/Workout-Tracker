package client.browse;

import data.Workout;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import util.ConnectionOptions;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class ServerHandler {

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8000;
    private static Socket socket = new Socket();
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    private ServerHandler() {
    }

    private static void connect() throws IOException {
        try {
            socket = new Socket();
            socket.connect(new InetSocketAddress(IP_ADDR, PORT), 1000);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            disconnect();
            throw e;
        }
    }

    private static void disconnect() {
        try {
            socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Object readObject() throws IOException, ClassNotFoundException {
        checkNullPointers();

        try {
            return input.readObject();
        } catch (IOException e) {
            connect();
            return input.readObject();
        }
    }

    private static void writeObject(Object o) throws IOException {
        checkNullPointers();

        try {
            output.writeObject(o);
            output.flush();
        } catch (IOException e) {
            connect();
            output.writeObject(o);
            output.flush();
        }
    }

    private static void checkNullPointers() throws IOException {
        if (socket == null || input == null || output == null) {
            connect();
        }
    }

    public static void uploadWorkout(Map.Entry<String, Workout> workout) throws IOException {
        writeObject(ConnectionOptions.SEND_WORKOUT);
        writeObject(workout);
    }

    public static Map<String, List<Workout>> getServerWorkouts() throws IOException, ClassNotFoundException {
        writeObject(ConnectionOptions.RETRIEVE_WORKOUTS);
        return (Map<String, List<Workout>>) readObject();
    }

    public static void showConnectionError() { //todo maybe srp
        new Alert(Alert.AlertType.ERROR, "Connection error", ButtonType.OK).showAndWait();
    }

    public static void showServerError() {
        new Alert(Alert.AlertType.ERROR, "Server error", ButtonType.OK).showAndWait();
    }

    public static void showUploadSuccessful() {
        new Alert(Alert.AlertType.INFORMATION, "Upload successful", ButtonType.OK).showAndWait();
    }
}