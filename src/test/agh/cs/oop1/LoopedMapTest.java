package agh.cs.oop1;

import org.junit.Assert;
import org.junit.Test;

public class LoopedMapTest {
    @Test
    public void testGetJungleLowerLeft(){
        final Vector2d vector_1_1 = new Vector2d(1,1);
        final Vector2d vector_7_6 = new Vector2d(7,6);
        final Vector2d vector_3_3 = new Vector2d(3,3);
        final Vector2d vector_m13_13 = new Vector2d(-19, 13);
        final Vector2d vector_m19_19 = new Vector2d(-13, 19);
        final Vector2d vector_m16_15 = new Vector2d(-16,15);
        Assert.assertEquals(vector_3_3, LoopedMap.getJungleLowerLeft(vector_1_1,vector_7_6, 3,3));
//        Assert.assertEquals(vector_m16_15, LoopedMap.getJungleLowerLeft(vector_m13_13,vector_m19_19,3,3));

        final Vector2d vector_m1_m1 = new Vector2d(-1,-1);
        final Vector2d vector_m5_m5 = new Vector2d(-5,-5);
        final Vector2d vector_5_5 = new Vector2d(5,5);
        Assert.assertEquals(vector_m1_m1, LoopedMap.getJungleLowerLeft(vector_m5_m5, vector_5_5, 3,3));

    }

}
