package Model.ImplementsPack;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2018/12/17
 * @描述  用于保存一列的数据，包括数据值，中值和标准差等。
 */
public class Column {
    private ArrayList<String> valList=null;//存储一列的值
    private int index=0;//存储一列的索引，即代表是第几列
    //最大值和最小值，用于求该列的中值
    private float maxValue =-Float.MAX_VALUE;
    private float minValue=Float.MAX_VALUE;
    //标准差
    private float standardDeviation = 0;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public ArrayList<String> getValList() {
        return valList;
    }

    public void setValList(ArrayList<String> valList) {
        this.valList = valList;
    }

    //获取该列的中值。
    public float getMiddle(){
        return (maxValue+minValue)/2;
    }

    public void addVal(String val){
        if(valList==null)  valList=new ArrayList<>();
        valList.add(val);
        float input=Float.parseFloat(val);
        if(input>maxValue) maxValue=input;
        if(input<minValue) minValue=input;
    }

    public String getVal(int i){
        return valList.get(i);
    }
}
