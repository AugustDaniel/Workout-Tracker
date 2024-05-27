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
//        workouts.add(new Workout("test 1"));
//        workouts.add(new Workout("test 2"));
//        System.out.println(workouts);

        while (!serverSocket.isClosed()) {
            try {
                System.out.println("Server: waiting for connection");
                Socket client = serverSocket.accept();
                System.out.println("Server: client connected");
                Connection connection = new Connection(client, new LinkedHashMap<String, List<Workout>>(workouts));
                connections.add(connection);
                service.execute(connection);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static synchronized void addWorkout(Map.Entry<String, Workout> entry) {
        if (workouts.computeIfAbsent(entry.getKey(), s -> new ArrayList<>())
                .stream()
                .noneMatch(s -> entry.getValue().equals(s))
        ) {
            workouts.get(entry.getKey()).add(entry.getValue());
            connections.forEach(c -> c.send(workouts));
        }
    }
}
