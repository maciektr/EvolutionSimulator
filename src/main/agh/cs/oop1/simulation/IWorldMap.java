package agh.cs.oop1.simulation;

public interface IWorldMap {
    void place(Animal animal);
    Vector2d legalPositionAfterMove(Vector2d afterMove);
    boolean canMoveTo(Vector2d afterMove);

}
