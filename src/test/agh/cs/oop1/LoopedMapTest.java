package agh.cs.oop1;

import agh.cs.oop1.simulation.Animal;
import agh.cs.oop1.simulation.LoopedMap;
import agh.cs.oop1.simulation.Vector2d;
import org.junit.Assert;
import org.junit.Test;

public class LoopedMapTest {
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
    public void testLegalPositionAfterMove(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Assert.assertEquals(new Vector2d(0,0), map.legalPositionAfterMove(new Vector2d(10,10)));
        Assert.assertEquals(new Vector2d(1,1), map.legalPositionAfterMove(new Vector2d(11,11)));
        Assert.assertEquals(new Vector2d(9,9), map.legalPositionAfterMove(new Vector2d(-1,-1)));
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
    public void testAnyAnimals(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);
        Assert.assertTrue(map.anyAnimals(position));
        Assert.assertFalse(map.anyAnimals(new Vector2d(4,4)));
    }

    @Test
    public void testGetRandomFreePosition(){
        LoopedMap map = new LoopedMap(10,10, 2, 2);
        Vector2d position = new Vector2d(1,1);
        Animal animal = new Animal(map, 100, position);

        Vector2d rndFree = map.getRandomFreePosition();
        Assert.assertNotEquals(rndFree, position);
        Assert.assertTrue(rndFree.x < 10 && rndFree.y < 10 && rndFree.x >= 0 && rndFree.y >= 0);
        Vector2d rndFree2 = map.getRandomFreePosition();
        Vector2d rndFree3 = map.getRandomFreePosition();
        Assert.assertTrue(rndFree != rndFree2 || rndFree != rndFree3);
    }

}
