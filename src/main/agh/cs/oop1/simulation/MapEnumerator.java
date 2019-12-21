package agh.cs.oop1.simulation;

import jdk.internal.util.xml.impl.Pair;

public class MapEnumerator {
    private final LoopedMap map;

    public MapEnumerator(LoopedMap map){
        this.map = map;
    }

    public MapCell getMapCellByIndex(int column, int row){
        if(this.map.getMapLowerLeft().add(new Vector2d(column, row)).follows(this.map.getMapUpperRight()))
            throw new IllegalArgumentException("MapCell indexes out of bound!");
        return this.map.getMapCell(this.map.getMapLowerLeft().add(new Vector2d(column, row)));
    }

    public int mapSize(){
        return this.numberOfColumns() * this.numberOfRows();
    }

    public int numberOfColumns(){
        return (this.map.getMapUpperRight().x - this.map.getMapLowerLeft().x);
    }

    public int numberOfRows(){
        return (this.map.getMapUpperRight().y - this.map.getMapLowerLeft().y);
    }

    public Vector2d junglePositionIndexes(){
        return this.map.getJungleLowerLeft().substract(this.map.getMapLowerLeft());
    }

    public int numberOfJungleColumns(){
        return (this.map.getJungleUpperRight().x - this.map.getJungleLowerLeft().x);
    }

    public int numberOfJungleRows(){
        return (this.map.getJungleUpperRight().y - this.map.getJungleLowerLeft().y);
    }

    public boolean indexesInJungle(int c, int r){
        Vector2d pos = this.map.getMapLowerLeft().add(new Vector2d(c,r));
        return pos.follows(this.map.getJungleLowerLeft()) && pos.precedes(this.map.getJungleUpperRight());
    }
    public int getColumn(Vector2d position){
        return position.substract(this.map.getMapLowerLeft()).x;
    }
    public int getRow(Vector2d position){
        return position.substract(this.map.getMapLowerLeft()).y;
    }
}
