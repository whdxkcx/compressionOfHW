package Model.ImplementsPack;

import Model.Abstract.Spot;
import Tools.InterfacePack.DataDeal;

import java.util.ArrayList;

public class ISubSpot extends Spot {
    public int x=0;
    public long y=0;
    public long z=0;
    private ArrayList<Long> outOfRangeList=null;
    public ISubSpot(){}

    public ISubSpot(int x,long y){
        this.x=x;
        this.y=y;
    }


    /**

     *@描述  添加不在范围内的值。

     *@参数  [val]

     *@返回值  boolean

     *@创建人  kcx

     *@创建时间  2018/12/19

     *@修改人和其它信息

     */
    public boolean addVal(Long val){

        if(outOfRangeList==null)  outOfRangeList=new ArrayList<>();
        if(outOfRangeList.size()>=3) return false;
        outOfRangeList.add(val);
        return true;
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
        String str="";
        if(y==z)
            if(y!=0) str=x + ":" + y;
            else   str=x+":";
        else str=x+":"+y+":"+z;
        if(outOfRangeList!=null)
        for(int i=0;i< outOfRangeList.size();i++)
            str+=":"+DataDeal.DataSimplify(outOfRangeList.get(i)+"");
        return str;
    }
}
