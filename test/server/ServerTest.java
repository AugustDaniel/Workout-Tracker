package server;

import data.Workout;
import org.junit.Before;
import org.junit.Test;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class ServerTest {

    @Before
    public void setUp() {
        Server.clearWorkouts();
    }

    @Test
    public void testDuplicateEntries() {
        Workout workout = new Workout("Workout");
        Map.Entry<String, Workout> entry = new AbstractMap.SimpleEntry<>("User", workout);

        int amountOfDuplicates = 7;
        for (int i = 0; i < amountOfDuplicates; i++) {
            Server.addWorkout(entry);
        }

        assertEquals(Server.getWorkouts().size(), 1);
        assertNotEquals(Server.getWorkouts().size(), amountOfDuplicates);
    }

    @Test
    public void testConcurrentAddWorkout() throws InterruptedException {
        int threadCount = 10;
        CountDownLatch latch = new CountDownLatch(threadCount);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                try {
                    latch.countDown();
                    latch.await();

                    Workout workout = new Workout("Workout " + index);
                    Server.addWorkout(new AbstractMap.SimpleEntry<>("User" + index, workout));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
        }

        executorService.shutdown();
        assertTrue("Thread shutdown has timed out", executorService.awaitTermination(5, TimeUnit.SECONDS));

        Map<String, List<Workout>> workouts = Server.getWorkouts();
        assertEquals(threadCount, workouts.size());
    }

    @Test
    public void testConcurrentGetWorkouts() throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            Workout workout = new Workout("Workout " + i);
            Server.addWorkout(new AbstractMap.SimpleEntry<>("User", workout));
        }

        int threadCount = 10;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                Map<String, List<Workout>> workouts = Server.getWorkouts();
                assertEquals(1, workouts.size());
                assertEquals(5, workouts.get("User").size());
            });
        }

        executorService.shutdown();
        assertTrue("Thread shutdown has timed out", executorService.awaitTermination(5, TimeUnit.SECONDS));
    }

    @Test
    public void testConcurrentAddAndGetWorkouts() throws InterruptedException {
        int threadCount = 10;
        String name = "User";
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount * 2);

        for (int i = 0; i < threadCount; i++) {
            final int index = i;
            executorService.submit(() -> {
                Workout workout = new Workout("Workout " + index);
                Server.addWorkout(new AbstractMap.SimpleEntry<>(name, workout));
            });

            executorService.submit(() -> {
                Map<String, List<Workout>> workouts = Server.getWorkouts();
                assertNotNull(workouts.get(name));
            });
        }

        executorService.shutdown();
        assertTrue("Thread shutdown has timed out", executorService.awaitTermination(5, TimeUnit.SECONDS));

        Map<String, List<Workout>> finalWorkouts = Server.getWorkouts();
        assertTrue(finalWorkouts.get(name).size() <= threadCount);
    }
}