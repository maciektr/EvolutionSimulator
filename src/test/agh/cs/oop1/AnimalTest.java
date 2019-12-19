package agh.cs.oop1;

import agh.cs.oop1.simulation.Animal;
import agh.cs.oop1.simulation.LoopedMap;
import agh.cs.oop1.simulation.MapDirection;
import agh.cs.oop1.simulation.Vector2d;
import org.junit.Assert;
import org.junit.Test;

public class AnimalTest {
    @Test
    public void testMove(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position,MapDirection.NORTH_EAST);
        Assert.assertTrue(map.anyAnimals(position));

        animal.move(MapDirection.NORTH_EAST);
        Vector2d afterMove =  position.add(MapDirection.NORTH_EAST.toUnitVector());
        Assert.assertEquals(afterMove, animal.getPosition());
        Assert.assertTrue(map.anyAnimals(afterMove));
        Assert.assertFalse(map.anyAnimals(position));
    }

    @Test
    public void testSetEnergyPerMove(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal.setEnergyPerMove(10);
        Animal animal = new Animal(map, 100, position,MapDirection.NORTH_EAST);
        Assert.assertEquals(100, animal.getEnergy());
        animal.move(MapDirection.NORTH_EAST);
        Assert.assertEquals(90, animal.getEnergy());
        Animal.setEnergyPerMove(20);
        animal.move(MapDirection.NORTH_EAST);
        Assert.assertEquals(70,animal.getEnergy());
    }

    @Test
    public void testReproduceEnergy() {
        LoopedMap map = new LoopedMap(10, 10, 2, 2);
        Vector2d position = new Vector2d(1, 1);
        Animal.setEnergyPerMove(10);
        int startEnergy = 100;
        Animal animal = new Animal(map, startEnergy, position, MapDirection.NORTH_EAST);

        int child = animal.reproduceEnergy();
        Assert.assertEquals(startEnergy / 4, child);
        Assert.assertEquals(startEnergy - (startEnergy / 4), animal.getEnergy());

        startEnergy = startEnergy - (startEnergy / 4);
        child = animal.reproduceEnergy();
        Assert.assertEquals(startEnergy / 4, child);
        Assert.assertEquals(startEnergy - (startEnergy / 4), animal.getEnergy());
    }

    @Test
    public void testDied(){
        LoopedMap map = new LoopedMap(10, 10, 2, 2);
        Vector2d position = new Vector2d(1, 1);
        Animal.setEnergyPerMove(10);
        Animal animal = new Animal(map, 10, position, MapDirection.NORTH_EAST);

        Assert.assertTrue(map.anyAnimals(animal.getPosition()));
        animal.move(MapDirection.NORTH_EAST);
        Assert.assertFalse(map.anyAnimals(position));
        Assert.assertFalse(map.anyAnimals(position.add(MapDirection.NORTH_EAST.toUnitVector())));
    }

}
