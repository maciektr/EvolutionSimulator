package agh.cs.oop1.simulation;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{
    int energy;
    Vector2d position;
    MapDirection direction;
    IWorldMap map;
    Genotype genotype;


    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();

    Animal(IWorldMap map, int energy, Vector2d position){
        this.map = map;
        this.energy = energy;
        this.position = position;
        this.direction = MapDirection.getRandomDirection();
        this.genotype = Genotype.getRandomGenotype();
        map.place(this);
    }

    public void addObserver(IPositionChangeObserver observer){
        observers.add(observer);
    }
    void removeObserver(IPositionChangeObserver observer){
        observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IPositionChangeObserver observer : observers){
            observer.positionChanged(oldPosition,newPosition, this);
        }
    }

    public void move(MoveDirection direction){
        switch(direction){
            case RIGHT:
                this.direction = this.direction.next();
                break;
            case LEFT:
                this.direction = this.direction.previous();
                break;
            case FORWARD:
            case BACKWARD:
                Vector2d thisMove = this.direction.toUnitVector();
                if(direction == MoveDirection.BACKWARD)
                    thisMove = thisMove.opposite();
                Vector2d afterMove = this.position.add(thisMove);
                afterMove = map.legalPositionAfterMove(afterMove);

                this.positionChanged(this.position, afterMove);
                this.position = afterMove;
                break;
        }
    }

    @Override
    public String toString(){
        return "Animal: "+this.position.toString()+" "+this.direction.toString();
    }

    public Vector2d getPosition(){
        return this.position;
    }


    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Animal))
            return false;
        Animal that = (Animal) other;
        return this.direction == that.direction
                && this.position == that.position
                && this.energy == that.energy
                && this.map.equals(that.map)
                && this.genotype.equals(that.genotype);
    }
}
