package agh.cs.oop1.simulation;

import java.util.List;

public class Statistics {
    private int currentNumberOfChildren = 0;
    private int currentNumberOfPlants = 0;
    private int currentAverageEnergy = 0;
    private int currentNumberOfLivingAnimals = 0;

    private int deadAnimalsCount = 0;
    private int animalDeathEpochsSum = 0;

    private Genotype dominantGenotype;
    private int epoch = 0;
    private transient GenotypePopularityTracker tracker = new GenotypePopularityTracker();

    private int historicalAverageNumberOfChildren = 0;
    private transient int historicalSumOfChildren = 0;
    private int historicalAverageSumOfPlants = 0;
    private transient int historicalSumOfPlants = 0;
    private int historicalAverageEnergy = 0;
    private transient int historicalEnergySum = 0;
    private int historicalAverageNumberOfLivingAnimals = 0;
    private transient int historicalNumberOfLivingAnimalsSum = 0;

    public Statistics() {
    }

    public GenotypePopularityTracker getTracker() {
        return this.tracker;
    }

    public int getEpoch() {
        return this.epoch;
    }

    public void spotEpoch() {
        this.epoch++;
        System.out.println(this.historicalSumOfChildren);
        this.historicalAverageNumberOfChildren = this.historicalSumOfChildren / this.epoch;
        this.historicalAverageSumOfPlants = this.historicalSumOfPlants / this.epoch;
        this.historicalAverageEnergy = this.historicalEnergySum / this.epoch;
        this.historicalAverageNumberOfLivingAnimals = this.historicalNumberOfLivingAnimalsSum / this.epoch;
    }

    public void setAverageEnergy(int numberOfAnimals, int energySum) {
        this.historicalEnergySum += this.currentAverageEnergy;
        this.currentNumberOfLivingAnimals = numberOfAnimals;
        this.currentAverageEnergy = this.getCurrentNumberOfLivingAnimals() == 0 ? 0 : energySum / this.getCurrentNumberOfLivingAnimals();
    }

    public void setCurrentNumberOfChildren(int currentNumberOfChildren) {
        this.historicalSumOfChildren += (this.getAverageNumberOfChildren());
        this.currentNumberOfChildren = currentNumberOfChildren;
    }

    public void spotNewPlant() {
        this.currentNumberOfPlants++;
    }

    public void spotEatenPlant() {
        this.currentNumberOfPlants--;
    }

    public void spotDeadAnimal(int epoch) {
        this.deadAnimalsCount++;
        this.animalDeathEpochsSum += epoch;
    }

    public void update(List<Animal> animals) {
        int energySum = 0;
        this.tracker = new GenotypePopularityTracker();

        int numberOfChildren = 0;
        for (Animal a : animals) {
            tracker.spotGenotype(a.getGenotype());
            energySum += a.getEnergy();
            numberOfChildren += a.getNumberOfChildren();
        }
        this.dominantGenotype = tracker.getTheMostPopular();
        this.setAverageEnergy(animals.size(), energySum);
        this.setCurrentNumberOfChildren(numberOfChildren);
    }

    public int getAverageDeadAnimalEpoch() {
        return this.deadAnimalsCount != 0 ? this.animalDeathEpochsSum / this.deadAnimalsCount : 0;
    }

    public int getAverageNumberOfChildren() {
        return this.getCurrentNumberOfLivingAnimals() != 0 ? this.currentNumberOfChildren / this.getCurrentNumberOfLivingAnimals() : 0;
    }

    public int getCurrentNumberOfLivingAnimals() {
        return this.currentNumberOfLivingAnimals;
    }

    public Genotype getDominantGenotype() {
        return this.dominantGenotype;
    }


    public int getCurrentNumberOfPlants() {
        return this.currentNumberOfPlants;
    }


    public int getCurrentAverageEnergy() {
        return this.currentAverageEnergy;
    }
}
