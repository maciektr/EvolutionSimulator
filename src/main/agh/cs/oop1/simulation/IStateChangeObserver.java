package agh.cs.oop1.simulation;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element);
    void died(IMapElement element);
}