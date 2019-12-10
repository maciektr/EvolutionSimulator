package agh.cs.oop1;

import org.javatuples.Triplet;
import org.javatuples.Unit;
import org.javatuples.Pair;
import sun.awt.im.InputMethodAdapter;


import java.util.*;
import java.util.function.UnaryOperator;

//Nazwa klasy odgapiona od @DenkoV
public class LoopedMap implements IWorldMap, IPositionChangeObserver {
    protected List<Animal> animals = new ArrayList<Animal>();
    protected Map<Vector2d, Iterable> objects = new HashMap<>();


    final Vector2d mapLowerLeft;
    final Vector2d mapUpperRight;
    final Vector2d jungleLowerLeft;
    final Vector2d jungleUpperRight;

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

    static Vector2d getJungleLowerLeft(Vector2d mapLowerLeft,Vector2d mapUpperRight, int jungleSideA, int jungleSideB){
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

    public Object objectAt(Vector2d position){
        return objects.get(position);
    }

    private boolean isFree(Vector2d position){
        return this.objectAt(position) == null;
    }

    public void place(Animal animal){
        if(!this.isFree(animal.getPosition())){
            throw new IllegalArgumentException("Cannot place element on map.\n Position "+animal.getPosition().toString() + " is already occupied.");
        }
        this.objects.put(animal.getPosition(),new Unit<IMapElement>(animal));
        this.animals.add(animal);
        animal.addObserver(this);
    }

    public boolean canMoveTo(Vector2d afterMove){
        if(this.objectAt(afterMove) instanceof Pair){
            return ((Pair) this.objectAt(afterMove)).getValue0() instanceof Animal
                    && ((Pair) this.objectAt(afterMove)).getValue1() instanceof Animal;
        }
        return !(this.objectAt(afterMove) instanceof Triplet);
    }

    public Vector2d getRandomFreePosition(){
        int w = this.mapUpperRight.x - this.mapLowerLeft.x;
        int h = this.mapUpperRight.y - this.mapLowerLeft.y;
        Random rand = new Random();

        int randW = rand.nextInt(w);
        int randH = rand.nextInt(h);
        int countOperations = 0;

        while(!this.isFree(this.mapLowerLeft.add(new Vector2d(randW, randH)))){
            if(countOperations > 1000000)
                throw new RuntimeException("Cannot find random free position on map!");
            randW = rand.nextInt(w);
            randH = rand.nextInt(h);
            countOperations++;
        }

        return this.mapLowerLeft.add(new Vector2d(randW, randH));
    }

//    private void takeOutToMove(Object a,Vector2d oldPosition, IMapElement element){
//        if(a instanceof Unit){
//            objects.remove(oldPosition);
//
//        }else if(a instanceof Pair){
//            if(((Pair) a).getValue0()!= element && ((Pair) a).getValue1() != element)
//                throw new IllegalArgumentException("Cannot move map element!\n No such element at given position!");
//
//            IMapElement untouched = (IMapElement) ((Pair) a).getValue0();
//            if(((Pair) a).getValue0() == element)
//                untouched = (IMapElement) ((Pair) a).getValue1();
//
//            objects.remove(oldPosition);
//            objects.put(oldPosition, new Unit<IMapElement>(untouched));
//
//        }else if(a instanceof Triplet) {
//            if(((Triplet) a).getValue0()!= element && ((Triplet) a).getValue1() != element && ((Triplet) a).getValue2() != element)
//                throw new IllegalArgumentException("Cannot move map element!\n No such element at given position!");
//
//            IMapElement untouched1 = (IMapElement)((Triplet) a).getValue0();
//            IMapElement untouched2 = (IMapElement)((Triplet) a).getValue1();
//            if(untouched1 == element)
//                    untouched1 = (IMapElement)((Triplet) a).getValue2();
//            if(untouched2 == element)
//                    untouched2 = (IMapElement)((Triplet) a).getValue2();
//
//
//            objects.remove(oldPosition);
//            objects.put(oldPosition, new Pair<IMapElement, IMapElement>(untouched1, untouched2));
//
//        }else
//            throw new IllegalArgumentException("Cannot move map element!\n Given position corrupted!");
//    }
//
//    void putOnPos(Object b, Vector2d newPosition, IMapElement element){
//        if(b == null){
//            objects.put(newPosition, new Unit<IMapElement>(element));
//        }else if (b instanceof Unit){
//            objects.remove(newPosition);
//            objects.put(newPosition, new Pair<IMapElement,IMapElement>((IMapElement) ((Unit) b).getValue0(), element));
//        }else if(b instanceof Pair){
//            objects.remove(newPosition);
//            objects.put(newPosition, new Triplet<IMapElement,IMapElement,IMapElement>((IMapElement)((Pair) b).getValue0(), (IMapElement)((Pair) b).getValue1(), element));
//        }
//    }

//    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element) {
//        Object a = this.objectAt(oldPosition);
//        if(a==null)
//            throw new IllegalArgumentException("Cannot move map element!\n Given position empty!");
//
//        Object b = this.objectAt(newPosition);
//        if (b instanceof Triplet)
//            throw new IllegalArgumentException("Cannot move map element!\n Destination is full!");
//
//        this.takeOutToMove(a,oldPosition,element);
//        this.putOnPos(b,newPosition,element);
//    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element){

    }

}
