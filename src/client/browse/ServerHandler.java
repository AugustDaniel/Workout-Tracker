package client.browse;

import com.sun.corba.se.spi.activation.ServerOperations;
import data.Workout;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import util.ConnectionOptions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ServerHandler {

    private static final String IP_ADDR = "localhost";
    private static final int PORT = 8000;
    private static Socket socket;
    private static ObjectInputStream input;
    private static ObjectOutputStream output;

    private static void connect() throws IOException {
        try {
            socket = new Socket(IP_ADDR, PORT);
            input = new ObjectInputStream(socket.getInputStream());
            output = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            disconnect();
            throw e;
        }
    }

    private static void disconnect() {
        try {
            if (socket != null) socket.close();
            if (input != null) input.close();
            if (output != null) output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void uploadWorkout(Map.Entry<String, Workout> workout) throws IOException {
        try {
            output.writeObject(ConnectionOptions.SEND_WORKOUT);
            output.writeObject(workout);
            output.flush();
        } catch (IOException e) {
            connect();
            uploadWorkout(workout);
        }
    }

    public static Map<String, List<Workout>> getServerWorkouts() throws IOException, ClassNotFoundException {
        try {
            output.writeObject(ConnectionOptions.RETRIEVE_WORKOUTS);
            return (Map<String, List<Workout>>) input.readObject();
        } catch (IOException e) {
            connect();
            return getServerWorkouts();
        }
    }

    public static void showConnectionError() {
        new Alert(Alert.AlertType.ERROR, "Connection error", ButtonType.OK).showAndWait();
    }

    public static void showServerError() {
        new Alert(Alert.AlertType.ERROR, "Server error", ButtonType.OK).showAndWait();
    }
}
