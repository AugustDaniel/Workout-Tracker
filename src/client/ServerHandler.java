package client;

import data.Workout;
import javafx.collections.ObservableList;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {

    public static ServerHandler instance = new ServerHandler();

    private Socket socket;
    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private ObservableList<Workout> workouts;

    private ServerHandler() {
    }

    public void connect() throws IOException {
        if (socket != null && socket.isConnected()) {
            startListening();
            return;
        }

        socket = new Socket("localhost", 8000); //todo
        input = new ObjectInputStream(socket.getInputStream());
        output = new ObjectOutputStream(socket.getOutputStream());
        startListening();
    }

    private void startListening() {
        if (pool.isTerminated()) {
            pool.execute(() -> {
                try {
                    workouts = (ObservableList<Workout>) input.readObject();
                    while (socket.isConnected()) {
                        workouts.add((Workout) input.readObject());
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
        pool.shutdown();
    }

    public void uploadWorkout(Workout workout) throws IOException {
        if (socket == null || !socket.isConnected()) {
            return;
        }

        output.writeObject(workout);
        output.flush();
    }

    public ObservableList<Workout> getServerWorkouts() {
        return workouts;
    }
}
