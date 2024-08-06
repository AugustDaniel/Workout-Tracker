package server;

import data.Workout;
import util.IOHelper;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;


public class Server {
    private static ServerSocket serverSocket;
    private static final File workoutPath = new File("workouts_server");
    private static final Map<String, List<Workout>> workouts;

    static {
        Map<String, List<Workout>> tempWorkouts = new ConcurrentHashMap<>();
        try {
            tempWorkouts = (Map<String, List<Workout>>) IOHelper.readObject(workoutPath);
        } catch (Exception e) {
            e.printStackTrace();
        }

        workouts = tempWorkouts;
    }

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
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

    public static void addWorkout(Map.Entry<String, Workout> entry) {
        workouts.merge(entry.getKey(),
                new CopyOnWriteArrayList<>(Collections.singletonList(entry.getValue())),
                (existingList, newListUnused) -> {
                    if (!existingList.contains(entry.getValue())) {
                        existingList.add(entry.getValue());

                        try {
                            IOHelper.saveObject(workouts, workoutPath);
                        } catch (Exception e) {
                            System.out.println("something went wrong");
                        }
                    }

                    return existingList;
                });
    }

    public static Map<String, List<Workout>> getWorkouts() {
        return new HashMap<>(workouts);
    }

    public static void clearWorkouts() {
        workouts.clear();
    }
}
