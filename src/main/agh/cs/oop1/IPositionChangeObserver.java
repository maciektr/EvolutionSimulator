package agh.cs.oop1;

public interface IPositionChangeObserver {
    void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element);
}