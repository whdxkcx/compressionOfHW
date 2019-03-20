package Model.ImplementsPack;

import Model.Abstract.Spot;
import Tools.InterfacePack.DataDeal;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class fSubSpot extends Spot {
    public int x=0;
    public float y=0;
    public float z=0;
    private ArrayList<Float> outOfRangeList=null;

    public fSubSpot(){}

    public fSubSpot(int x,float y){
         this.x=x;
         this.y=y;
    }


    public ArrayList<Float> getOutOfRangeList() {
        return outOfRangeList;
    }

    public void setOutOfRangeList(ArrayList<Float> outOfRangeList) {
        this.outOfRangeList = outOfRangeList;
    }

    /**

     *@描述  添加不在范围内的值

     *@参数  [val]

     *@返回值  boolean

     *@创建人  kcx

     *@创建时间  2018/12/19

     *@修改人和其它信息

     */
    public boolean addVal(float val){

        if(outOfRangeList==null)  outOfRangeList=new ArrayList<>();
        if(outOfRangeList.size()>=3) return false;
        outOfRangeList.add(val);
        return true;
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
        String str="";
        if(y==z)
            if(y!=0) str=x + ":" + y;
            else  str=x+":";
        else str=x + ":" +  y + ":" + z;
        if(outOfRangeList!=null)
        for(int i=0;i< outOfRangeList.size();i++)
            str+=":"+ DataDeal.DataSimplify(outOfRangeList.get(i)+"");
        return str;
    }
}
