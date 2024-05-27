package server;


import data.Workout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class Connection implements Runnable {

    private Socket client;
    private List<Workout> workouts;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(Socket client, List<Workout> workouts) {
        this.client = client;
        this.workouts = workouts;

        try {
            this.input = new ObjectInputStream(this.client.getInputStream());
            this.output = new ObjectOutputStream(this.client.getOutputStream());
            output.flush();
        } catch (Exception e) {
            terminateConnection();
        }
    }

    @Override
    public void run() {
        try {
            this.output.writeObject(this.workouts);
            this.output.flush();
            System.out.println("Server: written");
            while (this.client.isConnected()) {
                System.out.println("Server: listening");
                Workout workout = (Workout) this.input.readObject();
                Server.addWorkout(workout);
            }
        } catch (Exception e) {
            terminateConnection();
        }
    }

    public void send(Workout workout) {
        try {
            this.workouts.add(workout);
            this.output.writeObject(workout);
            this.output.flush();
        } catch (Exception e) {
            System.out.println(e);
            terminateConnection();
        }
    }

    private void terminateConnection() {
        try {
            if (input != null) input.close();
            if (output != null) output.close();
            if (client != null && !client.isClosed()) client.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
