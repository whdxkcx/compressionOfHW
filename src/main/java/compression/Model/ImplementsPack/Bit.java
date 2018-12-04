package Model.ImplementsPack;

import java.io.Serializable;
import java.util.ArrayList;

//用于存储对一个列压缩后的数据
public class Bit implements Serializable {

    //用于存比特位
    public ArrayList<Byte> b=new ArrayList<>();



    //用一个Map来存原值，以<索引，值>的形式
//    public Map<Integer,Float> valMap=new HashMap<>();
    //用一个ArrayList数组来存原值数组，即三种曲线类型都无法预测的值。
    public  ArrayList<Float> valList=new ArrayList<>();
    //用两个float型的字符来存储原值数组中的最大值和最小值。
    private float maxVal=-Float.MAX_VALUE;
    private float minVal=Float.MAX_VALUE;
    //用于判断当前列是否为全零列
    private boolean isZero=true;

    public boolean isZero() {
        return isZero;
    }

    public void setZero(boolean zero) {
        isZero = zero;
    }

    //比特位的索引
    public int index=0;
    //存储两个比特位
    public void addBit(String bit){
        int intIndex=index/4;
        int bitIndex=(index*2)%8;
        if(b.size()<=intIndex) b.add((byte)0);
        switch (bit){
            case "00":break;//不用改变
            case "01":b.set(intIndex,(byte)(b.get(intIndex)|(1<<bitIndex)));//用01左移
                break;
            case "10":b.set(intIndex,(byte)(b.get(intIndex)|(2<<bitIndex)));//用10左移
                break;
            case "11":b.set(intIndex,(byte)(b.get(intIndex)|(3<<bitIndex)));//用11左移
                break;
        }
        //元素个数加1；
        index++;
    }


    //根据索引获取两个比特位
    public String  getBit(int index){
        int intIndex=index/4;
        int bitIndex=(index*2)%8;
        int temp=(b.get(intIndex)>>>bitIndex)&3;

        switch (temp){
            case 0:return "00";
            case 1:return "01";
            case 2:return "10";
        }
        return "11";
    }

    //存储原值
    public void putVal(int i,float val){
        valList.add(val);

        //在存储原值的同时，更新原值数组的最大值和最小值
        if(val>maxVal) maxVal=val;
        else if(val<minVal) minVal=val;
    }


    /**

     *@描述  返回原值数组的中值

     *@参数  []

     *@返回值  float

     *@创建人  kcx

     *@创建时间  2018/11/27

     *@修改人和其它信息

     */
    public float getMiddleVal(){
        return  (maxVal+minVal)/2;
    }

    /**

     *@描述  返回原值数组的半径

     *@参数  []

     *@返回值  float

     *@创建人  kcx

     *@创建时间  2018/11/27

     *@修改人和其它信息

     */
    public float getRadiusVal(){
        return (maxVal-minVal)/2;
    }

    //根据索引获取原值
    public  float getVal(int i){
        return valList.get(i);
    }

    @Override
    public String toString() {
       return super.toString();
    }

}
