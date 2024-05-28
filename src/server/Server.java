package server;

import data.Workout;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;


public class Server {
    private static ServerSocket serverSocket;
    private final static List<Connection> connections = new ArrayList<>();
    private final static Map<String, List<Workout>> workouts = new LinkedHashMap<>();

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExecutorService service = Executors.newCachedThreadPool();

        while (!serverSocket.isClosed()) {
            try {
                System.out.println("Server: waiting for connection");
                Socket client = serverSocket.accept();
                System.out.println("Server: client connected");
                Connection connection = new Connection(client, new LinkedHashMap<>(workouts));
                connections.add(connection);
                service.execute(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void addWorkout(Map.Entry<String, Workout> entry) {
        List<Workout> workoutList = workouts.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());

        boolean workoutExists = workoutList.stream().anyMatch(workout -> workout.equals(entry.getValue()));
        if (!workoutExists) {
            workoutList.add(entry.getValue());
            connections.forEach(connection -> connection.send(workouts));
        }
    }
}
