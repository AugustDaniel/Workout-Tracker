package server;


import client.browse.ServerHandler;
import data.Workout;
import util.ConnectionOptions;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.Map;

public class Connection implements Runnable {

    private Socket client;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public Connection(Socket client) {
        this.client = client;

        try {
            this.output = new ObjectOutputStream(this.client.getOutputStream());
            this.input = new ObjectInputStream(this.client.getInputStream());
            output.flush();
        } catch (Exception e) {
            System.out.println(e);
            terminateConnection();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                ConnectionOptions option = (ConnectionOptions) input.readObject();
                System.out.println("Retrieved " + option.toString());
                switch (option) {
                    case SEND_WORKOUT:
                        receiveWorkoutFromClient();
                        break;
                    case RETRIEVE_WORKOUTS:
                        sendWorkoutsToClient();
                        break;
                }
            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
                terminateConnection();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void receiveWorkoutFromClient() throws IOException, ClassNotFoundException {
        Map.Entry<String, Workout> entry = (Map.Entry<String, Workout>) input.readObject();

        if (entry.getKey() == null || entry.getValue() == null) {
            return;
        }

        Server.addWorkout(entry);
        System.out.println("Workout added");
    }

    private void sendWorkoutsToClient() throws IOException {
        output.writeObject(Server.getWorkouts());
        output.flush();
        System.out.println("Workouts sent");
    }


    private void terminateConnection() {
        try {
            if (client != null) client.close();
            client = null;
            input = null;
            output = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
