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
        Assert.assertTrue(map.anyAnimals(position.add(MapDirection.NORTH_EAST.toUnitVector())));
        Assert.assertFalse(map.anyAnimals(position));
    }

}
