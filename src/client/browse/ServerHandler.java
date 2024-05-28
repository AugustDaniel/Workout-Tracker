package client.browse;

import data.Workout;

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

    public static ServerHandler instance = new ServerHandler();

    private Socket socket;
    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Map<String, List<Workout>> workouts = null; //yabadabadoo
    private Future<?> currentTask;

    private ServerHandler() {
    }

    public void connect() {
        if (currentTask != null && !currentTask.isDone()) {
            return;
        }

        currentTask = pool.submit(() -> {
            while (true) {
                try {
                    socket = new Socket("localhost", 8000);
                    output = new ObjectOutputStream(socket.getOutputStream());
                    input = new ObjectInputStream(socket.getInputStream());
                    output.flush();

                    workouts = (Map<String, List<Workout>>) input.readObject();

                    while (socket.isConnected()) {
                        workouts = (Map<String, List<Workout>>) input.readObject();
                    }
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
        if (!currentTask.isDone()) currentTask.cancel(true);
        if (input != null) input.close();
        if (output != null) output.close();
        pool.shutdownNow();
    }

    public void uploadWorkout(Map.Entry<String, Workout> workout) throws IOException {
        if (socket == null || !socket.isConnected() || currentTask.isDone()) {
            return;
        }

        output.writeObject(workout);
        output.flush();
    }

    public Map<String, List<Workout>> getServerWorkouts() {
        return workouts;
    }
}
