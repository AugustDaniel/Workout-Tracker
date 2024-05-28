package client.browse;

import data.Workout;

public class WorkoutTableRow {

    public Workout workout;
    private String uploader;
    private String name;

    public WorkoutTableRow(Workout workout, String uploader) {
        this.uploader = uploader;
        this.name = workout.getName();
        this.workout = workout;
    }

    public String getUploader() {
        return uploader;
    }

    public String getName() {
        return name;
    }

    public Workout getWorkout() {
        return workout;
    }
}
