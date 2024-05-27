package server;


import data.Workout;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Connection implements Runnable {

    private Socket client;
    private Map<String, List<Workout>> workouts;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(Socket client, Map<String, List<Workout>> workouts) {
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
                Map.Entry<String, Workout> workout = (Map.Entry<String, Workout>) this.input.readObject();
                Server.addWorkout(workout);
            }
        } catch (Exception e) {
            terminateConnection();
        }
    }

    public void send(Map<String, List<Workout>> map) {
        try {
            this.workouts = map;
            //todo maybe only do this when asked for by the client to improve performance
            this.output.writeObject(map);
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
