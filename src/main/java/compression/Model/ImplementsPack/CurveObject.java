package Model.ImplementsPack;

import Model.Abstract.Spot;

import java.util.ArrayList;

/**
 * @创建人 kcx
 * @创建时间 2018/10/31
 * @描述 用于存储对一个对象压缩完了之后的数据
 */
public class CurveObject {
    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public ArrayList<Bit> getBitList() {
        return bitList;
    }

    public void setBitList(ArrayList<Bit> bitList) {
        this.bitList = bitList;
    }
    public void addBit(String bit){
        int intIndex=index/8;
        int bitIndex=index%8;
        if(b.size()<=intIndex) b.add((byte)0);
        switch (bit){
            case "0":break;//不用改变
            case "1":b.set(intIndex,(byte)(b.get(intIndex)|(1<<bitIndex)));//用1左移
                break;
        }
        //元素个数加1；
        index++;
    }
    private String startTime = "";//起始时间
    private String endTime = "";//结束时间
    private String objId = "";//对象id
    private int rowCount=0;//该对象有多少行
    private ArrayList<Bit> bitList=new ArrayList<>();//用于存储每一列压缩后的结果.
    public ArrayList<Byte> b=new ArrayList<>();//用于存储列的类型吗，1代表全零列，0代表非全零列。
    //比特位的索引
    public int index=0;


}
