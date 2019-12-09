package agh.cs.oop1;

class HelperLoopedMap {
    static Vector2d getUpperRight(Vector2d mapLowerLeft, int mapSideA, int mapSideB){
        return mapLowerLeft.add(new Vector2d(mapSideA,mapSideB));
    }

    static boolean checkIfInputIsLegal(int mapSideA, int mapSideB, int jungleSideA, int jungleSideB){
        if(mapSideA <= 0 || mapSideB <= 0 || jungleSideA < 0 || jungleSideB < 0)
            throw new IllegalArgumentException("Map size can not be negative!");
        if(mapSideA < jungleSideA || mapSideB < jungleSideB)
            throw new IllegalArgumentException("Map size can not be less than jungle size!");
        if(mapSideA == jungleSideA || mapSideB == jungleSideB)
            throw new IllegalArgumentException("Map size can not be equal to jungle size!");
        return true;
    }


    static boolean checkIfInputIsLegal(Vector2d mapLowerLeft, Vector2d mapUpperRight, Vector2d jungleLowerLeft, Vector2d jungleUpperRight){
        if(jungleLowerLeft.equals(mapLowerLeft))
            throw new IllegalArgumentException("Map lower left and jungle lower left are equal!");

        if(!jungleLowerLeft.lowerLeft(mapLowerLeft).equals(mapLowerLeft))
            throw new IllegalArgumentException("Map lower left is not a lower left of jungle!");
        if(mapLowerLeft.equals(mapUpperRight))
            throw new IllegalArgumentException("Map lower left equals to map upper right!");

        if(!mapUpperRight.upperRight(mapLowerLeft).equals(mapUpperRight))
            throw new IllegalArgumentException("Specified map is incorrect!");

        if(!mapUpperRight.upperRight(jungleLowerLeft).equals(mapUpperRight)  || !mapUpperRight.upperRight(jungleUpperRight).equals(mapUpperRight))
            throw new IllegalArgumentException("Jungle location / size is incorrect!");

        return true;
    }


}
