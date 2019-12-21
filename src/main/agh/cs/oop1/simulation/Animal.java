package agh.cs.oop1.simulation;

import java.util.ArrayList;
import java.util.List;

public class Animal implements IMapElement{
    private static int energyPerMove=0;
    private Vector2d position;
    private MapDirection direction;
    private IWorldMap map;
    private Genotype genotype;
    private int numberOfChildrenBorn;
    private int energy;

    private List<IStateChangeObserver> observers = new ArrayList<IStateChangeObserver>();

    public Animal(IWorldMap map, int energy, Vector2d position){
        this.numberOfChildrenBorn = 0;
        this.map = map;
        this.energy = energy;
        position = this.map.legalPositionAfterMove(position);
        this.position = position;
        this.direction = MapDirection.getRandomDirection();
        this.genotype = new Genotype();
        map.placeAnimal(this);
    }
    public Animal(IWorldMap map, int energy, Vector2d position, MapDirection direction){
        this(map,energy, position);
        this.direction = direction;
    }

    public static void setEnergyPerMove(int energyPerMove){
        Animal.energyPerMove = energyPerMove;
    }

    public int getEnergy(){
        return this.energy;
    }

    public void addEnergy(int energy){
        if(energy < 0)
            throw new IllegalArgumentException("Animal cannot lose energy from eating!");
        this.energy+=energy;
        this.energyChanged();
    }

    public int reproduceEnergy(){
        this.numberOfChildrenBorn++;
        int e = this.energy/4;
        this.energy -= e;
        this.energyChanged();
        return e;
    }

    public void addObserver(IStateChangeObserver observer){
        observers.add(observer);
    }
    void removeObserver(IStateChangeObserver observer){
        observers.remove(observer);
    }

    private void positionChanged(Vector2d oldPosition, Vector2d newPosition){
        for(IStateChangeObserver observer : observers){
            observer.positionChanged(oldPosition,newPosition, this);
        }
    }

//    private void died(){
//        for(IStateChangeObserver observer : this.observers){
//            observer.died(this);
//        }
//    }

    public void energyChanged(){
        for(IStateChangeObserver observer : this.observers){
            observer.energyChanged(this);
        }
    }

    public void move(MapDirection direction){
        this.energy -= Animal.energyPerMove;
//        if(this.getEnergy() <= 0) {
//            this.died();
//            return;
//        }

        if(this.direction == direction) {
            Vector2d thisMove = this.direction.toUnitVector();
            Vector2d oldPosition = this.position;
            this.position = map.legalPositionAfterMove(this.position.add(thisMove));

            this.positionChanged(oldPosition, this.position);
        }else
            this.direction = direction;
        this.energyChanged();
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
