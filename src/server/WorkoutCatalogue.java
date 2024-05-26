package server;

import data.Workout;
import util.ObserverPattern;
import util.SubjectPattern;

import java.io.Serializable;
import java.util.*;

public class WorkoutCatalogue implements SubjectPattern, Serializable {

    private final Set<Workout> workouts = new LinkedHashSet<>();
    private final List<ObserverPattern> observers = Collections.synchronizedList(new ArrayList<>());

    public void addWorkout(Workout workout) {
        this.workouts.add(workout);
        notifyObservers();
    }

    public Set<Workout> getWorkouts() {
        return this.workouts;
    }

    @Override
    public void addObserver(ObserverPattern observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ObserverPattern observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(ObserverPattern::update);
    }
}
