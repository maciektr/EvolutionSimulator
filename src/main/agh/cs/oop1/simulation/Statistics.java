package agh.cs.oop1.simulation;

import java.util.List;

public class Statistics {
    private int deadAnimalsCount = 0;
    private int deadAnimalsEpochsSum = 0;
    private int numberOfChildren = 0;
    private  int numberOfPlants = 0;
    private int averageEnergy = 0;
    private int numberOfAnimals = 0;
    private Genotype theMostPopularGenotype;

    public Statistics(Genotype theMostPopularGenotype){
        this.theMostPopularGenotype = theMostPopularGenotype;
    }

    public int getAverageDeadAnimalEpoch(){
        return this.deadAnimalsCount != 0 ? this.deadAnimalsEpochsSum / this.deadAnimalsCount : 0;
    }

    public int getAverageNumberOfChildren(){
        return this.getNumberOfAnimals() != 0 ? this.numberOfChildren / this.getNumberOfAnimals() : 0;
    }

    public int getNumberOfAnimals(){
        return this.numberOfAnimals;
    }

    public void setTheMostPopularGenotype(GenotypePopularityTracker tracker){
        this.theMostPopularGenotype = tracker.getTheMostPopular();
    }

    public void setAverageEnergy(int numberOfAnimals, int energySum){
        this.numberOfAnimals = numberOfAnimals;
        this.averageEnergy = this.getNumberOfAnimals() == 0 ? 0 : energySum/this.getNumberOfAnimals();
    }

    public void setNumberOfChildren(int numberOfChildren){
        this.numberOfChildren = numberOfChildren;
    }

    public Genotype getTheMostPopularGenotype(){
        return this.theMostPopularGenotype;
    }

    public void spotNewPlant(){
        this.numberOfPlants++;
    }

    public void spotEatenPlant(){
        this.numberOfPlants--;
    }

    public int getNumberOfPlants(){
        return this.numberOfPlants;
    }

    public void spotDeadAnimal(int epoch){
        this.deadAnimalsCount++;
        this.deadAnimalsEpochsSum+=epoch;
    }

    public int getAverageEnergy(){
        return this.averageEnergy;
    }

    public void update(List<Animal> animals){
        int energySum = 0;
        GenotypePopularityTracker tracker = new GenotypePopularityTracker();
        int numberOfChildren = 0;
        for(Animal a : animals) {
            tracker.spotGenotype(a.getGenotype());
            energySum += a.getEnergy();
            numberOfChildren += a.getNumberOfChildren();
        }
        this.setTheMostPopularGenotype(tracker);
        this.setAverageEnergy(animals.size(), energySum);
        this.setNumberOfChildren(numberOfChildren);

    }
}
