package agh.cs.oop1.simulation;

public interface IStateChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element);
    void died(IMapElement element);
    void energyChanged(Animal animal);
}