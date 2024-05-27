package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Exercise implements Serializable {
    private String name;
    private List<ExerciseSet> sets;

    public Exercise(String name) {
        this.name = name;
        this.sets = new ArrayList<>();
    }

    public String getName() {
        return this.name;
    }

    public List<ExerciseSet> getSets() {
        return this.sets;
    }

    public void addSet(ExerciseSet set) {
        this.sets.add(set);
    }

    @Override
    public String toString() {
        return this.name;
    }
}
