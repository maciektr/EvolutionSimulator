package agh.cs.oop1.simulation;


import java.io.FileNotFoundException;

public class World {
    private static final String parametersPath = "parameters.json";

    public static void main(String []args) throws FileNotFoundException {

        try {
            final Configuration config = Configuration.fromJson(World.parametersPath);
            System.out.println(config.toString());
            LoopedMap map = new LoopedMap(config.width, config.height, config.jungleWidth(), config.jungleHeight());

        }catch(FileNotFoundException ex){
            System.out.println("Application cannot be launched!");
        }catch (IllegalArgumentException ex){
            System.out.println("Illegal configuration! Check parameters.json file.\n"+ex.toString());
        }
    }

}
