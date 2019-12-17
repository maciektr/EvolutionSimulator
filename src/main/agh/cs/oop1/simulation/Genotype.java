package agh.cs.oop1.simulation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Genotype {
    public static final byte maxGenValue = 7;
    public static final int genotypeSize = 32;

    private static final Random rand = new Random();
    private final byte[] genotype;

    public Genotype(){
         this.genotype = new byte[maxGenValue+1];
        for(byte i = 0; i<=maxGenValue; i++)
            this.genotype[i] = 1;

        byte left = genotypeSize - maxGenValue - 1;
        while(left > 0){
            this.genotype[Genotype.rand.nextInt(Genotype.maxGenValue+1)]++;
            left--;
        }
    }

    Genotype(byte[] genotype){
        if(genotype.length != Genotype.maxGenValue+1)
            throw new IllegalArgumentException("Genotype size is not valid!");
        this.genotype = genotype;
    }

    public String toString(){
        StringBuilder res= new StringBuilder();
        for(Byte b : this.getGenotypeAsArrayList())
            res.append((char) (b + (int) '0'));
        return res.toString();
    }

    public ArrayList<Byte> getGenotypeAsArrayList(){
        ArrayList<Byte> res = new ArrayList<>();
        for(byte i = 0; i<= Genotype.maxGenValue; i++)
            for(int k =0; k<this.genotype[i]; k++)
                res.add((Byte)i);
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


    public Genotype cross(Genotype other){
        byte[] child = new byte[Genotype.maxGenValue+1];

        int first = Genotype.rand.nextInt(Genotype.genotypeSize);
        int second = Genotype.rand.nextInt(Genotype.genotypeSize);
        if(first > second) {
            int tmp = first;
            first = second;
            second = tmp;
        }

        boolean fromFirst = Genotype.rand.nextBoolean();
        for(int m = 0; m<first; m++)
            child[(fromFirst ? this.getGenotypeAsArrayList().get(m) : other.getGenotypeAsArrayList().get(m))]++;

        fromFirst = Genotype.rand.nextBoolean();
        for(int m = first; m<second; m++)
            child[(fromFirst ? this.getGenotypeAsArrayList().get(m) : other.getGenotypeAsArrayList().get(m))]++;

        fromFirst = Genotype.rand.nextBoolean();
        for(int m = second; m<Genotype.genotypeSize; m++)
            child[(fromFirst ? this.getGenotypeAsArrayList().get(m) : other.getGenotypeAsArrayList().get(m))]++;

        int err = 0;
        for(int i = 0; i <= Genotype.maxGenValue; i++){
            if(child[i] <= 0) {
                child[i] = 1;
                err++;
            }
        }
        while(err > 0) {
            int k = Genotype.rand.nextInt(Genotype.maxGenValue + 1);
            while (child[k] <= 1)
                k = Genotype.rand.nextInt(Genotype.maxGenValue + 1);
            child[k]--;
            err--;
        }

        return new Genotype(child);
    }
}