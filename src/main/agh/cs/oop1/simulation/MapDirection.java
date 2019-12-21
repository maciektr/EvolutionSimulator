package agh.cs.oop1.simulation;

import java.util.Random;

public enum MapDirection {
    NORTH,
    NORTH_EAST,
    EAST,
    SOUTH_EAST,
    SOUTH,
    SOUTH_WEST,
    WEST,
    NORTH_WEST;

    public String toString(){
        switch(this){
            case NORTH:
                return "North";
            case NORTH_EAST:
                return "North east";
            case EAST:
                return "East";
            case SOUTH_EAST:
                return "South east";
            case SOUTH:
                return "South";
            case SOUTH_WEST:
                return "South west";
            case WEST:
                return "West";
            default:
                return "North west";
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
            case SOUTH:
                return new Vector2d(0,-1);
            case NORTH_EAST:
                return new Vector2d(1,1);
            case NORTH_WEST:
                return new Vector2d(-1,1);
            case SOUTH_EAST:
                return new Vector2d(1,-1);
            case SOUTH_WEST:
                return new Vector2d(-1,-1);
            default:
                return new Vector2d(0,0);
        }
    }

    public static MapDirection getRandomDirection(){
        Random rand = new Random();
        return MapDirection.values()[rand.nextInt(MapDirection.values().length)];
    }
}
