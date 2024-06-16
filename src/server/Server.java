package server;

import data.Workout;
import util.IOHelper;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


public class Server {
    private static ServerSocket serverSocket;
    private volatile static Map<String, List<Workout>> workouts = new LinkedHashMap<>();
    private static final File workoutPath = new File("workouts_server");

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            workouts = (Map<String, List<Workout>>) IOHelper.readObject(workoutPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExecutorService service = Executors.newCachedThreadPool();

        while (true) {
            try {
                Connection connection = new Connection(serverSocket.accept());
                System.out.println("Connection accepted");
                service.execute(connection);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }

    public static synchronized void addWorkout(Map.Entry<String, Workout> entry) {
        List<Workout> workoutList = workouts.computeIfAbsent(entry.getKey(), k -> new ArrayList<>());

        boolean workoutExists = workoutList.stream().anyMatch(workout -> workout.equals(entry.getValue()));
        if (!workoutExists) {
            workoutList.add(entry.getValue());

            try {
                IOHelper.saveObject(workouts, workoutPath);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static Map<String, List<Workout>> getWorkouts() {
        Map<String, List<Workout>> toSend = new LinkedHashMap<>();

        workouts.forEach((k, v) -> {
            List<Workout> workoutCopy = new ArrayList<>(v);
            toSend.put(k, workoutCopy);
        });

        return toSend;
    }
}
