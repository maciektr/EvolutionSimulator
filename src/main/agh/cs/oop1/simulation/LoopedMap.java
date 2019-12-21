package agh.cs.oop1.simulation;


import java.util.*;
import java.util.function.UnaryOperator;

//Nazwa klasy odgapiona od @DenkoV
public class LoopedMap implements IWorldMap, IStateChangeObserver {
    private List<Animal> animals = new ArrayList<Animal>();
    private Map<Vector2d, MapCell> objects = new HashMap<>();

    private final Vector2d mapLowerLeft;
    private final Vector2d mapUpperRight;
    private final Vector2d jungleLowerLeft;
    private final Vector2d jungleUpperRight;
    private final Random rand = new Random();

    List<Animal> getAnimals(){
        return this.animals;
    }

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


    public MapCell getMapCell(Vector2d position){
        return this.objects.get(position);
    }

    public boolean anyAnimals(Vector2d position){
        return this.getMapCell(position) != null && this.getMapCell(position).anyAnimals();
    }

    public boolean isPlantSet(Vector2d position){
        return this.getMapCell(position) != null && this.getMapCell(position).isPlantSet();
    }

    public void placeAnimal(Animal animal){
        this.addAnimalToCell(animal);
        this.animals.add(animal);
        animal.addObserver(this);
    }

    public Vector2d getRandomPosition(){
        int w = this.mapUpperRight.x - this.mapLowerLeft.x;
        int h = this.mapUpperRight.y - this.mapLowerLeft.y;

        int randW = this.rand.nextInt(w);
        int randH = this.rand.nextInt(h);

        return this.mapLowerLeft.add(new Vector2d(randW, randH));
    }

    public Vector2d getRandomJunglePosition(){
        int w = this.jungleUpperRight.x - this.jungleLowerLeft.x;
        int h = this.jungleUpperRight.y - this.jungleLowerLeft.y;

        int randW = this.rand.nextInt(w);
        int randH = this.rand.nextInt(h);

        return this.jungleLowerLeft.add(new Vector2d(randW, randH));
    }


    private Vector2d helperGetRandomFreePos(boolean fromJungle){
        Vector2d position = (fromJungle ? this.getRandomJunglePosition() : this.getRandomPosition());
        int countOperations = 0;

        while(this.isPlantSet(position) || this.anyAnimals(position)){
            if(countOperations > (new MapEnumerator(this).mapSize())/4)
//                throw new RuntimeException("Cannot find random free position on map!");
                return position;
            position = (fromJungle ? this.getRandomJunglePosition() : this.getRandomPosition());
            countOperations++;
        }

        return position;
    }

    public Vector2d getRandomFreeOfGrassPosition(){
        return this.helperGetRandomFreePos(false);
    }

    public Vector2d getRandomFreeOfGrassJunglePosition(){
        return this.helperGetRandomFreePos(true);
    }


    private void addAnimalToCell(Animal animal){
        Vector2d position = animal.getPosition();
        MapCell cell = this.getMapCell(position);
        if(cell == null)
            cell = new MapCell();
        cell.addAnimal(animal);
        this.objects.put(position, cell);
    }

    private void removeAnimalFromCell(Animal animal) {
        MapCell cell = this.getMapCell(animal.getPosition());
        if (cell == null)
            throw new IllegalArgumentException("Animal is not placed on map!");
        cell.removeAnimal(animal);
    }

    public void setPlantToCell(Plant plant){
        Vector2d position = plant.getPosition();
        MapCell cell = this.getMapCell(position);
        if(cell == null)
            cell = new MapCell();
        cell.setPlant(plant);
        this.objects.put(position, cell);
    }

    public void positionChanged(Vector2d oldPosition, Vector2d newPosition, IMapElement element){
        if(element instanceof Animal) {
            this.getMapCell(oldPosition).removeAnimal((Animal)element);
            this.addAnimalToCell((Animal)element);
        }
    }

    public void died(IMapElement element){
        if(element instanceof Animal){
            Animal animal = (Animal)element;
            this.animals.remove(animal);
            this.removeAnimalFromCell(animal);
        }
    }
    public void energyChanged(Animal animal){
        this.removeAnimalFromCell(animal);
        if(animal.getEnergy() <= 0)
            this.animals.remove(animal);
        else
            this.addAnimalToCell(animal);
    }

    public Vector2d getMapLowerLeft(){
        return mapLowerLeft;
    }
    public Vector2d getMapUpperRight(){
        return mapUpperRight;
    }
    public Vector2d getJungleLowerLeft(){
        return  jungleLowerLeft;
    }
    public Vector2d getJungleUpperRight(){
        return jungleUpperRight;
    }
    public List<Animal> getAnimalsList(){
        return this.animals;
    }
}
