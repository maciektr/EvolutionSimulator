package agh.cs.oop1.simulation;

import org.javatuples.Triplet;
import org.javatuples.Unit;
import org.javatuples.Pair;


import java.util.*;
import java.util.function.UnaryOperator;

//Nazwa klasy odgapiona od @DenkoV
public class LoopedMap implements IWorldMap, IPositionChangeObserver {
    protected List<Animal> animals = new ArrayList<Animal>();
    protected Map<Vector2d, MapCell> objects = new HashMap<>();

    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Random rand = new Random();


    public LoopedMap(Vector2d mapLowerLeft, Vector2d jungleLowerLeft, int mapSideA, int mapSideB, int jungleSideA, int jungleSideB) {
        HelperLoopedMap.checkIfInputIsLegal(mapSideA,mapSideB,jungleSideA,jungleSideB);
        this.mapLowerLeft = mapLowerLeft;
        this.mapUpperRight = HelperLoopedMap.getUpperRight(this.mapLowerLeft, mapSideA, mapSideB);
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = HelperLoopedMap.getUpperRight(this.jungleLowerLeft, jungleSideA, jungleSideB);
        HelperLoopedMap.checkIfInputIsLegal(this.mapLowerLeft,this.mapUpperRight,this.jungleLowerLeft,this.jungleUpperRight);
    }

    public LoopedMap(int mapWidth, int mapHeight, int jungleWidth, int jungleHeight){
        this(new Vector2d(0,0),
                getJungleLowerLeft(new Vector2d(0,0),HelperLoopedMap.getUpperRight(new Vector2d(0,0),mapWidth,mapHeight),jungleWidth,jungleHeight),
                mapWidth,mapHeight,jungleWidth,jungleHeight);
    }

    public LoopedMap(Vector2d mapLowerLeft, Vector2d jungleLowerLeft, Vector2d mapUpperRight, Vector2d jungleUpperRight){
        HelperLoopedMap.checkIfInputIsLegal(mapLowerLeft,mapUpperRight,jungleLowerLeft,jungleUpperRight);
        this.mapLowerLeft = mapLowerLeft;
        this.mapUpperRight = mapUpperRight;
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
    }

    public Vector2d legalPositionAfterMove(Vector2d afterMove){
        int w = this.mapUpperRight.x - this.mapLowerLeft.x;
        int h = this.mapUpperRight.y - this.mapLowerLeft.y;

        int xAfterMove = afterMove.x - this.mapLowerLeft.x;
        int yAfterMove = afterMove.y - this.mapLowerLeft.y;
        while(xAfterMove < 0) xAfterMove+=w;
        while(yAfterMove < 0) yAfterMove+=h;
        xAfterMove+=w;
        yAfterMove+=h;

        xAfterMove %= w;
        yAfterMove %= h;

        return new Vector2d(xAfterMove+this.mapLowerLeft.x, yAfterMove+this.mapLowerLeft.y);
    }

    public static Vector2d getJungleLowerLeft(Vector2d mapLowerLeft,Vector2d mapUpperRight, int jungleSideA, int jungleSideB){
        int x = mapUpperRight.x + mapLowerLeft.x;
        int y = mapUpperRight.y + mapLowerLeft.y;
        UnaryOperator<Integer> sgn = k -> {
          if(k>=0)
              return 1;
          else
              return -1;
        };
        return new Vector2d(sgn.apply(x)*(Math.abs(x) -jungleSideA)/2+x%2, sgn.apply(y)*(Math.abs(y)-jungleSideB)/2+y%2);
    }


    private MapCell getMapCell(Vector2d position){
        return this.objects.get(position);
    }

    private boolean anyAnimals(Vector2d position){
        return this.getMapCell(position).animals.isEmpty();
    }


    public void place(Animal animal){
        MapCell cell = this.objects.get(animal.getPosition());
        cell.animals.add(animal);
        this.objects.put(animal.getPosition(),cell);

        this.animals.add(animal);
        animal.addObserver(this);
    }

    public Vector2d getRandomFreePosition(){
        int w = this.mapUpperRight.x - this.mapLowerLeft.x;
        int h = this.mapUpperRight.y - this.mapLowerLeft.y;

        int randW = this.rand.nextInt(w);
        int randH = this.rand.nextInt(h);
        int countOperations = 0;

        while(!this.anyAnimals(this.mapLowerLeft.add(new Vector2d(randW, randH)))){
            if(countOperations > 1000000)
                throw new RuntimeException("Cannot find random free position on map!");
            randW = this.rand.nextInt(w);
            randH = this.rand.nextInt(h);
            countOperations++;
        }

        return this.mapLowerLeft.add(new Vector2d(randW, randH));
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element){
        if(element instanceof Animal) {
            this.getMapCell(oldPosition).removeAnimal((Animal)element);
            this.getMapCell(newPosition).addAnimal((Animal)element);
        }
    }

    public boolean equals(Object other){
        if (this == other)
            return true;
        if (!(other instanceof LoopedMap))
            return false;
        LoopedMap that = (LoopedMap) other;
        return this.mapLowerLeft.equals(that.mapLowerLeft)
                && this.mapUpperRight.equals(that.mapUpperRight)
                && this.jungleLowerLeft.equals(that.jungleLowerLeft)
                && this.jungleUpperRight.equals(that.jungleUpperRight)
                && this.animals.equals(that.animals)
                && this.objects.equals(that.objects);
    }


}
