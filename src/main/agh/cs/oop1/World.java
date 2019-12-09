package agh.cs.oop1;


import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Preconditions;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class World {
    private static final String parametersPath = "parameters.json";
    public static void main(String []args) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            final Configuration config = gson.fromJson(new FileReader(System.getProperty("user.dir") + "/" + World.parametersPath), Configuration.class);
            System.out.println(config.toString());

            LoopedMap map = new LoopedMap(config.width, config.height, config.jungleWidth(), config.jungleHeight());
            System.out.println(map.mapLowerLeft + " | "+ map.mapUpperRight);
            System.out.println(map.jungleLowerLeft+" | "+map.jungleUpperRight);

        }catch (FileNotFoundException ex){
            System.out.println("Configuration file parameters.json not found!\n Application cannot be launched.\n"+ex.toString());
        }catch (IllegalArgumentException ex){
            System.out.println("Illegal configuration! Check parameters.json file.\n"+ex.toString());
        }
    }

}
