package agh.cs.oop1;

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

}
