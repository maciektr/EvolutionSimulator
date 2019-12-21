package agh.cs.oop1;

import agh.cs.oop1.simulation.*;
import org.javatuples.Pair;
import org.junit.Assert;
import org.junit.Test;

public class MapCellTest {
    @Test
    public void testNumberOfAnimals(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Animal.setEnergyPerMove(1);
        Vector2d position1 = new Vector2d(1,1);
        for(int i = 0; i<10; i++)
            new Animal(map, 100, position1);

        Vector2d position2 = new Vector2d(2,2);
        Animal animal = new Animal(map, 100, position2, MapDirection.SOUTH_WEST);
        for(int i = 0; i<499; i++)
            new Animal(map, 100, position2);

        Assert.assertEquals(10, map.getMapCell(position1).numberOfAnimals());
        Assert.assertEquals(500, map.getMapCell(position2).numberOfAnimals());
        animal.move(MapDirection.SOUTH_WEST);
        Assert.assertEquals(position1, animal.getPosition());
        Assert.assertEquals(11, map.getMapCell(position1).numberOfAnimals());
        Assert.assertEquals(499, map.getMapCell(position2).numberOfAnimals());
    }

    @Test
    public void testGetTwoMostEnergeticAnimals() throws IllegalAccessException {
        LoopedMap map = new LoopedMap(10, 10, 2, 2);
        Vector2d position = new Vector2d(1, 1);
        Animal.setEnergyPerMove(10);
        Animal animal1 = new Animal(map, 110, position, MapDirection.NORTH_EAST);
        Animal animal2 = new Animal(map, 100, position, MapDirection.NORTH_EAST);

        Pair<Animal, Animal> res = map.getMapCell(position).getTwoMostEnergeticAnimals();
        Assert.assertEquals(animal1, res.getValue0());
        Assert.assertEquals(animal2, res.getValue1());

        new Animal(map, 10, position, MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getTwoMostEnergeticAnimals();
        Assert.assertEquals(animal1, res.getValue0());
        Assert.assertEquals(animal2, res.getValue1());

        Animal animal3 = new Animal(map, 105, position, MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getTwoMostEnergeticAnimals();
        Assert.assertEquals(animal1, res.getValue0());
        Assert.assertEquals(animal3, res.getValue1());

        animal1.move(MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getTwoMostEnergeticAnimals();
        Assert.assertEquals(animal3, res.getValue0());
        Assert.assertEquals(animal2, res.getValue1());
    }

    @Test
    public void testGetMostEnergeticAnimal() throws IllegalAccessException {
        LoopedMap map = new LoopedMap(10, 10, 2, 2);
        Vector2d position = new Vector2d(1, 1);
        Animal.setEnergyPerMove(10);
        Animal animal1 = new Animal(map, 110, position, MapDirection.NORTH_EAST);
        Animal animal2 = new Animal(map, 100, position, MapDirection.NORTH_EAST);

        Animal res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal1, res);

        Animal animal4 = new Animal(map, 76, position, MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal1, res);

        animal1.move(MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal2, res);

        Animal animal3 = new Animal(map, 105, position, MapDirection.NORTH_EAST);
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal3, res);

        map.getMapCell(position).removeAnimal(animal3);
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal2, res);

        animal2.reproduceEnergy();
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal4, res);

        animal2.addEnergy(10);
        res = map.getMapCell(position).getMostEnergeticAnimal();
        Assert.assertEquals(animal2, res);
    }

    @Test
    public void testRemoveAnimal(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);
        Assert.assertTrue(map.getMapCell(position).anyAnimals());
        map.getMapCell(position).removeAnimal(animal);
        Assert.assertFalse(map.getMapCell(position).anyAnimals());
    }

    @Test
    public void testAnyAnimals(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);

        MapCell testCell = new MapCell();
        Assert.assertFalse(testCell.anyAnimals());

        testCell.addAnimal(animal);
        Assert.assertTrue(testCell.anyAnimals());

        testCell.removeAnimal(animal);
        Assert.assertFalse(testCell.anyAnimals());
    }

    @Test
    public void testHasAnimal(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);

        MapCell testCell = new MapCell();
        Assert.assertFalse(testCell.hasAnimal(animal));
        testCell.addAnimal(animal);
        Assert.assertTrue(testCell.hasAnimal(animal));
        testCell.removeAnimal(animal);
        Assert.assertFalse(testCell.hasAnimal(animal));
    }


    @Test
    public void testRemovePlant(){
        MapCell testCell = new MapCell();
        Vector2d position_1 = new Vector2d(1,1);
        Plant plant = new Plant(position_1);

        testCell.setPlant(plant);
        Plant got = testCell.removePlant();

        Assert.assertNotNull(got);
        Assert.assertEquals(plant, got);

        got = testCell.removePlant();
        Assert.assertNull(got);
    }

    @Test
    public void testSetPlant(){
        MapCell testCell = new MapCell();
        Vector2d position_1 = new Vector2d(1,1);
        Plant plant = new Plant(position_1);

        Vector2d position_2 = new Vector2d(2,2);

        Plant plant_2 = new Plant(position_2);
        testCell.setPlant(plant);
        testCell.setPlant(plant_2);
        Plant got = testCell.removePlant();

        Assert.assertNotNull(got);
        Assert.assertEquals(got, plant_2);
        Assert.assertNotEquals(got, plant);
    }

}
