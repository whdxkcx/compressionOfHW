package Model.ImplementsPack;

import Model.Abstract.Spot;

public class ISubSpot extends Spot {
    public int x=0;
    public long y=0;
    public long z=0;

    public ISubSpot(){}

    public ISubSpot(int x,long y){
        this.x=x;
        this.y=y;
    }
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public long getY() {
        return y;
    }

    public void setY(long y) {
        this.y = y;
    }

    public long getZ() {
        return z;
    }

    public void setZ(long z) {
        this.z = z;
    }

    @Override
    public String toString() {

        if(flag)
            return x+"?"+y;
        return x+"?"+y+"?"+z;
    }
}
