package agh.cs.oop1.simulation;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class Configuration {
    public final int width;
    public final int height;
    public final int startEnergy;
    public final  int moveEnergy;
    public final int startAnimals;
    public final double jungleRatio;


    Configuration(){
        this.width = this.height = this.startEnergy = this.moveEnergy = this.startAnimals = 0;
        this.jungleRatio = 0;
    }

    public static Configuration fromJson(final String parametersPath) throws FileNotFoundException {
        Gson gson = new Gson();
        try {
            return gson.fromJson(new FileReader(System.getProperty("user.dir") + "/" + parametersPath), Configuration.class);
        }catch (FileNotFoundException ex){
            System.out.println("Configuration file not found!\n Path: "+ parametersPath+"\n"+ex.toString());
            throw ex;
        }
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "width=" + width +
                ", height=" + height +
                ", startEnergy=" + startEnergy +
                ", moveEnergy=" + moveEnergy +
                ", jungleRatio=" + jungleRatio +
                ", startAnimals="+ startAnimals +
                '}';
    }

    public int jungleWidth(){
        return (int)((double)this.width*this.jungleRatio);
    }

    public int jungleHeight(){
        return (int)((double)this.height*this.jungleRatio);
    }


}
