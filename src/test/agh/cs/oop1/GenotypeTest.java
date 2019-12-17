package agh.cs.oop1;

import agh.cs.oop1.simulation.Genotype;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class GenotypeTest {
    @Test
    public void testGetRandomGenotype(){
        ArrayList<Byte> gen = new Genotype().getGenotypeAsArrayList();
        for(Byte b : gen){
            Assert.assertTrue(b >= 0);
            Assert.assertTrue( b <= Genotype.maxGenValue);
        }
    }

    @Test
    public void testGetGenotypeAsArrayList(){

    }

    @Test
    public void testToString(){
        Assert.assertEquals(Genotype.genotypeSize, new Genotype().toString().length());
    }

    @Test
    public void testGetMove(){

    }

    @Test
    public void testCross(){
        Genotype gen1 = new Genotype();
        Genotype gen2 = new Genotype();

        Genotype child = gen1.cross(gen2);
        ArrayList<Byte> got = child.getGenotypeAsArrayList();

        int[] seen = new int[Genotype.maxGenValue+1];
        for(Byte b : got)
            seen[b]++;

        int sum = 0;
        for(int s :seen) {
            sum+=s;
            Assert.assertTrue(s >= 1);
        }
        Assert.assertEquals(Genotype.genotypeSize, sum);
    }


}
