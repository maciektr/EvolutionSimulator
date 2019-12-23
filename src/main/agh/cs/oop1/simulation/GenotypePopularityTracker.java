package agh.cs.oop1.simulation;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GenotypePopularityTracker{
    private Map<Genotype, Integer> tracker = new HashMap<>();
    private Genotype theMostPopular = null;
    private int theMostPopularCount = 0;


    public void spotGenotype(Genotype genotype){
        this.tracker.putIfAbsent(genotype, 0);
        int count = this.tracker.get(genotype)+1;
        this.tracker.put(genotype, count);
        if(count >= this.theMostPopularCount){
            this.theMostPopularCount = count;
            this.theMostPopular = genotype;
        }
    }

    public Genotype getTheMostPopular(){
        return theMostPopular;
    }

    private ArrayList<Animal> animals = new ArrayList<>();
    public void storeAnimal(Animal animal){
        if(animal.getGenotype().equals(this.getTheMostPopular()))
            this.animals.add(animal);
    }
}
