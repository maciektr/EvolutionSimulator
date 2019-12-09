package agh.cs.oop1;

public class Animal {
    int energy;
    Vector2d position;
    MapDirection direction;

    Animal(int energy, Vector2d position){
        this.energy = energy;
        this.position = position;
        this.direction = MapDirection.getRandomDirection();
    }

}
