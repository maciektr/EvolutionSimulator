package agh.cs.oop1.simulation;

public class Plant implements IMapElement {
    private Vector2d position;
    private static int energy;

    public Plant(Vector2d position){
        this.position = position;
    }

    public static void setEnergy(int energy){
        Plant.energy = energy;
    }

    public int getEnergy(){
        return Plant.energy;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return "Plant: "+this.position.toString();
    }

}
