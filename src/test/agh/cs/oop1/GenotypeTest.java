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
        Assert.assertEquals(Genotype.genotypeSize, new Genotype().getGenotypeAsArrayList().size());
        byte[] gen = new byte[Genotype.genotypeSize];
        for(byte i = 0; i<Genotype.genotypeSize; i++)
            gen[i] = i;
        ArrayList<Byte> received = new Genotype(gen).getGenotypeAsArrayList();
        for(byte i = 0; i<Genotype.genotypeSize; i++)
            Assert.assertEquals(gen[i], (byte) received.get(i));
    }

    @Test
    public void testToString(){
        Assert.assertEquals(Genotype.genotypeSize, new Genotype().toString().length());
        byte[] gen = new byte[Genotype.genotypeSize];
        for(byte i = 0; i<Genotype.genotypeSize; i++)
            gen[i] = i;
        String received = new Genotype(gen).toString();
        for(byte i = 0; i<Genotype.genotypeSize; i++)
            Assert.assertEquals(gen[i], (int)received.charAt(i) - (int)'0');
    }

    @Test
    public void testGetMove(){
        Assert.assertNotNull(new Genotype().getMove());
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
