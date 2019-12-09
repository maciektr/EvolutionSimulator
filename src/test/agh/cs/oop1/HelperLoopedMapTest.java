package agh.cs.oop1;

import org.junit.Assert;
import org.junit.Test;

public class HelperLoopedMapTest {

    @Test
    public void testGetUpperRight(){
        final Vector2d vector = new Vector2d(-10,-20);
        final Vector2d vector_0_0 = new Vector2d(0,0);
        Assert.assertEquals(vector_0_0, HelperLoopedMap.getUpperRight(vector, 10,20));
    }


}
