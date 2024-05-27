package client.browse;

import data.Workout;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerHandler {

    public static ServerHandler instance = new ServerHandler();

    private Socket socket;
    private ExecutorService pool = Executors.newFixedThreadPool(1);
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private List<Workout> workouts;

    private ServerHandler() {
    }

    public void connect() throws Exception {
        if (socket != null && socket.isConnected()) {
            startListening();
            return;
        }

        socket = new Socket("localhost", 8000); //todo
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        output.flush();

        workouts = (List<Workout>) input.readObject();
        startListening();
    }

    private void startListening() {
        pool.execute(() -> {
            try {
                while (true) {
                    workouts.add((Workout) input.readObject());
                }
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public void disconnect() throws IOException {
        if (socket != null) socket.close();
        pool.shutdown();
    }

    public void uploadWorkout(Workout workout) throws IOException {
        if (socket == null || !socket.isConnected() || pool.isTerminated()) {
            return;
        }

        output.writeObject(workout);
        output.flush();
    }

    public List<Workout> getServerWorkouts() {
        return workouts;
    }
}
