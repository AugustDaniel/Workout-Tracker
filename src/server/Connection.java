package server;

import data.Workout;
import util.ObserverPattern;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Connection implements Runnable, ObserverPattern {

    private Socket client;
    private final WorkoutCatalogue workoutCatalogue;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(Socket client, WorkoutCatalogue workoutCatalogue) {
        this.client = client;
        this.workoutCatalogue = workoutCatalogue;
        this.workoutCatalogue.addObserver(this);
    }

    @Override
    public void run() {
        try {
            this.input = new ObjectInputStream(this.client.getInputStream());
            this.output = new ObjectOutputStream(this.client.getOutputStream());

            this.output.writeObject(this.workoutCatalogue.getWorkouts());
            this.output.flush();

            while (this.client.isConnected()) {
                this.workoutCatalogue.addWorkout((Workout) input.readObject());
            }
        } catch (Exception e) {
            //todo
        } finally {
            terminateConnection();
        }
    }

    @Override
    public void update() {
        try {
            sendWorkoutsToClient();
        } catch (Exception e) {
            //todo
        }
    }

    private synchronized void sendWorkoutsToClient() throws IOException {
        this.output.writeObject(this.workoutCatalogue.getWorkouts());
        this.output.flush();
    }

    private void terminateConnection() {
        try {
            this.workoutCatalogue.removeObserver(this);
            if (input != null) input.close();
            if (output != null) output.close();
            if (client != null && !client.isClosed()) client.close();
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
        }
    }
}
