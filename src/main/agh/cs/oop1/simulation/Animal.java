package agh.cs.oop1.simulation;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{
    private Vector2d position;
    private MapDirection direction;
    private IWorldMap map;
    private Genotype genotype;
    private int numberOfChildrenBorn;
    private int energy;

    private List<IPositionChangeObserver> observers = new ArrayList<IPositionChangeObserver>();

    public Animal(IWorldMap map, int energy, Vector2d position){
        this.numberOfChildrenBorn = 0;
        this.map = map;
        this.energy = energy;
        this.position = position;
        this.direction = MapDirection.getRandomDirection();
        this.genotype = new Genotype();
        map.placeAnimal(this);
    }
    public Animal(IWorldMap map, int energy, Vector2d position, MapDirection direction){
        this(map,energy, position);
        this.direction = direction;
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

    public void move(MapDirection direction){
        if(this.direction == direction) {
            Vector2d thisMove = this.direction.toUnitVector();
            Vector2d afterMove = this.position.add(thisMove);
            afterMove = map.legalPositionAfterMove(afterMove);

            this.positionChanged(this.position, afterMove);
            this.position = afterMove;
        }else
            this.direction = direction;
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
                && this.genotype.equals(that.genotype);
    }
}
