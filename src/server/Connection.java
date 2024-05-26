package server;

import data.Workout;
import util.ObserverPattern;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicReference;

public class Connection implements Runnable, ObserverPattern {

    private Socket client;
    private final AtomicReference<WorkoutCatalogue> workoutCatalogue;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(Socket client, AtomicReference<WorkoutCatalogue> workoutCatalogue) {
        this.client = client;
        this.workoutCatalogue = workoutCatalogue;
        this.workoutCatalogue.get().addObserver(this);
    }

    @Override
    public void run() {
        try {
            this.input = new ObjectInputStream(this.client.getInputStream());
            this.output = new ObjectOutputStream(this.client.getOutputStream());

            this.output.writeObject(this.workoutCatalogue.get().getWorkouts());
            this.output.flush();

            while (this.client.isConnected()) {
                this.workoutCatalogue.get().addWorkout((Workout) input.readObject());
            }
        } catch (Exception e) {
            //todo
        }
    }

    @Override
    public void update() {
        try {
            this.output.writeObject(this.workoutCatalogue.get().getWorkouts());
            this.output.flush();
        } catch (Exception e) {
            //todo
        }
    }
}
