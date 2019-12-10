package agh.cs.oop1;

import java.util.Random;

public class Genotype {
    private static byte maxGenValue=7;

    private byte[] genotype = new byte[32];


    public static Genotype getRandomGenotype(){
        Random rand = new Random();
        Genotype gen = new Genotype();

        for(int i = 0; i< gen.genotype.length; i++){
            gen.genotype[i] = (byte)rand.nextInt(maxGenValue);
        }
        return gen;
    }
}
