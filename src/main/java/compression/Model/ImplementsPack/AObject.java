package Model.ImplementsPack;

import java.util.ArrayList;

public class AObject {
    private String startTime = "";
    private String endTime = "";
    private String objId = "";
    private ArrayList<String> quotaList = null;
    private float maxValue =Float.MAX_VALUE;
    private float minValue=-Float.MAX_VALUE;
    private float standardDeviation = 0;

     //获取该列的中值。
    public float getMiddle(){
        return (maxValue+minValue)/2;
    }

    public void addVal(String val){
        if(quotaList==null)  quotaList=new ArrayList<>();
        if(val.equals("NA")) val="0";
        quotaList.add(val);
    }

    public String getVal(int i){
        return quotaList.get(i);
    }

    public float getStandardDeviation() {
        return standardDeviation;
    }

    public void setStandardDeviation(float standardDeviation) {
        this.standardDeviation = standardDeviation;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public ArrayList<String> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(ArrayList<String> quotaList) {
        this.quotaList = quotaList;
    }

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
}
