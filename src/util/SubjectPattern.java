package util;

public interface SubjectPattern {

    void addObserver(ObserverPattern observer);

    void removeObserver(ObserverPattern observer);

    void notifyObservers();
}
