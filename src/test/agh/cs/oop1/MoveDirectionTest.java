package agh.cs.oop1;

import agh.cs.oop1.simulation.MoveDirection;
import org.junit.Assert;
import org.junit.Test;

public class MoveDirectionTest {
    @Test
    public void testGetRandomDirection(){
        Assert.assertNotNull(MoveDirection.getRandomDirection());
    }
}
