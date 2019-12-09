package agh.cs.oop1;

import java.util.Vector;
import java.util.function.UnaryOperator;

//Nazwa klasy odgapiona od @DenkoV
public class LoopedMap {
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
        Vector2d normalisedAfterMove= afterMove.substract(this.mapLowerLeft);
        Vector2d normalisedMapUpperRight = this.mapUpperRight.substract(this.mapLowerLeft);
        return this.mapLowerLeft.add(normalisedAfterMove.mod(normalisedMapUpperRight));
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
//        System.out.println(x);
        return new Vector2d(sgn.apply(x)*(Math.abs(x) -jungleSideA)/2+x%2, sgn.apply(y)*(Math.abs(y)-jungleSideB)/2+y%2);
    }

}
