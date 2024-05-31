package server;


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
            this.input = new ObjectInputStream(this.client.getInputStream());
            this.output = new ObjectOutputStream(this.client.getOutputStream());
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
                switch (option) {
                    case SEND_WORKOUT:
                        Server.addWorkout((Map.Entry<String, Workout>) input.readObject());
                        break;
                    case RETRIEVE_WORKOUTS:
                        output.writeObject(Server.getWorkouts());
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
