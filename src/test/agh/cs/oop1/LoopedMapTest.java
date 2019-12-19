package agh.cs.oop1;

import agh.cs.oop1.simulation.Animal;
import agh.cs.oop1.simulation.LoopedMap;
import agh.cs.oop1.simulation.Plant;
import agh.cs.oop1.simulation.Vector2d;
import org.junit.Assert;
import org.junit.Test;

public class LoopedMapTest {
    @Test
    public void testLegalPositionAfterMove(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Assert.assertEquals(new Vector2d(0,0), map.legalPositionAfterMove(new Vector2d(10,10)));
        Assert.assertEquals(new Vector2d(1,1), map.legalPositionAfterMove(new Vector2d(11,11)));
        Assert.assertEquals(new Vector2d(9,9), map.legalPositionAfterMove(new Vector2d(-1,-1)));
    }

    @Test
    public void testGetJungleLowerLeft(){
        final Vector2d vector_1_1 = new Vector2d(1,1);
        final Vector2d vector_7_6 = new Vector2d(7,6);
        Vector2d lowerleft = LoopedMap.getJungleLowerLeft(vector_1_1,vector_7_6, 3,3);

        Assert.assertTrue(lowerleft.x >=2 && lowerleft.x <=4);
        Assert.assertTrue(lowerleft.y >=2 && lowerleft.y <=4);

        final Vector2d vector_m5_m5 = new Vector2d(-5,-5);
        final Vector2d vector_5_5 = new Vector2d(5,5);
        lowerleft = LoopedMap.getJungleLowerLeft(vector_m5_m5, vector_5_5, 3,3);
        Assert.assertTrue(lowerleft.x >=-2 && lowerleft.x <=0);
        Assert.assertTrue(lowerleft.y >=-2 && lowerleft.y <=0);

    }

    @Test
    public void testAnyAnimals(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);
        Assert.assertTrue(map.anyAnimals(position));
        Assert.assertFalse(map.anyAnimals(new Vector2d(4,4)));
    }

    @Test
    public void testIsPlantSet(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Plant plant = new Plant(position);

        Assert.assertFalse(map.isPlantSet(position));
        map.setPlantToCell(plant);
        Assert.assertTrue(map.isPlantSet(position));
    }

    @Test
    public void testPlaceAnimal(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
//        There is no need to call it directly -> it is called by Animal constructor
        Animal animal = new Animal(map, 100, position);
        Assert.assertTrue(map.getMapCell(position).hasAnimal(animal));
    }

    @Test
    public void testGetRandomPosition(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position =  map.getRandomPosition();
        Assert.assertEquals(position, map.legalPositionAfterMove(position));
    }

    @Test
    public void testGetRandomJunglePosition(){
        Vector2d mapLowerLeft = new Vector2d(0,0);
        Vector2d mapUpperRight = new Vector2d(10,10);
        Vector2d jungleLowerLeft = new Vector2d(4,4);
        Vector2d jungleUpperRight = new Vector2d(7,7);
        LoopedMap map = new LoopedMap(mapLowerLeft,jungleLowerLeft,mapUpperRight,jungleUpperRight);

        Vector2d position = map.getRandomJunglePosition();
        Assert.assertTrue(position.precedes(jungleUpperRight));
        Assert.assertTrue(position.follows(jungleLowerLeft));
    }

    @Test
    public void testMapSize(){
        LoopedMap map = new LoopedMap(13,154, 2, 2);
        Assert.assertEquals(13*154, map.mapSize());
    }


    @Test
    public void testGetRandomFreeOfGrassPosition(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);
        map.getMapCell(position).setPlant(new Plant(position));

        Vector2d rndFree = map.getRandomFreeOfGrassPosition();
        Assert.assertFalse(map.isPlantSet(rndFree));
        Assert.assertFalse(map.anyAnimals(rndFree));
        Assert.assertNotEquals(rndFree, position);
        Assert.assertTrue(rndFree.x < 10 && rndFree.y < 10 && rndFree.x >= 0 && rndFree.y >= 0);
    }

    @Test
    public void testGetRandomFreeOfGrassJunglePosition(){
        Vector2d mapLowerLeft = new Vector2d(0,0);
        Vector2d mapUpperRight = new Vector2d(10,10);
        Vector2d jungleLowerLeft = new Vector2d(4,4);
        Vector2d jungleUpperRight = new Vector2d(7,7);
        LoopedMap map = new LoopedMap(mapLowerLeft,jungleLowerLeft,mapUpperRight,jungleUpperRight);

        Vector2d position = new Vector2d(5,5);
        Animal animal = new Animal(map, 100, position);
        map.getMapCell(position).setPlant(new Plant(position));

        Vector2d rndFree = map.getRandomFreeOfGrassJunglePosition();
        Assert.assertFalse(map.isPlantSet(rndFree));
        Assert.assertFalse(map.anyAnimals(rndFree));
        Assert.assertNotEquals(rndFree, position);
        Assert.assertTrue(rndFree.precedes(jungleUpperRight));
        Assert.assertTrue(rndFree.follows(jungleLowerLeft));
    }

    @Test
    public void testDied(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);
        Assert.assertTrue(map.anyAnimals(position));
        map.died(animal);
        Assert.assertFalse(map.anyAnimals(position));
    }

    @Test
    public void testEnergyChanged() throws IllegalAccessException {
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal1 = new Animal(map, 100, position);
        Animal animal2 = new Animal(map, 200, position);

        Assert.assertEquals(animal2, map.getMapCell(position).getMostEnergeticAnimal());
        animal1.addEnergy(200);
        Assert.assertEquals(animal1, map.getMapCell(position).getMostEnergeticAnimal());
    }

}
