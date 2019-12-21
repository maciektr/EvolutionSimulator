package agh.cs.oop1;

import agh.cs.oop1.simulation.LoopedMap;
import agh.cs.oop1.simulation.MapEnumerator;
import org.junit.Assert;
import org.junit.Test;

public class MapEnumeratorTest {
    @Test
    public void testMapSize(){
        MapEnumerator enumerator = new MapEnumerator(new LoopedMap(13,154, 2, 2));
        Assert.assertEquals(13*154, enumerator.mapSize());
    }

}
