package data;

public class Exercise {
    private int laps;
    private double raps;

    public Exercise(int laps, double raps){
        this.laps=laps;
        this.raps=raps;
    }

    public void setLaps(int laps) {
        this.laps = laps;
    }

    public void setRaps(double raps) {
        this.raps = raps;
    }

    public int getLaps() {
        return laps;
    }

    public double getRaps() {
        return raps;
    }
}
