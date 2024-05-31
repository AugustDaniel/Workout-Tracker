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
        addWorkout(new AbstractMap.SimpleEntry<>("testuploader", new Workout("testworkout")));

        while (!serverSocket.isClosed()) {
            try {
                Connection connection = new Connection(serverSocket.accept());
                System.out.println("Connection accepted");
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
        }
    }

    public static Map<String, List<Workout>> getWorkouts() {
        return new LinkedHashMap<>(workouts);
    }
}
