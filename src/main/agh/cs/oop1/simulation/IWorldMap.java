package agh.cs.oop1.simulation;

public interface IWorldMap {
    void placeAnimal(Animal animal);
    Vector2d legalPositionAfterMove(Vector2d afterMove);
    boolean equals(Object other);
}
