package agh.cs.oop1;

import agh.cs.oop1.simulation.Genotype;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

public class GenotypeTest {
    @Test
    public void testGetRandomGenotype(){
        ArrayList<Byte> gen = Genotype.getRandomGenotype().getGenotypeAsArrayList();
        for(Byte b : gen){
            Assert.assertTrue(b >= 0);
            Assert.assertTrue( b <= Genotype.maxGenValue);
        }
    }

    @Test
    public void testToString(){
        Assert.assertEquals(Genotype.genotypeSize, Genotype.getRandomGenotype().toString().length());
    }
}
