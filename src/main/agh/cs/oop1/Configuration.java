package agh.cs.oop1;

public class Configuration {
    public final int width;
    public final int height;
    public final int startEnergy;
    public final  int moveEnergy;
    public final double jungleRatio;

    Configuration(){
        this.width = this.height = this.startEnergy = this.moveEnergy =0;
        this.jungleRatio = 0;
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "width=" + width +
                ", height=" + height +
                ", startEnergy=" + startEnergy +
                ", moveEnergy=" + moveEnergy +
                ", jungleRatio=" + jungleRatio +
                '}';
    }

    public int jungleWidth(){
        return (int)((double)this.width*this.jungleRatio);
    }

    public int jungleHeight(){
        return (int)((double)this.height*this.jungleRatio);
    }


}
