package agh.cs.oop1;

import agh.cs.oop1.simulation.*;
import org.junit.Assert;
import org.junit.Test;

public class MapCellTest {
    @Test
    public void testAnimal(){
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
}
