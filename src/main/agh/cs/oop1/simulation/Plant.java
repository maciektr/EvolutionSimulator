package agh.cs.oop1.simulation;

public class Plant implements IMapElement {
    private Vector2d position;
    public Plant(Vector2d position){
        this.position = position;
    }

    public Vector2d getPosition(){
        return this.position;
    }

    @Override
    public String toString(){
        return "Plant: "+this.position.toString();
    }

}
