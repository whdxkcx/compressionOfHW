package Model.ImplementsPack;

import java.util.ArrayList;

public class AObject {
    private String startTime = "";
    private String endTime = "";
    private String objId = "";
    private ArrayList<String> quotaList = null;

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
