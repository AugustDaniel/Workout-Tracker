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
                        Server.addWorkout((Map.Entry<String, Workout>) input.readObject());
                        System.out.println("Workout added");
                        break;
                    case RETRIEVE_WORKOUTS:
                        output.writeObject(Server.getWorkouts());
                        output.flush();
                        System.out.println("Workouts sent");
                        break;
                }
            } catch (IOException e) {
                e.printStackTrace();
                terminateConnection();
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
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
