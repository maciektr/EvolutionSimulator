package agh.cs.oop1.simulation;


import org.javatuples.Pair;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class MapCell {
    private Plant plant;
    private PriorityQueue<Animal> animals = new PriorityQueue<Animal>((a,b) -> b.getEnergy() - a.getEnergy());

    public MapCell(){
        this.plant = null;
    }

    public int numberOfAnimals(){
        return this.animals.size();
    }

    public Pair<Animal, Animal> getTwoMostEnergeticAnimals() throws IllegalAccessException {
        if(this.numberOfAnimals()<2)
            throw new IllegalAccessException("Cannot return two animals! There is not enough animals in this cell!");
        Pair<Animal, Animal> result = new Pair<Animal, Animal>(this.animals.poll(), this.animals.peek());
        this.animals.add(result.getValue0());
        return result;
    }

    public Animal getMostEnergeticAnimal() throws IllegalAccessException {
        if(this.numberOfAnimals() < 1)
            throw new IllegalAccessException("Cannot return an animal! There is no animals in this cell!");
        return this.animals.peek();
    }

    public void removeAnimal(Animal animal){
        this.animals.remove(animal);
    }

    public void addAnimal(Animal animal){
        this.animals.add(animal);
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

    public void setPlant(Plant plant){
        this.plant = plant;
    }

    public boolean isPlantSet(){
        return this.plant != null;
    }

    public ArrayList<Animal> getAnimals(){
        return new ArrayList<>(this.animals);
    }
}
