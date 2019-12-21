package agh.cs.oop1;

import agh.cs.oop1.simulation.MapDirection;
import agh.cs.oop1.simulation.Vector2d;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;

public class MapDirectionTest {
    @Test
    public void testNext(){
        Assert.assertEquals(MapDirection.SOUTH_EAST, MapDirection.EAST.next());
        Assert.assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_EAST.next());
        Assert.assertEquals(MapDirection.SOUTH_WEST, MapDirection.SOUTH.next());
        Assert.assertEquals(MapDirection.WEST, MapDirection.SOUTH_WEST.next());
        Assert.assertEquals(MapDirection.NORTH_WEST, MapDirection.WEST.next());
        Assert.assertEquals(MapDirection.NORTH, MapDirection.NORTH_WEST.next());
        Assert.assertEquals(MapDirection.NORTH_EAST,MapDirection.NORTH.next());
        Assert.assertEquals(MapDirection.EAST,MapDirection.NORTH_EAST.next());
    }

    @Test
    public void testPrevious(){
        Assert.assertEquals(MapDirection.NORTH_WEST, MapDirection.NORTH.previous());
        Assert.assertEquals(MapDirection.WEST, MapDirection.NORTH_WEST.previous());
        Assert.assertEquals(MapDirection.SOUTH_WEST, MapDirection.WEST.previous());
        Assert.assertEquals(MapDirection.SOUTH, MapDirection.SOUTH_WEST.previous());
        Assert.assertEquals(MapDirection.SOUTH_EAST, MapDirection.SOUTH.previous());
        Assert.assertEquals(MapDirection.EAST, MapDirection.SOUTH_EAST.previous());
        Assert.assertEquals(MapDirection.NORTH_EAST, MapDirection.EAST.previous());
        Assert.assertEquals(MapDirection.NORTH, MapDirection.NORTH_EAST.previous());

    }

    @Test
    public void testGetRandomDirection(){
        Assert.assertNotNull(MapDirection.getRandomDirection());
    }

    @Test
    public void testToUnitVector(){
        Assert.assertNotNull(MapDirection.getRandomDirection().toUnitVector());
        Assert.assertNotEquals(MapDirection.getRandomDirection().toUnitVector(), new Vector2d(0, 0));
        Assert.assertFalse(MapDirection.getRandomDirection().toUnitVector().x > 1);
        Assert.assertFalse(MapDirection.getRandomDirection().toUnitVector().x < -1);
        Assert.assertFalse(MapDirection.getRandomDirection().toUnitVector().y > 1);
        Assert.assertFalse(MapDirection.getRandomDirection().toUnitVector().y < -1);
    }

}
