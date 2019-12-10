package agh.cs.oop1.simulation;

import java.util.Random;

public enum MapDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST;

    public String toString(){
        switch(this){
            case EAST:
                return "East";
            case WEST:
                return "West";
            case NORTH:
                return "North";
            default:
                return "South";
        }
    }

    public MapDirection next(){
        return MapDirection.values()[(this.ordinal()+1)%MapDirection.values().length];
    }

    public MapDirection previous(){
        return MapDirection.values()[(this.ordinal()-1+MapDirection.values().length)%MapDirection.values().length];
    }

    public Vector2d toUnitVector(){
        switch(this){
            case EAST:
                return new Vector2d(1,0);
            case WEST:
                return new Vector2d(-1,0);
            case NORTH:
                return new Vector2d(0,1);
            default:
                return new Vector2d(0,-1);
        }
    }

    public static MapDirection getRandomDirection(){
        Random rand = new Random();
        return MapDirection.values()[rand.nextInt(MapDirection.values().length)];
    }
}
