package agh.cs.oop1.simulation;

import java.util.PriorityQueue;

public class MapCell {
    private Plant plant;
    private PriorityQueue<Animal> animals = new PriorityQueue<Animal>();

    public MapCell(){
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

    public boolean anyAnimals(){
        return !this.animals.isEmpty();
    }

    public boolean hasAnimal(Animal animal){
        return this.animals.contains(animal);
    }

    public Plant removePlant(){
        Plant p = this.plant;
        this.plant = null;
        return p;
    }

}
