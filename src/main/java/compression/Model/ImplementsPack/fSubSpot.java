package Model.ImplementsPack;

import Model.Abstract.Spot;

public class fSubSpot extends Spot {
    public int x=0;
    public float y=0;
    public float z=0;

    public fSubSpot(){}

    public fSubSpot(int x,float y){
         this.x=x;
         this.y=y;
    }
    public int getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        if(flag)
        return x+"?"+String.format("%.2f",y);
        return x+"?"+String.format("%.2f",y)+"?"+String.format("%.2f",y);
    }
}
