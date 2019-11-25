package agh.cs.oop1;

//Nazwa klasy odgapiona od @DenkoV
public class LoopedMap {
    final Vector2d mapLowerLeft;
    final Vector2d mapUpperRight;
    final Vector2d jungleLowerLeft;
    final Vector2d jungleUpperRight;

    public LoopedMap(int mapSideA, int mapSideB, int jungleSideA, int jungleSideB){
        HelperLoopedMap.checkIfInputIsLegal(mapSideA,mapSideB,jungleSideA,jungleSideB);

        this.mapLowerLeft = new Vector2d(0,0);
        this.mapUpperRight = new Vector2d(mapSideA,mapSideB);
        this.jungleLowerLeft = new Vector2d((mapSideA - jungleSideA)/2, (mapSideB-jungleSideB)/2);
        this.jungleUpperRight = new Vector2d(this.jungleLowerLeft.x + jungleSideA, this.jungleLowerLeft.y+jungleSideB);

        if(this.jungleUpperRight.follows(this.mapUpperRight))
            throw new  NullPointerException("Jungle position was not calculated properly!");
    }

    public LoopedMap(Vector2d mapLowerLeft, Vector2d jungleLowerLeft, int mapSideA, int mapSideB, int jungleSideA, int jungleSideB) {
        HelperLoopedMap.checkIfInputIsLegal(mapLowerLeft,jungleLowerLeft,mapSideA,mapSideB,jungleSideA,jungleSideB);

        this.mapLowerLeft = mapLowerLeft;
        this.jungleLowerLeft = jungleLowerLeft;

        this.mapUpperRight = HelperLoopedMap.getUpperRight(this.mapLowerLeft, mapSideA, mapSideB);
        this.jungleUpperRight = HelperLoopedMap.getUpperRight(this.jungleLowerLeft, jungleSideA, jungleSideB);
    }

    public LoopedMap(Vector2d mapLowerLeft, Vector2d jungleLowerLeft, Vector2d mapUpperRight, Vector2d jungleUpperRight){
        HelperLoopedMap.checkIfInputIsLegal(mapLowerLeft,mapUpperRight,jungleLowerLeft,jungleUpperRight);

        this.mapLowerLeft = mapLowerLeft;
        this.mapUpperRight = mapUpperRight;
        this.jungleLowerLeft = jungleLowerLeft;
        this.jungleUpperRight = jungleUpperRight;
    }

    public Vector2d legalPositionAfterMove(Vector2d afterMove){
        return afterMove.mod(this.mapUpperRight);
    }


}
