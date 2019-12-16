package agh.cs.oop1.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genotype {
    public static final byte maxGenValue = 7;
    public static final int genotypeSize = 32;

    private static final Random rand = new Random();
    private byte[] genotype = new byte[genotypeSize];

    public static Genotype getRandomGenotype() {
        Genotype gen = new Genotype();

        for (int i = 0; i < gen.genotype.length; i++)
            gen.genotype[i] = (byte) Genotype.rand.nextInt(maxGenValue+1);

        return gen;
    }

    public String toString(){
        StringBuilder res= new StringBuilder();
        for(byte b : this.genotype)
            res.append((char) (b + (int) '0'));
        return res.toString();
    }

    public ArrayList<Byte> getGenotypeAsArrayList(){
        ArrayList<Byte> res = new ArrayList<>();
        for (byte b : this.genotype)
            res.add((Byte) b);
        return res;
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof Genotype))
            return false;
        Genotype that = (Genotype) other;
        return Arrays.equals(this.genotype, that.genotype);
    }

}
