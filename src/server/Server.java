package server;

import data.Workout;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    private static ServerSocket serverSocket;
    private final static List<Connection> connections = new ArrayList<>();
    private final static Queue<Workout> workouts = new ConcurrentLinkedQueue<>(); //https://stackoverflow.com/questions/6916385/is-there-a-concurrent-list-in-javas-jdk

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ExecutorService service = Executors.newCachedThreadPool();
        workouts.add(new Workout("test 1"));
        workouts.add(new Workout("test 2"));
        System.out.println(workouts);

        while (!serverSocket.isClosed()) {
            try {
                System.out.println("Server: waiting for connection");
                Socket client = serverSocket.accept();
                System.out.println("Server: client connected");
                Connection connection = new Connection(client, new ArrayList<>(workouts));
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
