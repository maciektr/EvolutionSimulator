package agh.cs.oop1;

import java.util.Random;

public enum MoveDirection {
    FORWARD,
    BACKWARD,
    RIGHT,
    LEFT;

    public static MoveDirection getRandomDirection(){
        Random rand = new Random();
        return MoveDirection.values()[rand.nextInt(MoveDirection.values().length)];
    }

}
