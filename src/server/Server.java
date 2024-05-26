package server;

import data.Workout;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static ServerSocket serverSocket;
    private final static List<Connection> connections = new ArrayList<>();
    private final static Set<Workout> workouts = new LinkedHashSet<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExecutorService service = Executors.newCachedThreadPool();

        while (!serverSocket.isClosed()) {
            try {
                Connection connection = new Connection(serverSocket.accept(), new LinkedHashSet<>(workouts));
                connections.add(connection);
                service.execute(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void addWorkout(Workout workout) {
        workouts.add(workout);
        connections.forEach(c -> c.send(workout));
    }
}
