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

    private String startTime = "";//起始时间
    private String endTime = "";//结束时间
    private String objId = "";//对象id
    private int rowCount=0;//该对象有多少行
    private ArrayList<Bit> bitList=new ArrayList<>();//用于存储每一列压缩后的结果.

}
