package Model.ImplementsPack;

import Model.Abstract.Spot;

import java.util.ArrayList;

public class AfterObject {
    private String startTime = "";
    private String endTime = "";
    private String objId = "";
    private ArrayList<ArrayList<Spot>> quotaList = null;

    public ArrayList<ArrayList<Spot>> getQuotaList() {
        return quotaList;
    }

    public void setQuotaList(ArrayList<ArrayList<Spot>> quotaList) {
        this.quotaList = quotaList;
    }

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
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
