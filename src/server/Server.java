package server;


import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

public class Server {
    private static ServerSocket serverSocket;

    public static void main(String[] args) {
        try {
            serverSocket = new ServerSocket(8000);
        } catch (Exception e) {
            System.out.println(e); //todo
        }

        ExecutorService service = Executors.newCachedThreadPool();
        WorkoutCatalogue workoutCatalogue = new WorkoutCatalogue();

        while (true) {
            try {
                service.execute(new Connection(serverSocket.accept(), workoutCatalogue));
            } catch (Exception e) {
                System.out.println(e); //todo
            }
        }
    }
}
