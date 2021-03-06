package agh.cs.oop1.simulation;

import org.javatuples.Pair;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

public class Simulation {
    private LoopedMap map;
    private Configuration config;
    private static Random rand = new Random();
    private Statistics statistics;

    public Simulation(Configuration config){
        this.config = config;
        this.map = new LoopedMap(config.width, config.height, config.jungleWidth(), config.jungleHeight());

        Plant.setEnergy(config.plantEnergy);
        Animal.setEnergyPerMove(config.moveEnergy);
        int numberOfAnimals = config.startAnimals;
        this.statistics = new Statistics();

        while(numberOfAnimals > 0){
            Animal a = new Animal(map, config.startEnergy, map.getRandomPosition());
            this.statistics.getTracker().spotGenotype(a.getGenotype());
            numberOfAnimals--;
        }
        for(Animal a : this.map.getAnimals()){
            if(a.getGenotype().equals(this.statistics.getDominantGenotype()))
                this.statistics.getTracker().storeAnimal(a);
        }

        this.setPlants();
        this.setOnlyJunglePlants();
    }

    private void setPlants(int plants){
        this.setOnlyJunglePlants(plants);
        plants = plants - (plants/2);
        while(plants > 0){
            Vector2d pos = this.map.getRandomFreeOfGrassPosition();
            if(this.map.getMapCell(pos)==null ||(this.map.getMapCell(pos)!=null && !this.map.getMapCell(pos).isPlantSet()))
                this.statistics.spotNewPlant();

            this.map.setPlantToCell(new Plant(pos));
            plants--;
        }
    }

    private void setPlants(){
        this.setPlants(this.config.plantsPerEpoch);
    }

    private void setOnlyJunglePlants(int plants){
        for(int i = 0; i<(plants/2); i++){
            Vector2d pos = this.map.getRandomJunglePosition();
            if(this.map.getMapCell(pos)==null ||(this.map.getMapCell(pos)!=null && !this.map.getMapCell(pos).isPlantSet()))
                this.statistics.spotNewPlant();
            this.map.setPlantToCell(new Plant(pos));
        }
    }

    private void setOnlyJunglePlants(){
        this.setOnlyJunglePlants(this.config.plantsPerEpoch);
    }

    private Vector2d getChildPos(Vector2d position){
        Vector2d childpos = position;
        int m1 = Simulation.rand.nextInt(3);
        int m2 = Simulation.rand.nextInt(3);

        boolean found = false;
        for(int i1 = 0; i1<3 && !found; i1++)
            for(int i2 = 0; i2<3 && !found; i2++){
                if(!this.map.anyAnimals(this.map.legalPositionAfterMove(childpos.add(new Vector2d((m1+i1)%3 -1, (m2+i2)%3 -1))))){
                    childpos = childpos.add(new Vector2d((m1+i1)%3 -1, (m2+i2)%3 -1));
                    found = true;
                }
            }
        return childpos;
    }

    public void nextEpoch() throws IllegalAccessException {
        this.statistics.spotEpoch();
        this.setPlants();

        HashSet<MapCell> cellsWithAnimals = new HashSet<MapCell>();
        ArrayList<Animal> animals = new ArrayList<Animal>(this.map.getAnimals());
        for(Animal a : animals){
            a.move(MapDirection.getRandomDirection());
            cellsWithAnimals.add(this.map.getMapCell(a.getPosition()));
            if(a.getEnergy() <= 0){
                this.statistics.spotDeadAnimal(this.statistics.getEpoch());
            }
        }

        for(MapCell cell : cellsWithAnimals){
            if(cell.numberOfAnimals() == 1){
                if(!cell.isPlantSet())
                    continue;
                int gainedEnergy = cell.removePlant().getEnergy();
                this.statistics.spotEatenPlant();
                cell.getMostEnergeticAnimal().addEnergy(gainedEnergy);
            }else if(cell.numberOfAnimals() >= 2){
                Pair<Animal, Animal> pair= cell.getTwoMostEnergeticAnimals();
                Animal a1 = pair.getValue0();
                Animal a2 = pair.getValue1();
                if(cell.isPlantSet()) {
                    a1.addEnergy(cell.removePlant().getEnergy());
                    this.statistics.spotEatenPlant();
                }

                new Animal(this.map, a1.reproduceEnergy()+a2.reproduceEnergy(), this.getChildPos(a1.getPosition()));
                a1.spotChild();
                a2.spotChild();
            }
        }
        this.statistics.update(this.map.getAnimals());
    }

    public LoopedMap getMap(){
        return this.map;
    }

    public int getEpoch(){return this.statistics.getEpoch();}

    public int getNumberOfAnimals(){
        return this.map.getAnimals().size();
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
