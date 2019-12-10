package agh.cs.oop1.simulation;

public class Plant implements IMapElement {
    Vector2d position;
    Plant(Vector2d position){
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
