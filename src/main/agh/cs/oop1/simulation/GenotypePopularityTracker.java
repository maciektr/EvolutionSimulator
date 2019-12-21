package agh.cs.oop1.simulation;

import java.util.HashMap;
import java.util.Map;

public class GenotypePopularityTracker {
    private Map<Genotype, Integer> tracker = new HashMap<>();

    public Genotype getMostPopular() throws IllegalAccessException {
        if(this.trackerSize()==0)
            throw new IllegalAccessException("There is no genotypes stored in log!");
        return this.tracker.entrySet().stream().max((entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).get().getKey();
    }

    public int trackerSize(){
        return this.tracker.size();
    }

    public void spotGenotype(Genotype genotype){

    }
}
