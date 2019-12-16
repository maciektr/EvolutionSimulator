package agh.cs.oop1.simulation;

import java.util.PriorityQueue;

public class MapCell {
    Plant plant;
    PriorityQueue<Animal> animals = new PriorityQueue<Animal>();

    MapCell(){
        this.plant = null;
    }

    public void removeAnimal(Animal animal){
        this.animals.remove(animal);
    }

    public void addAnimal(Animal animal){
        this.animals.add(animal);
    }

    public void setPlant(Plant plant){
        this.plant = plant;
    }

    public Plant removePlant(){
        Plant p = this.plant;
        this.plant = null;
        return p;
    }
}
