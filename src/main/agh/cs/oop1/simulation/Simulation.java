package agh.cs.oop1.simulation;

import org.javatuples.Pair;

import java.util.HashSet;
import java.util.Random;

public class Simulation {
    private LoopedMap map;
    private Configuration config;
    int epoch = 0;
    private static Random rand = new Random();

    public Simulation(Configuration config){
        this.config = config;
        this.map = new LoopedMap(config.width, config.height, config.jungleWidth(), config.jungleHeight());

        Plant.setEnergy(config.plantEnergy);
        Animal.setEnergyPerMove(config.moveEnergy);
        int numberOfAnimals = config.startAnimals;

        while(numberOfAnimals > 0){
            new Animal(map, config.startEnergy, map.getRandomPosition());
            numberOfAnimals--;
        }

        this.setPlants();
    }

    private void setPlants(int plants){
        while(plants > 0){
            Vector2d pos = this.map.getRandomFreeOfGrassPosition();
            this.map.setPlantToCell(new Plant(pos));
            plants--;
        }
    }
    private void setPlants(){
        this.setPlants(this.config.plantsPerEpoch);
    }

    public void nextEpoch() throws IllegalAccessException {
        this.epoch++;
        this.setPlants();

        HashSet<MapCell> cellsWithAnimals = new HashSet<MapCell>();
        for(Animal a : this.map.getAnimals()){
            a.move(MapDirection.getRandomDirection());
            cellsWithAnimals.add(this.map.getMapCell(a.getPosition()));
        }

        for(MapCell cell : cellsWithAnimals){
            if(cell.numberOfAnimals() == 1){
                if(!cell.isPlantSet())
                    continue;
                cell.getMostEnergeticAnimal().addEnergy(cell.removePlant().getEnergy());
            }else{
                Pair<Animal, Animal> pair= cell.getTwoMostEnergeticAnimals();
                Animal a1 = pair.getValue0();
                Animal a2 = pair.getValue1();
                if(cell.isPlantSet())
                    a1.addEnergy(cell.removePlant().getEnergy());

                Vector2d childpos = a1.getPosition();
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

                new Animal(this.map, a1.reproduceEnergy()+a2.reproduceEnergy(), childpos);
            }

        }

    }
}
