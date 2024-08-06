package integration;

import client.Client;
import client.browse.ServerHandler;
import data.Workout;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import server.Server;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerIntegrationTest {

    private static ExecutorService serverThread;
    private static final String workoutName = "workout";
    private static final String uploaderName = "user";

    @BeforeAll
    public static void setupServer() {
        serverThread = Executors.newSingleThreadExecutor();
        serverThread.execute(() -> {
            Server.main(null);
        });
    }

    @AfterAll
    public static void tearDownServer() {
        serverThread.shutdownNow();

        try {
            serverThread.awaitTermination(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeEach
    void setup() {
        Server.clearWorkouts();
        Client.clearWorkouts();
    }

    @Test
    void testUploadWorkoutToServerFromClient() throws IOException {
        Workout workout = new Workout(workoutName);

        ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>(uploaderName, workout));
        delay();

        assertEquals(1, Server.getWorkouts().size(), "Server has not received workout");
        assertNotNull(Server.getWorkouts().get(uploaderName));
        assertEquals(Server.getWorkouts().get(uploaderName).get(0).getName(), workoutName);
    }

    @Test
    void testReceiveWorkoutFromServer() throws IOException, ClassNotFoundException {
        Workout workout = new Workout(workoutName);
        Server.addWorkout(new AbstractMap.SimpleEntry<>(uploaderName, workout));

        Map<String, List<Workout>> workoutsReceivedFromServer = ServerHandler.getServerWorkouts();
        assertEquals(1, workoutsReceivedFromServer.size());
        assertNotNull(workoutsReceivedFromServer.get(uploaderName));
        assertEquals(workoutName, workoutsReceivedFromServer.get(uploaderName).get(0).getName());
    }

    @Test
    void testReceiveMultipleWorkoutsFromServer() throws IOException, ClassNotFoundException {
        int amountOfWorkouts = 10;
        int amountOfBatches = 3;
        int workoutCounter = 0;
        for (int i = 0; i < amountOfBatches; i++) {
            for (int j = 0; j < amountOfWorkouts; j++) {
                Workout workout = new Workout(workoutName + workoutCounter);
                Client.addWorkout(workout);
                ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>(uploaderName, workout));
                workoutCounter++;
            }
        }

        Map<String, List<Workout>> workoutsReceivedFromServer = ServerHandler.getServerWorkouts();
        assertNotNull(workoutsReceivedFromServer.get(uploaderName));
        assertEquals(amountOfWorkouts * amountOfBatches, workoutsReceivedFromServer.get(uploaderName).size());
    }

    private static void delay() {
        try {
            Thread.sleep(250);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
