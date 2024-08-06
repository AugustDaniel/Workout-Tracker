package integration;

import client.browse.ServerHandler;
import data.Workout;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import server.Server;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

public class ClientServerIntegrationTest {

    private static ExecutorService serverThread;

    @BeforeAll
    public static void setupServer() {
        serverThread = Executors.newSingleThreadExecutor();
        serverThread.execute(() -> {
            Server.main(null);
        });
    }

    @BeforeEach
    void setupClient() {
        Server.clearWorkouts();
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

    @Test
    void testUploadWorkoutToServerFromClient() {
        String workoutName = "workout";
        Workout workout = new Workout(workoutName);
        try {
            ServerHandler.uploadWorkout(new AbstractMap.SimpleEntry<>("user", workout));
        } catch (IOException e) {
            fail("upload error");
        }

        delay();
        assertEquals(1, Server.getWorkouts().size(), "Server has not received workout");
    }

    @Test
    void testReceiveWorkoutFromServer() {
        //todo
    }

    public static void delay() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
